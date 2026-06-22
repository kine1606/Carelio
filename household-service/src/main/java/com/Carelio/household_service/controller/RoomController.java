package com.Carelio.household_service.controller;

import com.Carelio.household_service.dto.request.CreateRoomRequest;
import com.Carelio.household_service.dto.request.UpdateRoomRequest;
import com.Carelio.household_service.dto.response.RoomResponse;
import com.Carelio.household_service.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
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
//    @PreAuthorize("hasRole('CUSTOMER')")
    public List<RoomResponse> getAll(
            @AuthenticationPrincipal Jwt jwt
    )
    {
        String userId = jwt.getSubject();
        return roomService.getAll(userId);
    }

    // GET /api/rooms/{id}
    @GetMapping("/{id}")
//    @PreAuthorize("hasRole('CUSTOMER')")
    public RoomResponse getById(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long id)
    {
        String userId = jwt.getSubject();
        return roomService.getById(userId, id);
    }

    // POST /api/rooms
    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public RoomResponse createRoom(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody @Valid CreateRoomRequest request)
    {
        String userId = jwt.getSubject();
        return roomService.createRoom(userId, request);
    }

    // PATCH /api/rooms/{id}
    @PatchMapping("/{roomId}")
    public RoomResponse updateRoom(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long roomId,
            @RequestBody @Valid UpdateRoomRequest request)
    {
        String userId = jwt.getSubject();
        return roomService.updateRoom(userId, roomId, request);
    }

//     DELETE /api/rooms/{id}
    @DeleteMapping("/{id}")
    public RoomResponse delete(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long id)
    {
        String userId = jwt.getSubject();
        return roomService.softDelete(userId, id);
    }
}