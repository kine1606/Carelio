package com.Carelio.service_order_service.controller;

import com.Carelio.service_order_service.dto.request.OrderAttachmentRequest;
import com.Carelio.service_order_service.dto.response.OrderAttachmentResponse;
import com.Carelio.service_order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/service-orders")
@RequiredArgsConstructor
public class OrderAttachmentController
{
    private final OrderService orderService;

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
}
