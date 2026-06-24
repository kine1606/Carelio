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

    @PatchMapping("/{orderId}/payment-success")
    public ResponseEntity<OrderResponse> paymentSuccess(@PathVariable Long orderId) {
        log.info("Mạng nội bộ: Nhận lệnh gạt trạng thái PAID cho đơn hàng số {}", orderId);

        return ResponseEntity.ok(orderService.processMarkOrderAsPaid(orderId));
    }

    // Nhận thông báo trả tiền thất bại
    @PatchMapping("/{orderId}/payment-failed")
    public ResponseEntity<Void> paymentFailed(@PathVariable Long orderId) {
        log.info("Mạng nội bộ: Đơn hàng {} bị hủy giao dịch do thanh toán lỗi", orderId);
        orderService.processMarkOrderAsFailed(orderId);
        return ResponseEntity.ok().build();
    }
}