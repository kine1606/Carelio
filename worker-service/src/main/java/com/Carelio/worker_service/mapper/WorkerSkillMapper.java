package com.Carelio.worker_service.mapper;

import com.Carelio.worker_service.dto.request.WorkerSkillRequest;
import com.Carelio.worker_service.dto.response.WorkerSkillResponse;
import com.Carelio.worker_service.entity.WorkerSkill;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WorkerSkillMapper
{
    WorkerSkill toEntity(WorkerSkillRequest workerSkillRequest);

    WorkerSkillResponse toResponse(WorkerSkill workerSkill);

    List<WorkerSkillResponse> toResponseList(List<WorkerSkill> skills);
}
