package com.Carelio.service_order_service.client.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkerProfileResponse
{
    private Long id;
    private Integer totalJobs;
    private Double ratingAvg;
    private String bio;
}
