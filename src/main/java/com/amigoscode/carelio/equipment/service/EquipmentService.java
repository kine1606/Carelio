package com.amigoscode.carelio.equipment.service;

import com.amigoscode.carelio.equipment.entity.Equipment;
import com.amigoscode.carelio.equipment.repository.EquipmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class EquipmentService
{
    private final EquipmentRepository equipmentRepository;

    public EquipmentService(EquipmentRepository equipmentRepository)
    {
        this.equipmentRepository = equipmentRepository;
    }

    public List<Equipment> getAll()
    {
        return equipmentRepository.findAll();
    }

    public Equipment createEquipment(Equipment equipment)
    {
        return equipmentRepository.save(equipment);
    }
}
