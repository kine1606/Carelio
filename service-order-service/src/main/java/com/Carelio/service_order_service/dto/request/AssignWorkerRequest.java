package com.Carelio.service_order_service.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AssignWorkerRequest
{
    @NotNull(message = "Worker id is required")
    private Long workerId;
}
