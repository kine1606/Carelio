package com.Carelio.worker_service.dto.request;

import com.Carelio.worker_service.entity.SkillLevel;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkerSkillRequest
{
    private Long workerProfileId;
    private Long serviceSkillId;
    private Long equipmentCategoryId;

    private Integer yearExperience;
    private SkillLevel skillLevel;
}
