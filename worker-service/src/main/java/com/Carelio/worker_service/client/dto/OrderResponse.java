package com.Carelio.worker_service.client.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse
{
    private Long orderId;
    private Long equipmentCategoryId;
    private Long serviceSkillId;
}

