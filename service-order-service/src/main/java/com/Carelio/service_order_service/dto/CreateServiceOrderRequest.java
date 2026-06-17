package com.amigoscode.carelio.serviceOrder.dto;


import com.amigoscode.carelio.serviceOrder.entity.ServiceType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateServiceOrderRequest
{
    private String title;
    private String description;

    @NotNull(message = "service types is required")
    private Set<ServiceType> serviceTypes;

    @NotNull(message = "user address information id is required")
    private Long userAddressInformationId;

    @NotNull(message = "equipment id is required")
    private Long equipmentId;
}
