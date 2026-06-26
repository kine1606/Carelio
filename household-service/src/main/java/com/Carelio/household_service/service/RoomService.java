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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        List<Room> rooms = roomRepository.findByHouse_UserId(userId);
        log.info("Found {} rooms", rooms.size());
        return roomMapper.toResponseList(rooms);
    }

    public Page<RoomResponse> getAllWithPagination(String userId, Pageable pageable)
    {
        Page<Room> rooms = roomRepository.findByHouse_UserId(userId,pageable);
        return rooms.map(roomMapper::toResponse);
    }

    @Cacheable(value = "ROOM_CACHE", key = "#roomId")
    public RoomResponse getById(String userId, Long roomId)
    {
        Room room = roomRepository.findByIdAndHouse_UserId(roomId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Room not found with id: " +  roomId));
        return roomMapper.toResponse(room);
    }

    @Transactional
    @CachePut(value = "ROOM_CACHE", key = "#result.id")
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
    @CachePut(value = "ROOM_CACHE", key = "#result.id")
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
    @CacheEvict(value = "ROOM_CACHE", key = "#roomId")
    public RoomResponse softDelete(String ownerId, Long roomId)
    {
        Room room = roomRepository.findByIdAndHouse_UserId(roomId, ownerId)
                .orElseThrow(() -> new EntityNotFoundException("Room not found with id: " +  roomId));
        if(!room.getEquipments().isEmpty())
        {
            throw new RuntimeException("Cannot delete this room because it has equipments");
        }
        room.setIsDeleted(true);
        Room savedRoom = roomRepository.save(room);
        log.info("Room id {} deleted", savedRoom.getId());
        return roomMapper.toResponse(savedRoom);
    }

}