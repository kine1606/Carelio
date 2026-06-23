package com.Carelio.worker_service.dto.request;

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
}