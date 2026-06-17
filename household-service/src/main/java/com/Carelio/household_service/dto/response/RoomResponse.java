package com.Carelio.household_service.dto.response;

import com.Carelio.household_service.base.BaseResponse;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@JsonPropertyOrder({
        "id",
        "name",
        "floor",
        "description",
        "houseId",
        "createdAt",
        "updatedAt"
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponse extends BaseResponse
{
    private String name;
    private Integer floor;
    private String description;
    private Long houseId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}