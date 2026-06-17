package com.Carelio.household_service.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HouseResponse
{
    private Long id;
    private Long ownerId;
    private String addressLine;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
