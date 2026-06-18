package com.Carelio.service_order_service.controller;

import com.Carelio.service_order_service.dto.request.OrderAttachmentRequest;
import com.Carelio.service_order_service.dto.request.OrderRequest;
import com.Carelio.service_order_service.dto.request.OrderReviewRequest;
import com.Carelio.service_order_service.dto.request.UpdateOrderRequest;
import com.Carelio.service_order_service.dto.response.OrderAttachmentResponse;
import com.Carelio.service_order_service.dto.response.OrderResponse;
import com.Carelio.service_order_service.dto.response.OrderReviewResponse;
import com.Carelio.service_order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/service-orders")
@RequiredArgsConstructor
public class ServiceOrderController
{
    private final OrderService orderService;
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders(@RequestHeader("X-USER-ID") Long userId)
    {
        return ResponseEntity.ok( orderService.getAll(userId));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(
            @RequestHeader("X-USER-ID") Long userId,
            @PathVariable Long orderId)
    {
        return ResponseEntity.ok( orderService.getById(userId, orderId));
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @RequestHeader("X-USER-ID") Long userId,
            @RequestBody OrderRequest orderRequest)
    {
        return ResponseEntity.ok(orderService.createOrder(userId, orderRequest));
    }

    @PatchMapping("/{orderId}")
    public ResponseEntity<OrderResponse> updateOrder(
            @RequestHeader("X-USER-ID") Long userId,
            @PathVariable Long orderId,
            @RequestBody UpdateOrderRequest update)
    {
        return ResponseEntity.ok(orderService.updateOrder(userId, orderId, update));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<OrderResponse> deleteOrder(
            @RequestHeader("X-USER-ID") Long userId,
            @PathVariable Long orderId)
    {
        return ResponseEntity.ok(orderService.deleteOrder(userId, orderId));
    }

    // ==================== Attachments ====================

    @PostMapping("/{orderId}/attachments")
    public ResponseEntity<OrderAttachmentResponse> createAttachment(
            @RequestHeader("X-USER-ID") Long userId,
            @PathVariable Long orderId,
            @RequestBody OrderAttachmentRequest request)
    {
        return ResponseEntity.ok(orderService.createAttachment(userId, orderId, request));
    }

    @GetMapping("/{orderId}/attachments")
    public ResponseEntity<List<OrderAttachmentResponse>> getAttachments(
            @RequestHeader("X-USER-ID") Long userId,
            @PathVariable Long orderId)
    {
        return ResponseEntity.ok(orderService.getAttachments(userId, orderId));
    }

    @DeleteMapping("/{orderId}/attachments/{attachmentId}")
    public ResponseEntity<Void> deleteAttachment(
            @RequestHeader("X-USER-ID") Long userId,
            @PathVariable Long orderId,
            @PathVariable Long attachmentId)
    {
        orderService.deleteAttachment(userId, orderId, attachmentId);
        return ResponseEntity.noContent().build();
    }

    // ==================== Reviews ====================

    @PostMapping("/{orderId}/reviews")
    public ResponseEntity<OrderReviewResponse> createReview(
            @RequestHeader("X-USER-ID") Long userId,
            @PathVariable Long orderId,
            @RequestBody OrderReviewRequest request)
    {
        return ResponseEntity.ok(orderService.createReview(userId, orderId, request));
    }

    @GetMapping("/{orderId}/reviews")
    public ResponseEntity<List<OrderReviewResponse>> getReviews(
            @RequestHeader("X-USER-ID") Long userId,
            @PathVariable Long orderId)
    {
        return ResponseEntity.ok(orderService.getReviews(userId, orderId));
    }
}
