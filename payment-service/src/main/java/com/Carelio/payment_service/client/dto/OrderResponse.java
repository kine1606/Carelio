package com.Carelio.payment_service.client.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {

    private Long id;
    private String userId;
    private Long workerId;
    private String title;
    private ServiceOrderStatus status;
    private String description;
    private BigDecimal price;
}
