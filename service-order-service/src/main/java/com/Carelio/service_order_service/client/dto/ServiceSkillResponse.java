package com.Carelio.service_order_service.client.dto;

import com.Carelio.worker_service.entity.ServiceSkillCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
