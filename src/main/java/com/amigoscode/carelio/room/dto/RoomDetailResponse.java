package com.amigoscode.carelio.room.dto;

import com.amigoscode.carelio.base.BaseResponse;
import com.amigoscode.carelio.equipment.dto.response.EquipmentSummaryResponse;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.util.List;

@JsonPropertyOrder({
        "id",
        "name",
        "description",
        "floor",
        "equipments",
        "createdAt",
        "updatedAt"
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomDetailResponse extends BaseResponse {
    private String name;
    private String description;
    private Integer floor;

    private List<EquipmentSummaryResponse> equipments;
}