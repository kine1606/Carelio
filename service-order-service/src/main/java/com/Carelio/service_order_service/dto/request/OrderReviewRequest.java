package com.Carelio.service_order_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderReviewRequest
{
    private Long orderId;
    private Long userId;
    private Long workerId;
    private Integer rating;
    private String comment;
}
