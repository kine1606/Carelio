package com.Carelio.household_service.controller;

import com.Carelio.household_service.dto.response.EquipmentResponse;
import com.Carelio.household_service.service.EquipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomEquipmentController
{

    private final EquipmentService equipmentService;

    @GetMapping("/{roomId}/equipments")
    public List<EquipmentResponse> getEquipmentsByRoom(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long roomId
    )
    {
        String userId = jwt.getSubject();
        return equipmentService.getEquipmentsByRoom(userId, roomId);
    }
}