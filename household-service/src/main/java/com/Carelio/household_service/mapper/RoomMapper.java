package com.amigoscode.carelio.room.mapper;

import com.amigoscode.carelio.room.dto.*;
import com.amigoscode.carelio.room.entity.Room;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    Room toEntity(CreateRoomRequest request);

    void updateEntity(UpdateRoomRequest request, @MappingTarget Room entity);

    RoomResponse toResponse(Room room);

    RoomDetailResponse toDetailResponse(Room room);
    List<RoomResponse> toResponseList(List<Room> rooms);
}