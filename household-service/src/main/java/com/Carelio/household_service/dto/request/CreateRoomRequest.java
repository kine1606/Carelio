package com.Carelio.household_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "houseId is not null")
    private Long houseId;
    private Integer floor;
    private String description;


}