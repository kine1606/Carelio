package com.amigoscode.carelio.room.dto;

import com.amigoscode.carelio.base.BaseResponse;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponse extends BaseResponse {

    private String name;
    private String description;
    private Integer floor;
}