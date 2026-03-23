package com.amigoscode.carelio.equipment.dto;

import com.amigoscode.carelio.base.BaseEntity;
import com.amigoscode.carelio.base.BaseResponse;
import com.amigoscode.carelio.equipment.entity.Equipment;
import com.amigoscode.carelio.equipment.entity.EquipmentConditionStatus;
import com.amigoscode.carelio.equipment.entity.EquipmentStatus;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EquipmentResponse extends BaseResponse {

    private String name;
    private String brand;
    private String model;
    private String serialNumber;

    private EquipmentStatus status;
    private EquipmentConditionStatus conditionStatus;

    private Long roomId;
    private String roomName;

    private Long equipmentCategoryId;
    private String equipmentCategoryName;
}