package com.Carelio.household_service.service;


import com.Carelio.household_service.dto.request.CreateRoomRequest;
import com.Carelio.household_service.dto.request.UpdateRoomRequest;
import com.Carelio.household_service.dto.response.RoomResponse;
import com.Carelio.household_service.entity.House;
import com.Carelio.household_service.entity.Room;
import com.Carelio.household_service.mapper.RoomMapper;
import com.Carelio.household_service.repository.HouseRepository;
import com.Carelio.household_service.repository.RoomRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class RoomService
{

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    private final HouseRepository houseRepository;
    public List<RoomResponse> getAll(String userId)
    {
        log.info("Get rooms by ownerId = {}", userId);
//        List<Room> rooms = roomRepository.findByOwnerIdAndDeletedFalse(ownerId);
        List<Room> rooms = roomRepository.findByHouse_UserId(userId);
        log.info("Found {} rooms", rooms.size());
        return roomMapper.toResponseList(rooms);
    }

    public RoomResponse getById(String userId, Long roomId)
    {
        Room room = roomRepository.findByIdAndHouse_UserId(roomId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Room not found with id: " +  roomId));
        return roomMapper.toResponse(room);
    }

    @Transactional
    public RoomResponse createRoom(String userId, CreateRoomRequest request)
    {
        House house = houseRepository.findByIdAndUserId(request.getHouseId(), userId)
                .orElseThrow(() -> new EntityNotFoundException("House not found with id: " +  request.getHouseId()));
        Room room = roomMapper.toEntity(request);
        room.setHouse(house);
        Room savedRoom = roomRepository.save(room);
        log.info("Room id {} created", savedRoom.getId());
        return roomMapper.toResponse(savedRoom);
    }

    @Transactional
    public RoomResponse updateRoom(String userId, Long roomId, UpdateRoomRequest req)
    {
        Room room = roomRepository.findByIdAndHouse_UserId(roomId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Room not found with id: " +  roomId));

        if (req.getName() != null) room.setName(req.getName());
        if (req.getDescription() != null) room.setDescription(req.getDescription());
        if (req.getFloor() != null) room.setFloor(req.getFloor());

        Room savedRoom = roomRepository.save(room);
        log.info("Room id {} updated", savedRoom.getId());

        return roomMapper.toResponse(savedRoom);
    }

    @Transactional
    public RoomResponse softDelete(String ownerId, Long id)
    {
        Room room = roomRepository.findByIdAndHouse_UserId(id, ownerId)
                .orElseThrow(() -> new EntityNotFoundException("Room not found with id: " +  id));
        if(!room.getEquipments().isEmpty())
        {
            throw new RuntimeException("Cannot delete this room because it has equipments");
        }
        room.setDeleted(true);
        Room savedRoom = roomRepository.save(room);
        log.info("Room id {} deleted", savedRoom.getId());
        return roomMapper.toResponse(savedRoom);
    }

}