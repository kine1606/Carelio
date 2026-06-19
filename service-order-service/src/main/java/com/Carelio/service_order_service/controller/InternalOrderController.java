package com.Carelio.service_order_service.controller;

import com.Carelio.service_order_service.dto.response.OrderResponse;
import com.Carelio.service_order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/internal/service-orders")
@RequiredArgsConstructor
public class InternalOrderController {

    private final OrderService orderService;
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderForInternalUse(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getById(orderId));
    }
}