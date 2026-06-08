package com.Carelio.household_service.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class CreateEquipmentRequest
{
    @NotNull(message = "Room id is required")
    private Long roomId;

    @NotNull(message = "Equipment category id is required")
    private Long equipmentCategoryId;

    @NotBlank(message = "Name is required")
    private String name;

    private String brand;
    private String model;
    private String serialNumber;
}