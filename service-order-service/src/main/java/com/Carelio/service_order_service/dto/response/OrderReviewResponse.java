package com.Carelio.service_order_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderReviewResponse
{
    private Long id;
    private Long orderId;
    private Long userId;
    private Long workerId;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
}
