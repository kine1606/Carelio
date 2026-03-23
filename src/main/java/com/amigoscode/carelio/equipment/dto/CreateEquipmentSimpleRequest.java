package com.amigoscode.carelio.equipment.dto;

import com.amigoscode.carelio.equipment.entity.EquipmentConditionStatus;
import com.amigoscode.carelio.equipment.entity.EquipmentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateEquipmentSimpleRequest
{
    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Status is required")
    private EquipmentStatus status;

    @NotNull(message = "Condition status is required")
    private EquipmentConditionStatus conditionStatus;

    @NotNull(message = "Room id is required")
    private Long roomId;

//    @NotNull(message = "Equipment category id is required")
//    private Long equipmentCategoryId;
}