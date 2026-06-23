package com.Carelio.service_order_service.controller;

import com.Carelio.service_order_service.dto.response.OrderResponse;
import com.Carelio.service_order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/internal/service-orders")
@RequiredArgsConstructor
@Slf4j
public class InternalOrderController
{

    private final OrderService orderService;

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderForInternalUse(@PathVariable Long orderId)
    {
        return ResponseEntity.ok(orderService.getByIdInternal(orderId));
    }

    @PatchMapping("/{orderId}/accept")
    public ResponseEntity<Void> acceptOrder(
            @PathVariable Long orderId,
            @RequestParam Long workerId)
    {
        log.info("Worker {} đang gọi Feign để NHẬN đơn hàng {}", workerId, orderId);
        orderService.processAcceptOrder(orderId, workerId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{orderId}/start")
    public ResponseEntity<Void> startOrder(
            @PathVariable Long orderId,
            @RequestParam Long workerId)
    {
        log.info("Worker {} đang gọi Feign để BẮT ĐẦU đơn hàng {}", workerId, orderId);
        orderService.processStartOrder(orderId, workerId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{orderId}/complete")
    public ResponseEntity<Void> completeOrder(
            @PathVariable Long orderId,
            @RequestParam Long workerId)
    {
        log.info("Worker {} đang gọi Feign để HOÀN THÀNH đơn hàng {}", workerId, orderId);
        orderService.processCompleteOrder(orderId, workerId);
        return ResponseEntity.ok().build();
    }
}