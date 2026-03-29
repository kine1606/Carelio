package com.amigoscode.carelio.equipment.controller;

import com.amigoscode.carelio.equipment.dto.request.CreateEquipmentSimpleRequest;
import com.amigoscode.carelio.equipment.dto.response.EquipmentResponse;
import com.amigoscode.carelio.equipment.dto.request.UpdateEquipmentRequest;
import com.amigoscode.carelio.equipment.mapper.EquipmentMapper;
import com.amigoscode.carelio.equipment.service.EquipmentService;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/equipment")
public class EquipmentController
{
    private final EquipmentService equipmentService;
    private final EquipmentMapper equipmentMapper;

    public EquipmentController(EquipmentService equipmentService, EquipmentMapper equipmentMapper)
    {
        this.equipmentService = equipmentService;
        this.equipmentMapper = equipmentMapper;
    }

    @GetMapping
    public List<EquipmentResponse> getEquipments()
    {
        return equipmentMapper.toResponseList(equipmentService.getAll());
    }

    @GetMapping("/{id}")
    public EquipmentResponse getEquipmentById(@PathVariable Long id)
    {
        return equipmentMapper.toResponse(equipmentService.getById(id));
    }
    @PostMapping
    public EquipmentResponse create(@RequestBody CreateEquipmentSimpleRequest res)
    {
        return equipmentMapper.toResponse(equipmentService.create(res));
    }

    @PatchMapping("/{id}")
    public EquipmentResponse update(@PathVariable Long id,
                                    @RequestBody UpdateEquipmentRequest res)
    {
        return equipmentMapper.toResponse(equipmentService.update(id, res));
    }

    // not done
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id)
    {
        equipmentService.delete(id);
    }
}

