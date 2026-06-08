package com.amigoscode.carelio.equipment.controller;

import com.amigoscode.carelio.equipment.entity.EquipmentCategory;
import com.amigoscode.carelio.equipment.service.EquipmentCategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/equipment-category")
@RequestMapping
public class EquipmentCategoryController
{
    private final EquipmentCategoryService equipmentCategoryService;

    public EquipmentCategoryController(EquipmentCategoryService equipmentCategoryService)
    {
        this.equipmentCategoryService = equipmentCategoryService;
    }
    @GetMapping
    public List<EquipmentCategory> getAll()
    {
        return equipmentCategoryService.getAll();
    }

    @GetMapping("/{id}")
    public EquipmentCategory getById(@PathVariable Long id)
    {
        return equipmentCategoryService.getById(id);
    }

    @PostMapping
    public EquipmentCategory create(EquipmentCategory equipmentCategory)
    {
        return equipmentCategoryService.create(equipmentCategory);
    }

    @PatchMapping("/{id}")
    public EquipmentCategory update(@PathVariable Long id,EquipmentCategory equipmentCategory)
    {
        return equipmentCategoryService.update(id, equipmentCategory);
    }

    // not that good.
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id)
    {
        equipmentCategoryService.delete(id);
    }
}
