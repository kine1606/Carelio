package com.Carelio.service_order_service.service;

import com.Carelio.service_order_service.client.HouseholdClient;
import com.Carelio.service_order_service.client.WorkerClient;
import com.Carelio.service_order_service.client.dto.WorkerSkillResponse;
import com.Carelio.service_order_service.client.dto.WorkerStatus;
import com.Carelio.service_order_service.dto.request.AssignWorkerRequest;
import com.Carelio.service_order_service.dto.request.OrderRequest;
import com.Carelio.service_order_service.dto.request.UpdateOrderRequest;
import com.Carelio.service_order_service.dto.request.UpdateOrderStatusRequest;
import com.Carelio.service_order_service.dto.response.OrderResponse;
import com.Carelio.service_order_service.entity.Order;
import com.Carelio.service_order_service.entity.PriceCatalog;
import com.Carelio.service_order_service.entity.ServiceOrderStatus;
import com.Carelio.service_order_service.mapper.OrderMapper;
import com.Carelio.service_order_service.repository.OrderRepository;
import com.Carelio.service_order_service.repository.PriceCatalogRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class OrderService
{

    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final HouseholdClient householdClient;
    private final WorkerClient workerClient;
    private final PriceCatalogRepository priceCatalogRepository;
    // =========================================================================
    // SECTION 1: CÁC THAO TÁC CRUD (CUSTOMER LAYER)
    // =========================================================================

    // GET /api/service-orders/{orderId}
    @Cacheable(value = "ORDER_CACHE", key = "#orderId")
    public OrderResponse getById(String userId, Long orderId)
    {
        Order order = getOrderEntity(userId, orderId);
        log.info("Order found with id: {} for customer UUID: {}", orderId, userId);
        return orderMapper.toResponse(order);
    }

    // GET /api/internal/service-orders/{orderId}
    @Cacheable(value = "ORDER_CACHE", key = "#orderId")
    public OrderResponse getByIdInternal(Long orderId)
    {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));
        log.info("Internal request: Order found with id: {}", orderId);
        return orderMapper.toResponse(order);
    }

    // GET /api/service-orders
    public List<OrderResponse> getAll(String userId)
    { // <-- Đổi sang String userId
        List<Order> orders = orderRepository.findAllByUserId(userId);
        log.info("Found {} orders for customer UUID: {}", orders.size(), userId);
        return orderMapper.toResponseList(orders);
    }

    // POST /api/service-orders
    @Transactional
    @CachePut(value = "ORDER_CACHE", key = "#result.id")
    public OrderResponse createOrder(String userId, OrderRequest request)
    {
        var evResponse = householdClient.validate(
                request.getEquipmentId(),
                request.getRoomId(),
                request.getHouseId()
        );

        var ssResponse = workerClient.getServiceSkill(request.getServiceSkillId());

        BigDecimal calculatedPrice = priceCatalogRepository
                .findByEquipmentCategoryIdAndServiceSkillId(request.getEquipmentCategoryId(), request.getServiceSkillId())
                .map(PriceCatalog::getPrice)
                .orElseGet(() -> {
                    log.warn("Chưa cấu hình giá cho Cặp thiết bị {} và Dịch vụ {}. Đang áp dụng giá sàn mặc định.",
                            request.getEquipmentCategoryId(), request.getServiceSkillId());
                    return new BigDecimal("200000");
                });
        Order order = orderMapper.toEntity(request, userId, evResponse, ssResponse);
        order.setPrice(calculatedPrice);
        Order saved = orderRepository.save(order);
        log.info("Order created successfully with ID: {} by user: {}", saved.getId(), userId);
        return orderMapper.toResponse(saved);
    }

    // DELETE /api/service-orders/{orderId}
    @Transactional
    @CacheEvict(value = "ORDER_CACHE", key ="#orderId")
    public OrderResponse deleteOrder(String userId, Long orderId)
    {
        Order order = getOrderEntity(userId, orderId);
        order.setStatus(ServiceOrderStatus.CANCELLED);
        Order saved = orderRepository.save(order);
        log.info("Order ID: {} cancelled successfully by customer: {}", orderId, userId);
        return orderMapper.toResponse(saved);
    }

    // PATCH /api/service-orders/{orderId}
    @Transactional
    @CachePut(value = "ORDER_CACHE", key = "#result.id")
    public OrderResponse updateOrder(String userId, Long orderId, UpdateOrderRequest request)
    {
        Order order = getOrderEntity(userId, orderId);
        order.setTitle(request.getTitle());
        order.setDescription(request.getDescription());
        order.setScheduledAt(request.getScheduledTime());

        Order saved = orderRepository.save(order);
        log.info("Order ID: {} updated successfully by customer: {}", orderId, userId);
        return orderMapper.toResponse(saved);
    }

    private Order getOrderEntity(String userId, Long orderId)
    {
        return orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId + " for user: " + userId));
    }

    // =========================================================================
    // SECTION 2: WORKFLOW & TRẠNG THÁI (INTERNAL LAYER)
    // =========================================================================

    // PATCH /api/orders/{id}/assign
    @Transactional
    @CachePut(value = "ORDER_CACHE", key = "#orderId")
    public OrderResponse assignWorker(String userId, Long orderId, AssignWorkerRequest workerRequest) throws Exception
    {
        Order order = getOrderEntity(userId, orderId);
        if (order.getStatus() != ServiceOrderStatus.POSTED) {
            throw new RuntimeException("Only allowed to assign worker when order status is POSTED");
        }

        List<WorkerSkillResponse> workerSkills = workerClient.getWorkerSkills(workerRequest.getWorkerId());
        if (workerSkills == null || workerSkills.isEmpty()) {
            throw new RuntimeException("Worker has no skills or does not exist");
        }

        var workerProfile = workerSkills.get(0).getWorkerProfileResponse();
        if (workerProfile.getStatus() != WorkerStatus.AVAILABLE)
        {
            throw new RuntimeException("Current worker is not ready for this order");
        }

        boolean isSuitable = false;
        for (WorkerSkillResponse workerSkill : workerSkills)
        {
            if (workerSkill.getServiceSkillResponse().getId().equals(order.getServiceSkillId())
                    && workerSkill.getEquipmentCategoryId().equals(order.getEquipmentCategoryId()))
            {
                isSuitable = true;
                break;
            }
        }
        if (!isSuitable) {
            throw new RuntimeException("Worker is not suitable for this order");
        }

        Order saved = saveOrderAssignment(order, workerProfile.getId());
        log.info("Order ID: {} assigned to worker ID: {} successfully", orderId, workerProfile.getId());
        return orderMapper.toResponse(saved);
    }

    @Transactional
    public Order saveOrderAssignment(Order order, Long workerId)
    {
        order.setWorkerId(workerId);
        order.setScheduledAt(LocalDateTime.now());
        order.setStatus(ServiceOrderStatus.CLAIMED);
        return orderRepository.save(order);
    }

    // Phục vụ API nội bộ /accept công việc từ phía Thợ
    @Transactional
    @CachePut(value = "ORDER_CACHE", key = "#orderId")
    public OrderResponse processAcceptOrder(Long orderId, Long workerId)
    {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));

        if (order.getStatus() != ServiceOrderStatus.POSTED)
        {
            throw new RuntimeException("Current status (" + order.getStatus() + ") is unavailable to work with (Must be POSTED)");
        }

        order.setStatus(ServiceOrderStatus.CLAIMED);
        order.setWorkerId(workerId);
        Order saved = orderRepository.save(order);
        log.info("Order ID: {} was claimed successfully by worker ID: {}", orderId, workerId);
        return orderMapper.toResponse(saved);
    }

    // Phục vụ API nội bộ /start công việc từ phía Thợ
    @Transactional
    @CachePut(value = "ORDER_CACHE", key = "#orderId")
    public OrderResponse processStartOrder(Long orderId, Long workerId)
    {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));

        if (!order.getWorkerId().equals(workerId)) {
            throw new RuntimeException("This worker ID: " + workerId + " is not assigned for order ID: " + orderId);
        }
        if (!order.getStatus().equals(ServiceOrderStatus.CLAIMED)) {
            throw new RuntimeException("Current status (" + order.getStatus() + ") is unavailable to work with (Must be CLAIMED)");
        }

        order.setStatus(ServiceOrderStatus.IN_PROGRESS);
        Order saved = orderRepository.save(order);
        log.info("Order ID: {} change status to IN_PROGRESS successfully by worker ID: {}", orderId, workerId); // <-- Đã sửa lỗi log
        return orderMapper.toResponse(saved);
    }

    // Phục vụ API nội bộ /complete
    @Transactional
    @CachePut(value = "ORDER_CACHE", key = "#orderId")
    public OrderResponse processCompleteOrder(Long orderId, Long workerId)
    {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));

        if (!order.getWorkerId().equals(workerId)) {
            throw new RuntimeException("This worker ID: " + workerId + " is not assigned for order ID: " + orderId);
        }
        if (order.getStatus() != ServiceOrderStatus.IN_PROGRESS) {
            throw new RuntimeException("Current status (" + order.getStatus() + ") is unavailable to work with (Must be IN_PROGRESS)");
        }

        order.setStatus(ServiceOrderStatus.COMPLETED);
        Order saved = orderRepository.save(order);
        log.info("Order ID: {} change status to COMPLETED successfully by worker ID: {}", orderId, workerId); // <-- Đã sửa lỗi log
        return orderMapper.toResponse(saved);
    }

    // Cập nhật trạng thái thủ công (Dành cho Admin hoặc luồng khẩn cấp)
    @Transactional
    @CachePut(value = "ORDER_CACHE", key = "#orderId")
    public OrderResponse updateStatus(String userId, Long orderId, UpdateOrderStatusRequest request)
    {
        Order order = getOrderEntity(userId, orderId);
        order.setStatus(request.getStatus());
        Order saved = orderRepository.save(order);
        log.info("Order ID: {} updated status to {} successfully by user {}", orderId, request.getStatus(), userId);
        return orderMapper.toResponse(saved);
    }

    @Transactional
    @CacheEvict(value = "ORDER_CACHE", key = "#orderId")
    public OrderResponse processMarkOrderAsPaid(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));

        if (order.getStatus() != ServiceOrderStatus.COMPLETED)
        {
            throw new IllegalStateException("Đơn hàng phải ở trạng thái COMPLETED mới được phép chuyển sang PAID!");
        }

        order.setStatus(ServiceOrderStatus.PAID); // Chính thức đóng vòng đời đơn hàng thành công
        Order saved = orderRepository.save(order);
        log.info("Đơn hàng số {} đã chính thức chuyển trạng thái sang PAID.", orderId);
        return orderMapper.toResponse(saved);
    }

    @Transactional
    @CacheEvict(value = "ORDER_CACHE", key = "#orderId")
    public void processMarkOrderAsFailed(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));

        // Nghiệp vụ linh hoạt: Khi thanh toán lỗi, ta giữ nguyên trạng thái COMPLETED
        // để Khách hàng có thể bấm nút "Thử lại" hoặc đổi sang phương thức thanh toán khác (như Tiền mặt)
        log.warn("Đơn hàng số {} thanh toán không thành công. Giữ nguyên trạng thái COMPLETED chờ xử lý lại.", orderId);
    }
}