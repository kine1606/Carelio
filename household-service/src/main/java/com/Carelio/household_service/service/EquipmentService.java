package com.Carelio.household_service.service;


import com.Carelio.household_service.dto.request.CreateEquipmentRequest;
import com.Carelio.household_service.dto.request.UpdateEquipmentRequest;
import com.Carelio.household_service.dto.response.EquipmentResponse;
import com.Carelio.household_service.entity.Equipment;
import com.Carelio.household_service.entity.EquipmentCategory;
import com.Carelio.household_service.entity.Room;
import com.Carelio.household_service.mapper.EquipmentMapper;
import com.Carelio.household_service.repository.EquipmentCategoryRepository;
import com.Carelio.household_service.repository.EquipmentRepository;
import com.Carelio.household_service.repository.RoomRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EquipmentService
{
    private final EquipmentRepository equipmentRepository;
    private final RoomRepository roomRepository;
    private final EquipmentMapper equipmentMapper;
    private final EquipmentCategoryRepository  equipmentCategoryRepository;

    public List<EquipmentResponse> getAll(Long ownerId)
    {
        List<Equipment> equipments = equipmentRepository.findAllByDeletedFalseAndOwnerId(ownerId);
        return equipmentMapper.toResponseList(equipments);
    }

    public EquipmentResponse getById(Long ownerId, Long id)
    {
        Equipment e = equipmentRepository.findByIdAndOwnerId(id, ownerId)
                .orElseThrow(() -> new EntityNotFoundException("Equipment not found with id:" + id));
        return equipmentMapper.toResponse(e);
    }

    public List<EquipmentResponse> getEquipmentsByRoom(Long ownerId, Long roomId)
    {
        if(!roomRepository.existsByIdAndOwnerId(roomId, ownerId))
        {
            throw new RuntimeException("Room not found or not owned by user");
        }
        List<Equipment> equipmentList = equipmentRepository.findByRoomIdAndOwnerId(roomId, ownerId)
                .stream().toList();

        return  equipmentMapper.toResponseList(equipmentList);
    }

    @Transactional
    public EquipmentResponse createEquipment(Long ownerId, CreateEquipmentRequest req)
    {
        Room room = roomRepository.findByIdAndOwnerId(req.getRoomId(), ownerId)
                .orElseThrow(() -> new EntityNotFoundException("Room not found or not owned by user"));

        EquipmentCategory category = equipmentCategoryRepository.findById(req.getEquipmentCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        Equipment equipment = equipmentMapper.toEntity(req);
        equipment.setOwnerId(ownerId);
        equipment.setRoom(room);
        Equipment savedEquipment = equipmentRepository.save(equipment);
        log.info("Equipment {} is created successfully", savedEquipment.getId());
        return equipmentMapper.toResponse(savedEquipment);
    }

    @Transactional
    public EquipmentResponse updateEquipment(Long ownerId, Long equipmentId, UpdateEquipmentRequest req)
    {
        Equipment e = equipmentRepository.findByIdAndOwnerId(equipmentId, ownerId)
                .orElseThrow(() -> new EntityNotFoundException("Equipment not found with id:" + equipmentId));
        if (req.getRoomId() != null)
        {
            Room room = roomRepository.findByIdAndOwnerId(req.getRoomId(), ownerId)
                    .orElseThrow(() -> new EntityNotFoundException("Room not found or not owned by user"));
            e.setRoom(room);
        }
        if (req.getStatus() != null) e.setStatus(req.getStatus());
        if (req.getConditionStatus() != null) e.setConditionStatus(req.getConditionStatus());
        if (req.getName() != null) e.setName(req.getName());
        if (req.getBrand() != null) e.setBrand(req.getBrand());
        if (req.getModel() != null) e.setModel(req.getModel());
        if (req.getSerialNumber() != null) e.setSerialNumber(req.getSerialNumber());

        Equipment savedEquipment = equipmentRepository.save(e);
        log.info("Equipment {} is updated successfully", savedEquipment.getId());
        return equipmentMapper.toResponse(savedEquipment);
    }

//    @Transactional
//    public void delete(Long id) {
//        Equipment equipment = getById(id);
//        equipment.setDeleted(true);
//        equipment.setUpdatedAt(LocalDateTime.now());
//    }
}
