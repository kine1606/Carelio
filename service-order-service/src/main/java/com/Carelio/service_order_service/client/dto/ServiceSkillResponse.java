package com.Carelio.service_order_service.client.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceSkillResponse
{
    private Long id;
    private com.Carelio.service_order_service.client.dto.ServiceSkillCode serviceSkillCode;
}
