package com.Carelio.household_service.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateHouseRequest
{
    @NotNull(message = "Address line is required")
    private String addressLine;
}
