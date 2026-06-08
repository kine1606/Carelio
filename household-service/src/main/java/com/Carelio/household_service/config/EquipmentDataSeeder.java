package com.Carelio.household_service.config;

import com.Carelio.household_service.entity.Equipment;
import com.Carelio.household_service.entity.EquipmentCategory;
import com.Carelio.household_service.entity.EquipmentConditionStatus;
import com.Carelio.household_service.entity.EquipmentStatus;
import com.Carelio.household_service.entity.Room;
import com.Carelio.household_service.repository.EquipmentCategoryRepository;
import com.Carelio.household_service.repository.EquipmentRepository;
import com.Carelio.household_service.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@Order(3)
public class EquipmentDataSeeder implements CommandLineRunner {

    private final EquipmentRepository equipmentRepository;
    private final RoomRepository roomRepository;
    private final EquipmentCategoryRepository equipmentCategoryRepository;

    @Override
    public void run(String... args) {
        if (equipmentRepository.count() > 0) {
            return;
        }

        Room livingRoom = roomRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Room id 1 not found"));

        Room bedroom = roomRepository.findById(2L)
                .orElseThrow(() -> new RuntimeException("Room id 2 not found"));

        Room kitchen = roomRepository.findById(3L)
                .orElseThrow(() -> new RuntimeException("Room id 3 not found"));

        EquipmentCategory airConditionerCategory = equipmentCategoryRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("EquipmentCategory id 1 not found"));

        EquipmentCategory televisionCategory = equipmentCategoryRepository.findById(2L)
                .orElseThrow(() -> new RuntimeException("EquipmentCategory id 2 not found"));

        EquipmentCategory refrigeratorCategory = equipmentCategoryRepository.findById(3L)
                .orElseThrow(() -> new RuntimeException("EquipmentCategory id 3 not found"));

        EquipmentCategory washingMachineCategory = equipmentCategoryRepository.findById(4L)
                .orElseThrow(() -> new RuntimeException("EquipmentCategory id 4 not found"));

        List<Equipment> equipmentList = List.of(
                Equipment.builder()
                        .ownerId(1L)
                        .name("Máy lạnh phòng khách")
                        .brand("Daikin")
                        .model("FTKC35UAVMV")
                        .serialNumber("DAIKIN-LR-001")
                        .status(EquipmentStatus.ACTIVE)
                        .conditionStatus(EquipmentConditionStatus.NORMAL)
                        .room(livingRoom)
                        .equipmentCategory(airConditionerCategory)
                        .deleted(false)
                        .build(),

                Equipment.builder()
                        .ownerId(1L)
                        .name("Tivi phòng khách")
                        .brand("Samsung")
                        .model("UA55CU8000")
                        .serialNumber("SAMSUNG-TV-001")
                        .status(EquipmentStatus.ACTIVE)
                        .conditionStatus(EquipmentConditionStatus.NORMAL)
                        .room(livingRoom)
                        .equipmentCategory(televisionCategory)
                        .deleted(false)
                        .build(),

                Equipment.builder()
                        .ownerId(1L)
                        .name("Tủ lạnh bếp")
                        .brand("Panasonic")
                        .model("NR-BV360QSVN")
                        .serialNumber("PANASONIC-RF-001")
                        .status(EquipmentStatus.ACTIVE)
                        .conditionStatus(EquipmentConditionStatus.NEEDS_CLEANING)
                        .room(kitchen)
                        .equipmentCategory(refrigeratorCategory)
                        .deleted(false)
                        .build(),

                Equipment.builder()
                        .ownerId(1L)
                        .name("Máy giặt")
                        .brand("LG")
                        .model("FV1411S3B")
                        .serialNumber("LG-WM-001")
                        .status(EquipmentStatus.ACTIVE)
                        .conditionStatus(EquipmentConditionStatus.NORMAL)
                        .room(kitchen)
                        .equipmentCategory(washingMachineCategory)
                        .deleted(false)
                        .build(),

                Equipment.builder()
                        .ownerId(1L)
                        .name("Máy lạnh phòng ngủ")
                        .brand("Panasonic")
                        .model("CU/CS-XU9ZKH-8")
                        .serialNumber("PANASONIC-AC-001")
                        .status(EquipmentStatus.ACTIVE)
                        .conditionStatus(EquipmentConditionStatus.NEEDS_REPAIR)
                        .room(bedroom)
                        .equipmentCategory(airConditionerCategory)
                        .deleted(false)
                        .build(),

                Equipment.builder()
                        .ownerId(2L)
                        .name("Tivi user khác")
                        .brand("Sony")
                        .model("KD-55X80L")
                        .serialNumber("SONY-TV-USER2-001")
                        .status(EquipmentStatus.ACTIVE)
                        .conditionStatus(EquipmentConditionStatus.NORMAL)
                        .room(livingRoom)
                        .equipmentCategory(televisionCategory)
                        .deleted(false)
                        .build()
        );

        equipmentRepository.saveAll(equipmentList);
    }
}