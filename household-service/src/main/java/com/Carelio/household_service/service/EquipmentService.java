package com.amigoscode.carelio.equipment.service;

import com.amigoscode.carelio.equipment.dto.request.CreateEquipmentSimpleRequest;
import com.amigoscode.carelio.equipment.dto.request.UpdateEquipmentRequest;
import com.amigoscode.carelio.equipment.entity.Equipment;
import com.amigoscode.carelio.equipment.entity.EquipmentCategory;
import com.amigoscode.carelio.equipment.mapper.EquipmentMapper;
import com.amigoscode.carelio.equipment.repository.EquipmentRepository;
import com.amigoscode.carelio.room.entity.Room;
import com.amigoscode.carelio.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EquipmentService
{
    private final EquipmentRepository equipmentRepository;
    private final EquipmentMapper equipmentMapper;
    private final RoomService roomService;
    private final EquipmentCategoryService equipmentCategoryService;

    public List<Equipment> getAll()
    {
        return equipmentRepository.findAllByDeletedFalse();
    }

    public Equipment getById(Long id)
    {
        return equipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipment not found"));
    }

    @Transactional
    public Equipment create(CreateEquipmentSimpleRequest req)
    {
        Room room = roomService.getById(req.getRoomId());
        EquipmentCategory equipmentCategory =
                equipmentCategoryService.getById(req.getEquipmentCategoryId());

        Equipment equipment = equipmentMapper.toEntity(req);
        equipment.setRoom(room);
        equipment.setEquipmentCategory(equipmentCategory);
        equipment.setCreatedAt(LocalDateTime.now());

        return equipmentRepository.save(equipment);
    }

    @Transactional
    public Equipment update(Long id, UpdateEquipmentRequest req)
    {
        Equipment e = getById(id);
        if (req.getRoomId() != null)
        {
            Room room = roomService.getById(req.getRoomId());
            e.setRoom(room);
        }
        if (req.getStatus() != null) e.setStatus(req.getStatus());
        if (req.getConditionStatus() != null) e.setConditionStatus(req.getConditionStatus());
        e.setUpdatedAt(LocalDateTime.now());
        return equipmentRepository.save(e);
    }

    @Transactional
    public void delete(Long id) {
        Equipment equipment = getById(id);
        equipment.setDeleted(true);
        equipment.setUpdatedAt(LocalDateTime.now());
    }
}
