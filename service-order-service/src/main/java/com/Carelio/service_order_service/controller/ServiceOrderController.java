package com.Carelio.service_order_service.controller;

import com.Carelio.service_order_service.dto.request.OrderRequest;
import com.Carelio.service_order_service.dto.request.UpdateOrderRequest;
import com.Carelio.service_order_service.dto.response.OrderResponse;
import com.Carelio.service_order_service.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ServiceOrderController {

    private final OrderService orderService;

    @GetMapping("/api/service-orders")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    public ResponseEntity<List<OrderResponse>> getAllOrders(@AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject(); // Bóc UUID của Khách hàng từ Token
        return ResponseEntity.ok(orderService.getAll(userId)); // Nhớ sửa tầng Service nhận vào String userId nhé
    }

    @GetMapping("/api/service-orders/{orderId}")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    public ResponseEntity<OrderResponse> getOrderById(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long orderId) {
        String userId = jwt.getSubject();
        return ResponseEntity.ok(orderService.getById(userId, orderId));
    }

    @PostMapping("/api/service-orders")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<OrderResponse> createOrder(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody @Valid OrderRequest orderRequest) {
        String userId = jwt.getSubject();
        return ResponseEntity.ok(orderService.createOrder(userId, orderRequest));
    }

    @PatchMapping("/api/service-orders/{orderId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<OrderResponse> updateOrder(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long orderId,
            @RequestBody @Valid UpdateOrderRequest update) {
        String userId = jwt.getSubject();
        return ResponseEntity.ok(orderService.updateOrder(userId, orderId, update));
    }

    @DeleteMapping("/api/service-orders/{orderId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<OrderResponse> deleteOrder(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long orderId) {
        String userId = jwt.getSubject();
        return ResponseEntity.ok(orderService.deleteOrder(userId, orderId));
    }
}