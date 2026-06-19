package com.Carelio.service_order_service.controller;

import com.Carelio.service_order_service.dto.request.OrderReviewRequest;
import com.Carelio.service_order_service.dto.response.OrderReviewResponse;
import com.Carelio.service_order_service.service.OrderReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/service-orders")
@RequiredArgsConstructor
public class OrderReviewController
{
    private final OrderReviewService orderReviewService;

    @PostMapping("/{orderId}/reviews")
    public ResponseEntity<OrderReviewResponse> createReview(
            @RequestHeader("X-USER-ID") Long userId,
            @PathVariable Long orderId,
            @RequestBody OrderReviewRequest request)
    {
        return ResponseEntity.ok(orderReviewService.createReview(userId, orderId, request));
    }

    @GetMapping("/{orderId}/reviews")
    public ResponseEntity<List<OrderReviewResponse>> getReviews(
            @RequestHeader("X-USER-ID") Long userId,
            @PathVariable Long orderId)
    {
        return ResponseEntity.ok(orderReviewService.getReviews(userId, orderId));
    }
}
