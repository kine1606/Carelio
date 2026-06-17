package com.Carelio.household_service.mapper;


import com.Carelio.household_service.dto.request.CreateRoomRequest;
import com.Carelio.household_service.dto.response.RoomResponse;
import com.Carelio.household_service.entity.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoomMapper
{

    Room toEntity(CreateRoomRequest request);

//    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "house.id", target = "houseId")
    RoomResponse toResponse(Room room);
    List<RoomResponse> toResponseList(List<Room> rooms);
}