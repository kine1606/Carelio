package com.Carelio.household_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateRoomRequest
{
    @NotBlank(message = "Name is required")
    private String name;

    private Integer floor;
    private String description;


}