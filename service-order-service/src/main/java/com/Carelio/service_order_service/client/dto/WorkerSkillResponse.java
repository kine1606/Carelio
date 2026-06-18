package com.Carelio.service_order_service.client.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class WorkerSkillResponse
{
    private Long id;
    private WorkerProfileResponse workerProfileResponse;
    private ServiceSkillResponse serviceSkillResponse;
    private Long equipmentCategoryId;
    private String equipmentCategoryName;
    private Integer yearExperience;
}