package com.Carelio.household_service.service;


import com.Carelio.household_service.dto.request.CreateEquipmentRequest;
import com.Carelio.household_service.dto.request.UpdateEquipmentRequest;
import com.Carelio.household_service.dto.response.EquipmentResponse;
import com.Carelio.household_service.dto.response.EquipmentValidationResponse;
import com.Carelio.household_service.entity.Equipment;
import com.Carelio.household_service.entity.EquipmentCategory;
import com.Carelio.household_service.entity.House;
import com.Carelio.household_service.entity.Room;
import com.Carelio.household_service.mapper.EquipmentMapper;
import com.Carelio.household_service.repository.EquipmentCategoryRepository;
import com.Carelio.household_service.repository.EquipmentRepository;
import com.Carelio.household_service.repository.HouseRepository;
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
    private final EquipmentCategoryRepository equipmentCategoryRepository;
    private final HouseRepository houseRepository;

    public List<EquipmentResponse> getAll(Long userId)
    {
        List<Equipment> equipments = equipmentRepository.findAllByDeletedFalseAndRoom_House_UserId(userId);
        log.info("found {} equipment with userId: {}", equipments.size(), userId);
        return equipmentMapper.toResponseList(equipments);
    }

    public EquipmentResponse getById(Long userId, Long id)
    {
        Equipment e = equipmentRepository.findByIdAndRoom_House_UserId(id, userId)
                .orElseThrow(() -> new EntityNotFoundException("Equipment not found with id:" + id));
        log.info("Equipment found with id:{}", id);
        return equipmentMapper.toResponse(e);
    }

    public List<EquipmentResponse> getEquipmentsByRoom(Long userId, Long roomId)
    {
        Room room = roomRepository
                .findByIdAndHouse_UserId(roomId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Room not found"));

        List<Equipment> equipmentList =
                equipmentRepository.findByRoom_Id(room.getId());
        log.info("found {} equipment with roomId: {}", equipmentList.size(), roomId);
        return equipmentMapper.toResponseList(equipmentList);
    }

    @Transactional
    public EquipmentResponse createEquipment(Long userId, CreateEquipmentRequest req)
    {
        Room room = roomRepository.findByIdAndHouse_UserId(req.getRoomId(), userId)
                .orElseThrow(() -> new EntityNotFoundException("Room not found or not owned by user"));

        EquipmentCategory category = equipmentCategoryRepository.findById(req.getEquipmentCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        Equipment equipment = equipmentMapper.toEntity(req);
        equipment.setRoom(room);
        equipment.setEquipmentCategory(category);
        Equipment savedEquipment = equipmentRepository.save(equipment);
        log.info("Equipment {} is created successfully", savedEquipment.getId());
        return equipmentMapper.toResponse(savedEquipment);
    }

    @Transactional
    public EquipmentResponse updateEquipment(Long userId, Long equipmentId, UpdateEquipmentRequest req)
    {
        Equipment e = equipmentRepository.findByIdAndRoom_House_UserId(equipmentId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Equipment not found with id:" + equipmentId));
        if (req.getRoomId() != null) {
            Room room = roomRepository.findByIdAndHouse_UserId(req.getRoomId(), userId)
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

    public EquipmentValidationResponse validateEquipment(Long userId,
                                                         Long equipmentId,
                                                         Long roomId,
                                                         Long houseId)
    {
        House house = houseRepository.findByIdAndUserId(houseId, userId)
                .orElseThrow(() -> new EntityNotFoundException("House " + houseId + " not found or not owned by user"));
        Room room = roomRepository.findByIdAndHouse_Id(roomId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Room " + roomId + " not found or not belonged to house " + houseId));
        Equipment equipment = equipmentRepository.findByIdAndRoom_Id(equipmentId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Equipment " + equipmentId + " not found or not belonged to room " + roomId));

        return EquipmentValidationResponse
                .builder()
                .houseAddressLine(house.getAddressLine())
                .roomName(room.getName())
                .equipmentSerialNumber(equipment.getSerialNumber())
                .equipmentBrand(equipment.getBrand())
                .equipmentCategoryName(equipment.getEquipmentCategory().getName())
                .build();
    }
}
