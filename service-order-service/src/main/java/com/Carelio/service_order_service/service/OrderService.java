package com.Carelio.service_order_service.service;

import com.Carelio.service_order_service.client.HouseholdClient;
import com.Carelio.service_order_service.client.WorkerClient;
import com.Carelio.service_order_service.client.dto.EquipmentValidationResponse;
import com.Carelio.service_order_service.client.dto.ServiceSkillResponse;
import com.Carelio.service_order_service.client.dto.WorkerSkillResponse;
import com.Carelio.service_order_service.client.dto.WorkerStatus;
import com.Carelio.service_order_service.dto.request.AssignWorkerRequest;
import com.Carelio.service_order_service.dto.request.OrderRequest;
import com.Carelio.service_order_service.dto.request.UpdateOrderRequest;
import com.Carelio.service_order_service.dto.request.UpdateOrderStatusRequest;
import com.Carelio.service_order_service.dto.response.OrderResponse;
import com.Carelio.service_order_service.entity.Order;
import com.Carelio.service_order_service.entity.ServiceOrderStatus;
import com.Carelio.service_order_service.mapper.OrderMapper;
import com.Carelio.service_order_service.mapper.OrderReviewMapper;
import com.Carelio.service_order_service.repository.OrderRepository;
import com.Carelio.service_order_service.repository.OrderReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class OrderService
{
    private final OrderMapper orderMapper;
    private final OrderReviewMapper orderReviewMapper;
    private final OrderRepository orderRepository;
    private final OrderReviewRepository orderReviewRepository;
    private final HouseholdClient householdClient;
    private final WorkerClient workerClient;

    //===========================================CRUD==================================================
    //GET /api/orders/{id}
    public OrderResponse getById(Long userId, Long orderId)
    {
        Order order = getOrderEntity(userId, orderId);
        log.info("Order found with id: {} and userId: {}", orderId, userId);
        return orderMapper.toResponse(order);
    }

    //GET /api/orders
    public List<OrderResponse> getAll(Long userId)
    {
        List<Order> orders = orderRepository.findAllByUserId(userId);
        log.info("Found {} orders", orders.size());

        return orderMapper.toResponseList(orders);
    }

    //POST /api/orders
    @Transactional
    public OrderResponse createOrder(Long userId, OrderRequest request)
    {
        EquipmentValidationResponse evResponse = householdClient.validate(
                userId,
                request.getEquipmentId(),
                request.getRoomId(),
                request.getHouseId()
        );

        ServiceSkillResponse ssResponse = workerClient.getServiceSkill(
                request.getServiceSkillId()
        );
        Order order = orderMapper.toEntity(request, userId, evResponse, ssResponse);
        Order saved = orderRepository.save(order);
        log.info("Order created successfully: {}", order);

        return orderMapper.toResponse(saved);
    }

    //DELETE /api/orders/{id}
    @Transactional
    public OrderResponse deleteOrder(Long userId, Long orderId)
    {
        Order order = getOrderEntity(userId, orderId);
        order.setStatus(ServiceOrderStatus.CANCELLED);
        Order saved = orderRepository.save(order);
        log.info("Order deleted successfully: {}", order);

        return orderMapper.toResponse(saved);
    }

    //PATCH /api/orders/{id}
    @Transactional
    public OrderResponse updateOrder(Long userId, Long orderId, UpdateOrderRequest request)
    {
        Order order = getOrderEntity(userId, orderId);
        order.setTitle(request.getTitle());
        order.setDescription(request.getDescription());
        order.setScheduledAt(request.getScheduledTime());

        Order saved = orderRepository.save(order);
        log.info("Order updated successfully: {}", order);
        return orderMapper.toResponse(saved);
    }

    private Order getOrderEntity(Long userId, Long orderId)
    {
        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));
        return order;
    }

    //==================WORKFLOW====================
//    PATCH /api/orders/{id}/assign
    @Transactional
    public OrderResponse assignWorker(Long userId, Long orderId, AssignWorkerRequest workerRequest) throws Exception
    {
        // status = PENDING
        // worker status = available
        // skill is suitable for this job.
        Order order = getOrderEntity(userId, orderId);
        if (order.getStatus() != ServiceOrderStatus.POSTED) {
            throw new BadRequestException("only assigned when status = POSTED");
        }
        List<WorkerSkillResponse> workerSkills = workerClient.getWorkerSkills(workerRequest.getWorkerId());
        if (workerSkills == null || workerSkills.isEmpty()) {
            throw new BadRequestException("Worker has no skills or does not exist");
        }
        var workerProfile = workerSkills.get(0).getWorkerProfileResponse();
        if (workerProfile.getStatus() != WorkerStatus.AVAILABLE) {
            throw new BadRequestException("Current worker is not ready for this order");
        }
        boolean isSuitable = false;
        for (WorkerSkillResponse workerSkill : workerSkills) {
            if (workerSkill.getServiceSkillResponse().getId().equals(order.getServiceSkillId())
                    && workerSkill.getEquipmentCategoryId().equals(order.getEquipmentCategoryId())) {
                isSuitable = true;
                break;
            }
        }
        if (!isSuitable) {
            throw new BadRequestException("Worker is not suitable for this order");
        }
        Order saved = saveOrderAssignment(order, workerProfile.getId());
        log.info("Order assigned successfully: {}", order);
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

    //PATCH /api/service-orders/{id}/accept
    @Transactional
    public OrderResponse processAcceptOrder(Long orderId)
    {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));

        if (order.getStatus() != ServiceOrderStatus.POSTED) {
            throw new RuntimeException("Current status (" +order.getStatus() +") is unavaiable to work with (Must be POSTED)");
        }

        order.setStatus(ServiceOrderStatus.CLAIMED);
        orderRepository.save(order);
        log.info("Order change status to CLAIMED successfully", orderId);
        return orderMapper.toResponse(order);
    }
    //PATCH /api/service-orders/{id}/complete
    @Transactional
    public OrderResponse processCompleteOrder(Long orderId)
    {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));

        if (order.getStatus() != ServiceOrderStatus.IN_PROGRESS) {
            throw new RuntimeException("Current status (" +order.getStatus() +") is unavaiable to work with (Must be IN_PROGRESS)");
        }

        order.setStatus(ServiceOrderStatus.COMPLETED);
        orderRepository.save(order);
        log.info("Order change status to COMPLETED successfully", orderId);
        return  orderMapper.toResponse(order);
    }

    //PATCH /api/orders/{id}/status
    @Transactional
    public OrderResponse updateStatus(Long userId, Long orderId, UpdateOrderStatusRequest request)
    {
        Order order = getOrderEntity(userId, orderId);
        order.setStatus(request.getStatus());
        Order saved = orderRepository.save(order);
        log.info("Order updated status successfully: {}", order);
        return orderMapper.toResponse(saved);
    }


}