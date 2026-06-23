package com.Carelio.worker_service.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkerSkillRequest
{
//    private Long workerProfileId;
    @NotNull
    private Long serviceSkillId;

    @NotNull
    private Long equipmentCategoryId;
    private Integer yearExperience;
}
