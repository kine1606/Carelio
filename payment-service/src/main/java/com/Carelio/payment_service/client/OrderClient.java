package com.Carelio.payment_service.client;

import com.Carelio.payment_service.client.dto.OrderResponse;
import com.Carelio.payment_service.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "service-order-service",
        configuration = FeignClientConfig.class,
contextId = "paymentServiceOrderClient",

        url = "${order.service.url}")
public interface OrderClient {

    // Gọi đúng endpoint nội bộ lấy thông tin đơn hàng chúng ta vừa mở ở câu trước
    @GetMapping("/api/internal/service-orders/{orderId}")
    OrderResponse getOrderById(@PathVariable("orderId") Long orderId);

    // Endpoint nội bộ báo cho Order Service biết là đơn này đã trả tiền xong xuôi
    @PatchMapping("/api/internal/service-orders/{orderId}/payment-success")
    OrderResponse markOrderSuccess(@PathVariable("orderId") Long orderId);

    // Endpoint nội bộ báo hủy/thất bại do lỗi thanh toán
    @PatchMapping("/api/internal/service-orders/{orderId}/payment-failed")
    OrderResponse markOrderFailed(@PathVariable("orderId") Long orderId);

}