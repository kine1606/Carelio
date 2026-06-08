package com.Carelio.household_service.controller;

import com.Carelio.household_service.dto.response.EquipmentResponse;
import com.Carelio.household_service.service.EquipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomEquipmentController
{

    private final EquipmentService equipmentService;

    @GetMapping("/{roomId}/equipments")
    public List<EquipmentResponse> getEquipmentsByRoom(
            @RequestHeader("X-USER-ID") Long userId,
            @PathVariable Long roomId
    )
    {
        return equipmentService.getEquipmentsByRoom(userId, roomId);
    }
}