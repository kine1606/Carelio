package com.Carelio.service_order_service.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EquipmentResponse
{
    private Long id;
    private String name;
    private String serialNumber;
    private String brand;
    private String model;
}
