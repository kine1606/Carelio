package com.Carelio.worker_service.client;

import com.Carelio.worker_service.client.dto.OrderResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "order-client", contextId = "workerServiceOrderClient" , url = "${order.service.url}")
public interface OrderClient
{
    @PatchMapping("/api/service-orders/{orderId}/accept")
    void acceptOrder(@PathVariable("orderId") Long orderId,
                     @RequestParam("workerId") Long workerId);

    @PatchMapping("/api/service-orders/{orderId}/complete")
    void completeOrder(@PathVariable("orderId") Long orderId,
                         @RequestParam("workerId") Long workerId);

    @PatchMapping("/api/service-orders/{orderId}/start")
    void startOrder(@PathVariable("orderId") Long orderId,
                    @RequestParam("workerId") Long workerId);

    @GetMapping("/api/internal/service-orders/{orderId}")
    OrderResponse getOrder(@PathVariable("orderId") Long orderId);
}
