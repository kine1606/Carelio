package com.Carelio.worker_service.client;

import com.Carelio.worker_service.client.dto.OrderResponse;
import com.Carelio.worker_service.config.FeignClientConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "service-order-service",
        contextId = "workerServiceOrderClient",
        url = "${order.service.url}",
        fallbackFactory = OrderClient.OrderClientFallbackFactory.class,
        configuration = FeignClientConfig.class
)
public interface OrderClient {

    @PatchMapping("/api/internal/service-orders/{orderId}/accept")
    void acceptOrder(@PathVariable("orderId") Long orderId,
                     @RequestParam("workerId") Long workerId);

    @PatchMapping("/api/internal/service-orders/{orderId}/start")
    void startOrder(@PathVariable("orderId") Long orderId,
                    @RequestParam("workerId") Long workerId);

    @PatchMapping("/api/internal/service-orders/{orderId}/complete")
    void completeOrder(@PathVariable("orderId") Long orderId,
                       @RequestParam("workerId") Long workerId);

    @GetMapping("/api/internal/service-orders/{orderId}")
    OrderResponse getOrder(@PathVariable("orderId") Long orderId);

    // =========================================================================
    // 🛡️ CLASS FALLBACK FACTORY ĐỒNG BỘ 100% VỚI ORDER INTERFACE
    // =========================================================================
    @Component
    @Slf4j
    class OrderClientFallbackFactory implements org.springframework.cloud.openfeign.FallbackFactory<OrderClient> {
        @Override
        public OrderClient create(Throwable cause) {
            return new OrderClient() {

                @Override
                public void acceptOrder(Long orderId, Long workerId) {
                    log.error("🚨 Circuit Breaker [acceptOrder] kích hoạt! Thợ {} không thể nhận đơn {} vì lỗi: {}",
                            workerId, orderId, cause.getMessage());
                }

                @Override
                public void startOrder(Long orderId, Long workerId) {
                    log.error("🚨 Circuit Breaker [startOrder] kích hoạt! Thợ {} không thể bắt đầu đơn {} vì lỗi: {}",
                            workerId, orderId, cause.getMessage());
                }

                @Override
                public void completeOrder(Long orderId, Long workerId) {
                    log.error("🚨 Circuit Breaker [completeOrder] kích hoạt! Thợ {} không thể hoàn thành đơn {} vì lỗi: {}",
                            workerId, orderId, cause.getMessage());
                }

                @Override
                public OrderResponse getOrder(Long orderId) {
                    log.error("🚨 Circuit Breaker [getOrder] kích hoạt! Không lấy được thông tin đơn {} vì lỗi: {}",
                            orderId, cause.getMessage());

                    OrderResponse fallback = new OrderResponse();
                    fallback.setOrderId(orderId);
                    return fallback;
                }
            };
        }
    }
}