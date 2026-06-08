package com.Carelio.worker_service.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkerProfileRequest
{
    private Long userId;
    private String bio;
}
