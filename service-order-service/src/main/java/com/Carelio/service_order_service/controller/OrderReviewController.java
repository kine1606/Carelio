package com.Carelio.service_order_service.controller;

import com.Carelio.service_order_service.dto.request.OrderReviewRequest;
import com.Carelio.service_order_service.dto.response.OrderReviewResponse;
import com.Carelio.service_order_service.service.OrderReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/service-orders")
@RequiredArgsConstructor
public class OrderReviewController
{

    private final OrderReviewService orderReviewService;

    // POST /api/service-orders/{orderId}/review
    @PostMapping("/{orderId}/review")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<OrderReviewResponse> createReview(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long orderId,
            @RequestBody @Valid OrderReviewRequest request)
    {
        String userId = jwt.getSubject();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderReviewService.createReview(userId, orderId, request));
    }

    // GET /api/service-orders/{orderId}/review
    @GetMapping("/{orderId}/review")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('WORKER') or hasRole('ADMIN')")
    public ResponseEntity<OrderReviewResponse> getReview(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long orderId)
    {
        String userId = jwt.getSubject();
        return ResponseEntity.ok(orderReviewService.getReview(userId, orderId));
    }
}