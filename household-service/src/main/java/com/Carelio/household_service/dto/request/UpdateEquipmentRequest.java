package com.Carelio.household_service.dto.request;


import com.Carelio.household_service.entity.EquipmentConditionStatus;
import com.Carelio.household_service.entity.EquipmentStatus;
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

    private String name;
    private String brand;
    private String model;
    private String serialNumber;
}