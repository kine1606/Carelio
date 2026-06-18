package com.Carelio.service_order_service.client.dto;

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
