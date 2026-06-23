package com.Carelio.worker_service.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateWorkerSkillRequest
{
    private Long serviceSkillId;

    private Long equipmentCategoryId;

    private Integer yearExperience;
}
