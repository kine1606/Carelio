package com.Carelio.service_order_service.service;

import com.Carelio.service_order_service.client.WorkerClient;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class OrderReviewService
{
    private final OrderReviewMapper orderReviewMapper;
    private final OrderRepository orderRepository;
    private final OrderReviewRepository orderReviewRepository;
    private final WorkerClient workerClient;

    private Order getOrderEntity(Long userId, Long orderId)
    {
        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));
        return order;
    }
    //POST /api/orders/{orderId}/reviews
    public OrderReviewResponse createReview(Long userId, Long orderId, OrderReviewRequest request)
    {
        Order order = getOrderEntity(userId, orderId);
        if (order.getStatus() != ServiceOrderStatus.COMPLETED) {
            throw new IllegalStateException("Order must be COMPLETED before submitting a review.");
        }
        if (orderReviewRepository.findByOrderIdAndUserId(orderId, userId).isPresent()) {
            throw new IllegalStateException("You have already reviewed this order.");
        }
        OrderReview review = OrderReview.builder()
                .orderId(orderId)
                .userId(userId)
                .workerId(order.getWorkerId())
                .rating(request.getRating())
                .comment(request.getComment())
                .build();
        OrderReview saved = orderReviewRepository.save(review);

        // update RatingAvg of worker after done reviewing
        workerClient.updateRating(review.getWorkerId(), review.getRating());
        log.info("Review created successfully for orderId: {}", orderId);
        return orderReviewMapper.toResponse(saved);
    }

    //GET /api/orders/{orderId}/reviews
    public List<OrderReviewResponse> getReviews(Long userId, Long orderId)
    {
        getOrderEntity(userId, orderId);
        List<OrderReview> reviews = orderReviewRepository.findAllByOrderId(orderId);
        log.info("Found {} reviews for orderId: {}", reviews.size(), orderId);
        return orderReviewMapper.toResponseList(reviews);
    }
}
