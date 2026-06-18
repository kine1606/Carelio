package com.Carelio.service_order_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderRequest
{
    private String title;
    private String description;
    private LocalDateTime scheduledTime;
}