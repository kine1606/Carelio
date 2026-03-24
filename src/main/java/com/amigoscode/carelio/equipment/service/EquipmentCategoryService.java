package com.amigoscode.carelio.equipment.service;

import com.amigoscode.carelio.equipment.entity.Equipment;
import com.amigoscode.carelio.equipment.entity.EquipmentCategory;
import com.amigoscode.carelio.equipment.repository.EquipmentCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipmentCategoryService
{
    private final EquipmentCategoryRepository equipmentCategoryRepository;
    public EquipmentCategoryService(EquipmentCategoryRepository equipmentCategoryRepository)
    {
        this.equipmentCategoryRepository = equipmentCategoryRepository;
    }
    public List<EquipmentCategory> getAll()
    {
        return equipmentCategoryRepository.findAll();
    }

    public EquipmentCategory getById(Long id)
    {
        return equipmentCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipment category not found"));
    }
    public EquipmentCategory create(EquipmentCategory equipmentCategory)
    {
        return equipmentCategoryRepository.save(equipmentCategory);
    }
    public EquipmentCategory update(Long id, EquipmentCategory equipmentCategory)
    {
        EquipmentCategory ec = getById(id);
        ec.setName(equipmentCategory.getName());
        return equipmentCategoryRepository.save(ec);
    }
    public void delete(Long id)
    {
        EquipmentCategory ec = getById(id);
        if(!ec.getEquipments().isEmpty())
        {
            throw new RuntimeException("Equipment category has equipment(s)");
        }
        equipmentCategoryRepository.delete(ec);
    }
}
