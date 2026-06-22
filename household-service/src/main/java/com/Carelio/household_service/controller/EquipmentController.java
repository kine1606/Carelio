package com.Carelio.household_service.controller;

import com.Carelio.household_service.dto.request.CreateEquipmentRequest;
import com.Carelio.household_service.dto.request.UpdateEquipmentRequest;
import com.Carelio.household_service.dto.response.EquipmentResponse;
import com.Carelio.household_service.dto.response.EquipmentValidationResponse;
import com.Carelio.household_service.service.EquipmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/equipments")
@PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
public class EquipmentController
{
    private final EquipmentService equipmentService;

    // GET /api/equipments
    @GetMapping
    public List<EquipmentResponse> getEquipments(
            @AuthenticationPrincipal Jwt jwt
    )
    {
        String userId = jwt.getSubject();
        return equipmentService.getAll(userId);
    }

    // GET /api/equipments/{equipmentId}
    @GetMapping("/{equipmentId}")
    public EquipmentResponse getEquipmentById(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long equipmentId
    )
    {
        String userId = jwt.getSubject();
        return equipmentService.getById(userId, equipmentId);
    }

    // POST /api/equipments
    @PostMapping
    public EquipmentResponse createEquipment(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody @Valid CreateEquipmentRequest res)
    {
        String userId = jwt.getSubject();
        return equipmentService.createEquipment(userId, res);
    }

    @PatchMapping("/{id}")
    public EquipmentResponse updateEquipment(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long id,
            @RequestBody @Valid UpdateEquipmentRequest res)
    {
        String userId = jwt.getSubject();
        return equipmentService.updateEquipment(userId, id, res);
    }

    @GetMapping("/{id}/validate")
    public EquipmentValidationResponse validateEquipment(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long id,
            @RequestParam Long roomId,
            @RequestParam Long houseId
    )
    {
        String userId = jwt.getSubject();
        return equipmentService.validateEquipment(userId, id, roomId, houseId);
    }

    @DeleteMapping("/{id}")
    public void delete(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long id
    )
    {
        String userId = jwt.getSubject();
        equipmentService.deleteEquipment(userId, id);
    }
}

