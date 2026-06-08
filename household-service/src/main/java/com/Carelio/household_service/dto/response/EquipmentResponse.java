package com.Carelio.household_service.dto.response;

import com.Carelio.household_service.base.BaseResponse;
import com.Carelio.household_service.entity.EquipmentConditionStatus;
import com.Carelio.household_service.entity.EquipmentStatus;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({
        "id",
        "name",
        "brand",
        "model",
        "serialNumber",
        "status",
        "conditionStatus",
        "room",
        "equipmentCategory",
})
public class EquipmentResponse extends BaseResponse
{

    private String name;
    private String brand;
    private String model;
    private String serialNumber;

    private EquipmentStatus status;
    private EquipmentConditionStatus conditionStatus;

//    private RoomSummaryResponse room;

    private CategoryResponse equipmentCategory;
}