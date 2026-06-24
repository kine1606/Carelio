package com.Carelio.service_order_service.service;

import com.Carelio.service_order_service.client.WorkerClient;
import com.Carelio.service_order_service.client.dto.WorkerProfileResponse;
import com.Carelio.service_order_service.dto.request.OrderReviewRequest;
import com.Carelio.service_order_service.dto.response.OrderReviewResponse;
import com.Carelio.service_order_service.entity.Order;
import com.Carelio.service_order_service.entity.OrderReview;
import com.Carelio.service_order_service.entity.ServiceOrderStatus;
import com.Carelio.service_order_service.mapper.OrderReviewMapper;
import com.Carelio.service_order_service.repository.OrderRepository;
import com.Carelio.service_order_service.repository.OrderReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // Tối ưu hóa các thao tác đọc (GET) mặc định là Read-Only
@Slf4j
public class OrderReviewService {

    private final OrderReviewMapper orderReviewMapper;
    private final OrderRepository orderRepository;
    private final OrderReviewRepository orderReviewRepository;
    private final WorkerClient workerClient;

    private Order getOrderEntity(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));
    }

    private void validateOrderAccess(Order order, String userId) {
        if (order.getUserId().equals(userId)) {
            return;
        }
        if (order.getWorkerId() != null) {
            try {
                WorkerProfileResponse workerProfile = workerClient.getWorkerByKeycloakId(userId);
                if (workerProfile.getId().equals(order.getWorkerId())) {
                    return; // Hợp lệ!
                }
            } catch (Exception e) {
                log.error("Không thể xác thực vai trò Thợ từ Worker Service", e);
            }
        }

        throw new RuntimeException("Bạn không có quyền truy cập thông tin của đơn hàng này!");
    }

    // POST /api/service-orders/{orderId}/reviews
    @Transactional
    @CachePut(value = "ORDER_REVIEW_CACHE", key = "#orderId")
    public OrderReviewResponse createReview(String userId, Long orderId, OrderReviewRequest request) {
        Order order = getOrderEntity(orderId);

        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("Bạn không có quyền đánh giá đơn hàng của người khác!");
        }

        if (order.getStatus() != ServiceOrderStatus.COMPLETED) {
            throw new IllegalStateException("Đơn hàng phải ở trạng thái COMPLETED mới có thể đánh giá.");
        }

        if (orderReviewRepository.findByOrderIdAndUserId(orderId, userId).isPresent()) {
            throw new IllegalStateException("Bạn đã thực hiện đánh giá cho đơn hàng này rồi.");
        }

        OrderReview review = OrderReview.builder()
                .orderId(orderId)
                .userId(userId)
                .workerId(order.getWorkerId())
                .rating(request.getRating())
                .comment(request.getComment())
                .build();

        OrderReview saved = orderReviewRepository.save(review);

        try {
            workerClient.updateRating(review.getWorkerId(), review.getRating());
            log.info("Đã cập nhật điểm trung bình ngầm sang Worker Service cho thợ ID: {}", review.getWorkerId());
        } catch (Exception e) {
            log.error("Gặp sự cố kết nối, không thể tự động cập nhật Rating sang Worker Service", e);
        }

        log.info("Khách hàng {} đã tạo review thành công cho đơn hàng: {}", userId, orderId);
        return orderReviewMapper.toResponse(saved);
    }

    // GET /api/service-orders/{orderId}/reviews
    @Cacheable(value = "ORDER_REVIEW_CACHE", key = "#orderId")
    public OrderReviewResponse getReview(String userId, Long orderId)
    {
        Order order = getOrderEntity(orderId);
        validateOrderAccess(order, userId);
        OrderReview review = orderReviewRepository.findByOrderId(orderId);
        return orderReviewMapper.toResponse(review);
    }
}