package com.Carelio.household_service.service;


import com.Carelio.household_service.dto.request.CreateRoomRequest;
import com.Carelio.household_service.dto.request.UpdateRoomRequest;
import com.Carelio.household_service.dto.response.RoomResponse;
import com.Carelio.household_service.entity.Room;
import com.Carelio.household_service.mapper.RoomMapper;
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

    public List<RoomResponse> getAll(Long ownerId)
    {
        log.info("Get rooms by ownerId = {}", ownerId);
//        List<Room> rooms = roomRepository.findByOwnerIdAndDeletedFalse(ownerId);
        List<Room> rooms = roomRepository.findByOwnerId(ownerId);

        log.info("Found {} rooms", rooms.size());

        return roomMapper.toResponseList(rooms);
    }

    public RoomResponse getById(Long ownerId, Long roomId)
    {
        Room room = roomRepository.findByIdAndOwnerId(roomId, ownerId)
                .orElseThrow(() -> new EntityNotFoundException("Room not found with id: " +  roomId));
        return roomMapper.toResponse(room);
    }

    @Transactional
    public RoomResponse createRoom(Long ownerId, CreateRoomRequest request)
    {
        Room room = roomMapper.toEntity(request);
        room.setOwnerId(ownerId);
        Room savedRoom = roomRepository.save(room);
        log.info(savedRoom.getOwnerId() + " " + room.getOwnerId());
        log.info("Room id {} created", savedRoom.getId());
        return roomMapper.toResponse(savedRoom);
    }

    @Transactional
    public RoomResponse updateRoom(Long ownerId, Long roomId, UpdateRoomRequest req)
    {
        Room room = roomRepository.findByIdAndOwnerId(roomId, ownerId)
                .orElseThrow(() -> new EntityNotFoundException("Room not found with id: " +  roomId));

        if (req.getName() != null) room.setName(req.getName());
        if (req.getDescription() != null) room.setDescription(req.getDescription());
        if (req.getFloor() != null) room.setFloor(req.getFloor());

        Room savedRoom = roomRepository.save(room);
        log.info("Room id {} updated", savedRoom.getId());

        return roomMapper.toResponse(savedRoom);
    }

    @Transactional
    public RoomResponse softDelete(Long ownerId, Long id)
    {
        Room room = roomRepository.findByIdAndOwnerId(id, ownerId)
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