package com.amigoscode.carelio.equipment.dto;

import com.amigoscode.carelio.base.BaseResponse;
import com.amigoscode.carelio.equipment.entity.EquipmentConditionStatus;
import com.amigoscode.carelio.equipment.entity.EquipmentStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentSimpleResponse extends BaseResponse
{
    private String name;
    private EquipmentStatus status;
    private EquipmentConditionStatus conditionStatus;
}