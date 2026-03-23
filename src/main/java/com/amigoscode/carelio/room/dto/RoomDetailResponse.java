package com.amigoscode.carelio.room.dto;

import com.amigoscode.carelio.base.BaseResponse;
import com.amigoscode.carelio.equipment.dto.EquipmentResponse;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomDetailResponse extends BaseResponse {
    private String name;
    private String description;
    private Integer floor;
    private List<EquipmentResponse> equipments;
}