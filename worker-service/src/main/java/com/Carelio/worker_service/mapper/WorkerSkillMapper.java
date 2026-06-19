package com.Carelio.worker_service.mapper;

import com.Carelio.worker_service.dto.request.WorkerSkillRequest;
import com.Carelio.worker_service.dto.response.ServiceSkillResponse;
import com.Carelio.worker_service.dto.response.WorkerProfileResponse;
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

    @Mapping(target = "workerProfileResponse", source = "workerProfile", qualifiedByName = "toWorkerProfile")
    @Mapping(target = "serviceSkillResponse", source = "serviceSkill", qualifiedByName = "toServiceSkillRes")
    @Mapping(target = "equipmentCategoryName", source = "equipmentCategoryName")
    WorkerSkillResponse toResponse(WorkerSkill workerSkill);

    List<WorkerSkillResponse> toResponseList(List<WorkerSkill> skills);

    @Named("toWorkerProfile")
    default WorkerProfileResponse toWorkerProfile(WorkerProfile profile) {
        if (profile == null) return null;
        return WorkerProfileResponse.builder()
                .id(profile.getId())
                .userId(profile.getUserId())
                .status(profile.getStatus())
                .bio(profile.getBio())
                .ratingAvg(profile.getRatingAvg())
                .totalJobs(profile.getTotalJobs())
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
