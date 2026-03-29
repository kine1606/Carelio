package com.amigoscode.carelio.equipment.mapper;


import com.amigoscode.carelio.equipment.dto.request.CreateEquipmentRequest;
import com.amigoscode.carelio.equipment.dto.request.CreateEquipmentSimpleRequest;
import com.amigoscode.carelio.equipment.dto.request.UpdateEquipmentRequest;
import com.amigoscode.carelio.equipment.dto.response.EquipmentResponse;
import com.amigoscode.carelio.equipment.entity.Equipment;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EquipmentMapper {

    Equipment toEntity(CreateEquipmentRequest request);
    Equipment toEntity(UpdateEquipmentRequest request);
    Equipment toEntity(CreateEquipmentSimpleRequest request);

    void updateEntity(UpdateEquipmentRequest request, @MappingTarget Equipment entity);

//    EquipmentResponse toResponse(Equipment Equipment);
    EquipmentResponse toResponse(Equipment Equipment);
    List<EquipmentResponse> toResponseList(List<Equipment> Equipments);
}