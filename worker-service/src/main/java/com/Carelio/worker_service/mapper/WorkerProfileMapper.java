package com.Carelio.worker_service.mapper;

import com.Carelio.worker_service.dto.request.WorkerProfileRequest;
import com.Carelio.worker_service.dto.response.WorkerProfileResponse;
import com.Carelio.worker_service.entity.WorkerProfile;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WorkerProfileMapper
{
    WorkerProfileResponse toResponse(WorkerProfile workerProfile);
    WorkerProfile toEntity(WorkerProfileRequest req);

    List<WorkerProfileResponse> toResponseList(List<WorkerProfile> workerProfileList);
}
