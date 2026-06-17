package com.Carelio.household_service.mapper;

import com.Carelio.household_service.dto.request.CreateHouseRequest;
import com.Carelio.household_service.dto.response.HouseResponse;
import com.Carelio.household_service.entity.House;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HouseMapper
{
    House toEntity(CreateHouseRequest request);
    HouseResponse toResponse(House entity);
    List<HouseResponse> toResponseList(List<House> houses);
}
