package com.Carelio.worker_service.mapper;

import com.Carelio.worker_service.dto.request.WorkerSkillRequest;
import com.Carelio.worker_service.dto.response.ServiceSkillResponse;
import com.Carelio.worker_service.dto.response.WorkerProfileSummaryResponse;
import com.Carelio.worker_service.dto.response.WorkerSkillResponse;
import com.Carelio.worker_service.entity.ServiceSkill;
import com.Carelio.worker_service.entity.WorkerProfile;
import com.Carelio.worker_service.entity.WorkerSkill;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WorkerSkillMapper
{
    WorkerSkill toEntity(WorkerSkillRequest workerSkillRequest);

    @Mapping(target = "workerProfileResponse", source = "workerProfile", qualifiedByName = "toWorkerProfileSummary")
    @Mapping(target = "serviceSkillResponse", source = "serviceSkill", qualifiedByName = "toServiceSkillRes")
    WorkerSkillResponse toResponse(WorkerSkill workerSkill);

    List<WorkerSkillResponse> toResponseList(List<WorkerSkill> skills);

    @Named("toWorkerProfileSummary")
    default WorkerProfileSummaryResponse toWorkerProfileSummary(WorkerProfile profile) {
        if (profile == null) return null;
        return WorkerProfileSummaryResponse.builder()
                .id(profile.getId())
                .userId(profile.getUserId())
                .bio(profile.getBio())
                .build();
    }

    @Named("toServiceSkillRes")
    default ServiceSkillResponse toServiceSkillRes(ServiceSkill skill) {
        if (skill == null) return null;
        return ServiceSkillResponse.builder()
                .id(skill.getId())
                .serviceSkillCode(skill.getServiceSkillCode())
                .build();
    }
}
