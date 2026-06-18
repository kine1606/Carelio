package com.Carelio.household_service.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EquipmentValidationResponse
{
    private String houseAddressLine;
    private String roomName;
    private String equipmentSerialNumber;
    private String equipmentBrand;
    private String equipmentCategoryName;
}
