package com.Carelio.worker_service.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class WorkerProfileSummaryResponse
{
    private Long id;
    private Long userId;
}
