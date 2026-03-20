package com.amigoscode.carelio.equipment.controller;

import com.amigoscode.carelio.equipment.entity.Equipment;
import com.amigoscode.carelio.equipment.service.EquipmentService;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/equipments")
public class EquipmentController
{
    private final EquipmentService equipmentService;

    public EquipmentController(EquipmentService equipmentService)
    {
        this.equipmentService = equipmentService;
    }

    @GetMapping
    public List<Equipment> getEquipments()
    {
        return equipmentService.getAll();
    }

    @PostMapping
    public Equipment create(@RequestBody Equipment equipment)
    {
        return equipmentService.createEquipment(equipment);
    }
}

