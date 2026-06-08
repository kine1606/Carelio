package com.Carelio.household_service.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateRoomRequest
{
    private String name;
    private Integer floor;
    private String description;

}