package com.amigoscode.carelio.equipment.service;

import com.amigoscode.carelio.equipment.entity.Equipment;
import com.amigoscode.carelio.equipment.entity.EquipmentCategory;
import com.amigoscode.carelio.equipment.repository.EquipmentCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EquipmentCategoryService
{
    private final EquipmentCategoryRepository equipmentCategoryRepository;

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
        ec.setUpdatedAt(LocalDateTime.now());
        return equipmentCategoryRepository.save(ec);
    }

    public void delete(Long id)
    {
        EquipmentCategory ec = getById(id);
        if (!ec.getEquipments().isEmpty()) {
            throw new RuntimeException("Equipment category has equipment(s)");
        }
        equipmentCategoryRepository.delete(ec);
    }
}
