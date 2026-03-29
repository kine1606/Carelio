package com.amigoscode.carelio.room.dto;

import com.amigoscode.carelio.base.BaseResponse;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

@JsonPropertyOrder({
        "id",
        "name",
        "description",
        "floor",
        "createdAt",
        "updatedAt"
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponse extends BaseResponse {
    private String name;
    private String description;
    private Integer floor;
}