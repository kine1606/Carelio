package com.Carelio.worker_service.dto.response;

import com.Carelio.worker_service.entity.WorkerStatus;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonPropertyOrder({
        "id",
        "userId",
        "totalJobs",
        "ratingAvg",
        "bio",
        "status"
})
public class WorkerProfileResponse
{
    private Long id;
    private Long userId;
    private Integer totalJobs;
    private Double ratingAvg;
    private String bio;
    private WorkerStatus status;
}
