package com.Carelio.household_service.controller;

import com.Carelio.household_service.dto.request.CreateRoomRequest;
import com.Carelio.household_service.dto.request.UpdateRoomRequest;
import com.Carelio.household_service.dto.response.RoomResponse;
import com.Carelio.household_service.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController
{

    private final RoomService roomService;

    // GET /api/rooms
    @GetMapping
    public List<RoomResponse> getAll(
            @RequestHeader("X-USER-ID") Long ownerId)
    {
        return roomService.getAll(ownerId);
    }

    // GET /api/rooms/{id}
    @GetMapping("/{id}")
    public RoomResponse getById(
            @RequestHeader("X-USER-ID") Long ownerId,
            @PathVariable Long id)
    {
        return roomService.getById(ownerId, id);
    }

    // POST /api/rooms
    @PostMapping
    public RoomResponse createRoom(
            @RequestHeader("X-USER-ID") Long ownerId,
            @RequestBody @Valid CreateRoomRequest request)
    {
        return roomService.createRoom(ownerId, request);
    }

    // PATCH /api/rooms/{id}
    @PatchMapping("/{roomId}")
    public RoomResponse updateRoom(
            @RequestHeader("X-USER-ID") Long ownerId,
            @PathVariable Long roomId,
            @RequestBody @Valid UpdateRoomRequest request)
    {
        return roomService.updateRoom(ownerId, roomId, request);
    }

//     DELETE /api/rooms/{id}
    @DeleteMapping("/{id}")
    public RoomResponse delete(
            @RequestHeader("X-USER-ID") Long ownerId,
            @PathVariable Long id)
    {
        return roomService.softDelete(ownerId, id);
    }
}