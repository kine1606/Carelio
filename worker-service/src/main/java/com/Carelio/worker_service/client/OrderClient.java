package com.Carelio.worker_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "order-client", contextId = "workerServiceOrderClient" , url = "${order.service.url}")
public interface OrderClient
{
    @PatchMapping("/api/service-orders/{orderId}/accept")
    void acceptOrder(@PathVariable("orderId") Long orderId);

    @PatchMapping("/api/service-orders/{orderId}/complete")
    void completeOrder(@PathVariable("orderId") Long orderId);

    @PatchMapping("/api/service-orders/{orderId}/start")
    void startOrder(@PathVariable("orderId") Long orderId);
}
