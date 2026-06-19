package com.Carelio.service_order_service.controller;

import com.Carelio.service_order_service.dto.request.OrderRequest;
import com.Carelio.service_order_service.dto.request.UpdateOrderRequest;
import com.Carelio.service_order_service.dto.response.OrderResponse;
import com.Carelio.service_order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/service-orders")
@RequiredArgsConstructor
@Slf4j
public class ServiceOrderController
{
    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders(@RequestHeader("X-USER-ID") Long userId)
    {
        return ResponseEntity.ok(orderService.getAll(userId));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(
            @RequestHeader("X-USER-ID") Long userId,
            @PathVariable Long orderId)
    {
        return ResponseEntity.ok(orderService.getById(userId, orderId));
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

    //===================================INTERNAL USE ========================================

    @PatchMapping("/{orderId}/accept")
    public ResponseEntity<OrderResponse> acceptOrder(@PathVariable Long orderId)
    {
        return ResponseEntity.ok(orderService.processAcceptOrder(orderId));
    }

    @PatchMapping("/{orderId}/start")
    public ResponseEntity<OrderResponse> startOrder(@PathVariable Long orderId)
    {
        return ResponseEntity.ok(orderService.processStartOrder(orderId));
    }
    @PatchMapping("/{orderId}/complete")
    public ResponseEntity<OrderResponse> completeOrder(@PathVariable Long orderId)
    {
        return ResponseEntity.ok(orderService.processCompleteOrder(orderId));
    }
}
