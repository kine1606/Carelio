package com.Carelio.household_service.controller;

import com.Carelio.household_service.dto.request.CreateEquipmentRequest;
import com.Carelio.household_service.dto.request.UpdateEquipmentRequest;
import com.Carelio.household_service.dto.response.EquipmentResponse;
import com.Carelio.household_service.dto.response.EquipmentValidationResponse;
import com.Carelio.household_service.service.EquipmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/equipments")
public class EquipmentController
{
    private final EquipmentService equipmentService;
    // GET /api/equipments
    @GetMapping
    public List<EquipmentResponse> getEquipments(
            @RequestHeader("X-USER-ID") Long userId
    )
    {
        return equipmentService.getAll(userId);
    }

    // GET /api/equipments/{equipmentId}
    @GetMapping("/{equipmentId}")
    public EquipmentResponse getEquipmentById(
            @RequestHeader("X-USER-ID") Long userId,
            @PathVariable Long equipmentId)
    {
        return equipmentService.getById(userId, equipmentId);
    }

    // POST /api/equipments
    @PostMapping
    public EquipmentResponse createEquipment(
            @RequestHeader("X-USER-ID") Long userId,
            @RequestBody @Valid CreateEquipmentRequest res)
    {
        return equipmentService.createEquipment(userId, res);
    }

    @PatchMapping("/{id}")
    public EquipmentResponse updateEquipment(
            @RequestHeader("X-USER-ID") Long userId,
            @PathVariable Long id,
            @RequestBody @Valid UpdateEquipmentRequest res)
    {
        return equipmentService.updateEquipment(userId, id, res);
    }

    @GetMapping("/{id}/validate")
    public EquipmentValidationResponse validateEquipment(
            @RequestHeader("X-USER-ID") Long userId,
            @PathVariable Long id,
            @RequestParam Long roomId,
            @RequestParam Long houseId
    )
    {
        return equipmentService.validateEquipment(userId, id, roomId, houseId);
    }

    // not done
//    @DeleteMapping("/{id}")
//    public void delete(@PathVariable Long id)
//    {
//        equipmentService.delete(id);
//    }
}

