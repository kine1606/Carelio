package com.Carelio.notification_service.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderAcceptedEvent
{
    private Long orderId;
    private String customerId;
    private Long workerId;
    private String status;
}