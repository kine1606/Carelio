package com.Carelio.worker_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkerSkillResponse
{

    private Long id;
    private WorkerProfileSummaryResponse workerProfileResponse;
    private ServiceSkillResponse serviceSkillResponse;

    // private EquipmentCategoryResponse
    private Long equipmentCategoryId;

    private Integer yearExperience;
}
