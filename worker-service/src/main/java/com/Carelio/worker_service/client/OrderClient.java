package com.Carelio.worker_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "order-client", contextId = "workerServiceOrderClient" , url = "${order.service.url}")
public interface OrderClient
{
    @PutMapping("/api/service-orders/{orderId}/accept")
    void acceptOrder(@PathVariable("orderId") Long orderId);

    @PutMapping("/api/service-orders/{orderId}/complete")
    void completeOrder(@PathVariable("orderId") Long orderId);
}
