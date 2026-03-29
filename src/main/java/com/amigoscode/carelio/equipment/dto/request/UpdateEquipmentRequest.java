package com.amigoscode.carelio.equipment.dto.request;

import com.amigoscode.carelio.equipment.entity.EquipmentConditionStatus;
import com.amigoscode.carelio.equipment.entity.EquipmentStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateEquipmentRequest
{
    private EquipmentStatus status;
    private EquipmentConditionStatus conditionStatus;
    private Long roomId;
}