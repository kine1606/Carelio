package com.amigoscode.carelio.equipment.service;

import com.amigoscode.carelio.equipment.dto.CreateEquipmentSimpleRequest;
import com.amigoscode.carelio.equipment.dto.UpdateEquipmentRequest;
import com.amigoscode.carelio.equipment.entity.Equipment;
import com.amigoscode.carelio.equipment.mapper.EquipmentMapper;
import com.amigoscode.carelio.equipment.repository.EquipmentRepository;
import com.amigoscode.carelio.room.entity.Room;
import com.amigoscode.carelio.room.repository.RoomRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class EquipmentService
{
    private final EquipmentRepository equipmentRepository;
    private final EquipmentMapper equipmentMapper;
    private final RoomRepository roomRepository;
    public EquipmentService(EquipmentRepository equipmentRepository, EquipmentMapper equipmentMapper, RoomRepository roomRepository)
    {
        this.equipmentRepository = equipmentRepository;
        this.equipmentMapper = equipmentMapper;
        this.roomRepository = roomRepository;
    }

    public List<Equipment> getAll()
    {
        return equipmentRepository.findAll();
    }

    public Equipment getById(@PathVariable Long id)
    {
        return equipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipment not found"));
    }

    public Equipment create(CreateEquipmentSimpleRequest res)
    {
        Room room = roomRepository.findById(res.getRoomId())
                        .orElseThrow(() -> new RuntimeException("Room Not Found"));
        Equipment equipment = equipmentMapper.toEntity(res);
        equipment.setRoom(room);
        return equipmentRepository.save(equipment);
    }

    // not done
    public void delete(@PathVariable Long id)
    {
        Equipment equipment = getById(id);
        equipmentRepository.delete(equipment);
    }
    // not done
    public Equipment update(Long id, UpdateEquipmentRequest res)
    {
        Equipment e = getById(id);
        e.setName(res.getName());
        return equipmentRepository.save(e);
    }
}
