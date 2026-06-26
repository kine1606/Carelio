package com.Carelio.notification_service.listener;

import com.Carelio.notification_service.controller.NotificationController;
import com.Carelio.notification_service.event.OrderAcceptedEvent;
import com.Carelio.notification_service.event.OrderPlacedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Component
@Slf4j
public class OrderNotificationListener {

    private static final String TOPIC = "order-events-topic";
    private static final String GROUP = "carelio-notification-group";

    @KafkaListener(topics = TOPIC, groupId = GROUP, properties = "spring.json.value.default.type=com.Carelio.notification_service.event.OrderPlacedEvent")
    public void handleOrderPlaced(OrderPlacedEvent event) {
        log.info("📩 [Kafka] Nhận được OrderPlacedEvent cho đơn hàng: {}", event.getOrderId());

        String customerId = event.getCustomerId();
        String msg = String.format("Đơn hàng #%d của bạn đã được đăng tải thành công với trạng thái %s.",
                event.getOrderId(), event.getStatus());

        sendRealtimeMessage(customerId, "ORDER_CREATED", msg);
    }

    @KafkaListener(topics = TOPIC, groupId = GROUP, properties = "spring.json.value.default.type=com.Carelio.notification_service.event.OrderAcceptedEvent")
    public void handleOrderAccepted(OrderAcceptedEvent event) {
        log.info("📩 [Kafka] Nhận được OrderAcceptedEvent. Thợ {} nhận đơn: {}", event.getWorkerId(), event.getOrderId());

        String customerId = event.getCustomerId();
        String msg = String.format("Tin vui! Đơn hàng #%d đã được kỹ thuật viên (ID: %d) chấp nhận sửa chữa.",
                event.getOrderId(), event.getWorkerId());

        sendRealtimeMessage(customerId, "ORDER_ACCEPTED", msg);
    }

    private void sendRealtimeMessage(String userId, String eventName, String data) {
        SseEmitter emitter = NotificationController.emitters.get(userId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name(eventName).data(data));
                log.info("⚡ [SSE] Đã đẩy tin nhắn realtime thành công đến user: {}", userId);
            } catch (IOException e) {
                log.warn("❌ [SSE] Lỗi đường truyền, gỡ bỏ kết nối của user: {}", userId);
                NotificationController.emitters.remove(userId);
            }
        } else {
            log.info("💤 User {} hiện đang offline. Tin nhắn sẽ được lưu vào lịch sử (nếu cấu hình thêm DB).", userId);
        }
    }
}