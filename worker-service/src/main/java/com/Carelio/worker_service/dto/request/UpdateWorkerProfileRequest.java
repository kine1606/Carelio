package com.Carelio.worker_service.dto.request;

import com.Carelio.worker_service.entity.WorkerStatus;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateWorkerProfileRequest
{
    @Size(max = 500)
    private String bio;

    private WorkerStatus status;
}