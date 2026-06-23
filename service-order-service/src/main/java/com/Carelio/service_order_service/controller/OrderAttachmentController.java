package com.Carelio.service_order_service.controller;

import com.Carelio.service_order_service.dto.request.OrderAttachmentRequest;
import com.Carelio.service_order_service.dto.response.OrderAttachmentResponse;
import com.Carelio.service_order_service.service.OrderAttachmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/service-orders")
@RequiredArgsConstructor
public class OrderAttachmentController {

    private final OrderAttachmentService orderAttachmentService;

    // POST /api/service-orders/{orderId}/attachments
    @PostMapping("/{orderId}/attachments")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('WORKER')")
    public ResponseEntity<OrderAttachmentResponse> createAttachment(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long orderId,
            @RequestBody @Valid OrderAttachmentRequest request) {
        String userId = jwt.getSubject();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderAttachmentService.createAttachment(userId, orderId, request));
    }

    // GET /api/service-orders/{orderId}/attachments
    @GetMapping("/{orderId}/attachments")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('WORKER') or hasRole('ADMIN')")
    public ResponseEntity<List<OrderAttachmentResponse>> getAttachments(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long orderId) {
        String userId = jwt.getSubject();
        return ResponseEntity.ok(orderAttachmentService.getAttachments(userId, orderId));
    }

    // DELETE /api/service-orders/{orderId}/attachments/{attachmentId}
    @DeleteMapping("/{orderId}/attachments/{attachmentId}")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('WORKER')")
    public ResponseEntity<Void> deleteAttachment(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long orderId,
            @PathVariable Long attachmentId) {
        String userId = jwt.getSubject();
        orderAttachmentService.deleteAttachment(userId, orderId, attachmentId);
        return ResponseEntity.noContent().build();
    }
}