package com.Carelio.household_service.mapper;

import com.Carelio.household_service.dto.request.CreateEquipmentRequest;
import com.Carelio.household_service.dto.response.EquipmentResponse;
import com.Carelio.household_service.entity.Equipment;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EquipmentMapper
{
    Equipment toEntity(CreateEquipmentRequest request);
    EquipmentResponse toResponse(Equipment Equipment);
    List<EquipmentResponse> toResponseList(List<Equipment> Equipments);
}