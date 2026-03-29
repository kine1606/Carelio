package com.amigoscode.carelio.room.service;

import com.amigoscode.carelio.room.dto.CreateRoomRequest;
import com.amigoscode.carelio.room.dto.RoomResponse;
import com.amigoscode.carelio.room.dto.UpdateRoomRequest;
import com.amigoscode.carelio.room.mapper.RoomMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.amigoscode.carelio.room.entity.Room;
import com.amigoscode.carelio.room.repository.RoomRepository;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService
{

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    public List<Room> getAll()
    {
        return roomRepository.findAllByDeletedFalse();
    }

    public Room getById(Long id)
    {
        return roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));
    }

    public Room create(CreateRoomRequest request)
    {
        Room room = roomMapper.toEntity(request);
        room.setCreatedAt(LocalDateTime.now());
        return roomRepository.save(room);
    }

    public Room softDelete(Long id)
    {
        Room room = getById(id);
        if(!room.getEquipments().isEmpty())
        {
            throw new RuntimeException("Cannot delete this room because it has equipments");
        }
        room.setDeleted(true);
        return roomRepository.save(room);
    }

    public Room update(Long id, UpdateRoomRequest request)
    {
        Room room = getById(id);
        room.setName(request.getName());
        room.setDescription(request.getDescription());
        room.setFloor(request.getFloor());

        room.setUpdatedAt(LocalDateTime.now());
        return roomRepository.save(room);
    }
}