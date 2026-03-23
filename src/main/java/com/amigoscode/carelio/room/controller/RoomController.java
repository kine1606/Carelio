package com.amigoscode.carelio.room.controller;

import com.amigoscode.carelio.room.dto.CreateRoomRequest;
import com.amigoscode.carelio.room.dto.RoomDetailResponse;
import com.amigoscode.carelio.room.dto.RoomResponse;
import com.amigoscode.carelio.room.dto.UpdateRoomRequest;
import com.amigoscode.carelio.room.mapper.RoomMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.amigoscode.carelio.room.entity.Room;
import com.amigoscode.carelio.room.service.RoomService;
import java.util.List;

@RestController
@RequestMapping("/room")
public class RoomController {

    private final RoomService roomService;
    private final RoomMapper roomMapper;

    public RoomController(RoomService roomService, RoomMapper roomMapper)
    {
        this.roomService = roomService;
        this.roomMapper = roomMapper;
    }

    @GetMapping
    public List<RoomResponse> getAll() {
        List<Room> rooms = roomService.getAll();
        return roomMapper.toResponseList(rooms);
    }

    @GetMapping("/{id}")
    public RoomDetailResponse getById(@PathVariable Long id) {
        Room room = roomService.getById(id);
        return roomMapper.toDetailResponse(room);
    }

    @PostMapping
    public RoomResponse create(@RequestBody CreateRoomRequest request)
    {
        return roomMapper.toResponse(roomService.create(request));
    }

    @PatchMapping("/{id}")
    public RoomResponse update(@PathVariable Long id, @RequestBody UpdateRoomRequest request)
    {
        return roomMapper.toResponse(roomService.update(id, request));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id)
    {
        roomService.delete(id);
        return ResponseEntity.noContent().build();
    }
}