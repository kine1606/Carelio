package com.amigoscode.carelio.equipment.dto.response;

import com.amigoscode.carelio.base.BaseResponse;
import com.amigoscode.carelio.equipment.entity.EquipmentConditionStatus;
import com.amigoscode.carelio.equipment.entity.EquipmentStatus;
import com.amigoscode.carelio.room.dto.RoomSummaryResponse;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

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
public class EquipmentResponse extends BaseResponse {

    private String name;
    private String brand;
    private String model;
    private String serialNumber;

    private EquipmentStatus status;
    private EquipmentConditionStatus conditionStatus;

    private RoomSummaryResponse room;

    private EquipmentCategorySummaryResponse equipmentCategory;
}