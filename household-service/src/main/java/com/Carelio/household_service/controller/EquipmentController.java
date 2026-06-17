package com.Carelio.household_service.controller;

import com.Carelio.household_service.dto.request.CreateEquipmentRequest;
import com.Carelio.household_service.dto.request.UpdateEquipmentRequest;
import com.Carelio.household_service.dto.response.EquipmentResponse;
import com.Carelio.household_service.mapper.EquipmentMapper;
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
    private final EquipmentMapper equipmentMapper;

    // GET /api/equipments
    @GetMapping
    public List<EquipmentResponse> getEquipments(
            @RequestHeader("X-USER-ID") Long ownerId
    )
    {
        return equipmentService.getAll(ownerId);
    }

    // GET /api/equipments/{equipmentId}
    @GetMapping("/{equipmentId}")
    public EquipmentResponse getEquipmentById(
            @RequestHeader("X-USER-ID") Long ownerId,
            @PathVariable Long equipmentId)
    {
        return equipmentService.getById(ownerId, equipmentId);
    }

    // POST /api/equipments
    @PostMapping
    public EquipmentResponse createEquipment(
            @RequestHeader("X-USER-ID") Long ownerId,
            @RequestBody @Valid CreateEquipmentRequest res)
    {
        return equipmentService.createEquipment(ownerId, res);
    }

    @PatchMapping("/{id}")
    public EquipmentResponse updateEquipment(
            @RequestHeader("X-USER-ID") Long ownerId,
            @PathVariable Long id,
            @RequestBody @Valid UpdateEquipmentRequest res)
    {
        return equipmentService.updateEquipment(ownerId, id, res);
    }

    // not done
//    @DeleteMapping("/{id}")
//    public void delete(@PathVariable Long id)
//    {
//        equipmentService.delete(id);
//    }
}

