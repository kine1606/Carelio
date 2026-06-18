package com.Carelio.service_order_service.dto.request;

import com.Carelio.service_order_service.entity.ServiceOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderRequest
{
    private Long workerId;
    private ServiceOrderStatus status;
    private String title;
    private String description;
}