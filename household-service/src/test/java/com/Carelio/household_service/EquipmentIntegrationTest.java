package com.Carelio.household_service;

import com.Carelio.household_service.entity.*;
import com.Carelio.household_service.repository.EquipmentCategoryRepository;
import com.Carelio.household_service.repository.EquipmentRepository;
import com.Carelio.household_service.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
class EquipmentIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private EquipmentCategoryRepository equipmentCategoryRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Room livingRoom;
    private Room bedroom;
    private EquipmentCategory airConditionerCategory;
    private EquipmentCategory televisionCategory;

    @BeforeEach
    void setUp() {
        equipmentRepository.deleteAll();
        equipmentCategoryRepository.deleteAll();
        roomRepository.deleteAll();

        livingRoom = Room.builder()
                .ownerId(1L)
                .name("Phòng khách")
                .floor(1)
                .description("Phòng chính")
                .deleted(false)
                .build();

        bedroom = Room.builder()
                .ownerId(1L)
                .name("Phòng ngủ")
                .floor(2)
                .description("Phòng ngủ chính")
                .deleted(false)
                .build();

        roomRepository.saveAll(List.of(livingRoom, bedroom));

        airConditionerCategory = EquipmentCategory.builder()
                .name("Máy lạnh")
                .deleted(false)
                .build();

        televisionCategory = EquipmentCategory.builder()
                .name("Tivi")
                .deleted(false)
                .build();

        equipmentCategoryRepository.saveAll(List.of(
                airConditionerCategory,
                televisionCategory
        ));
    }

    @Test
    void createEquipment_shouldCreateEquipmentWithOwnerIdRoomAndCategory() throws Exception {
        String requestBody = """
                {
                    "name": "Máy lạnh phòng khách",
                    "brand": "Daikin",
                    "model": "FTKC35UAVMV",
                    "serialNumber": "DAIKIN-LR-001",
                    "roomId": %d,
                    "equipmentCategoryId": %d
                }
                """.formatted(livingRoom.getId(), airConditionerCategory.getId());

        mockMvc.perform(post("/api/equipments")
                        .header("X-USER-ID", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ownerId", is(1)))
                .andExpect(jsonPath("$.name", is("Máy lạnh phòng khách")))
                .andExpect(jsonPath("$.brand", is("Daikin")))
                .andExpect(jsonPath("$.model", is("FTKC35UAVMV")))
                .andExpect(jsonPath("$.serialNumber", is("DAIKIN-LR-001")));
    }

    @Test
    void getAllEquipment_shouldReturnOnlyEquipmentOfCurrentOwner() throws Exception {
        Equipment equipment1 = Equipment.builder()
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
                .build();

        Equipment equipment2 = Equipment.builder()
                .ownerId(1L)
                .name("Tivi phòng ngủ")
                .brand("Samsung")
                .model("UA55CU8000")
                .serialNumber("SAMSUNG-TV-001")
                .status(EquipmentStatus.ACTIVE)
                .conditionStatus(EquipmentConditionStatus.NORMAL)
                .room(bedroom)
                .equipmentCategory(televisionCategory)
                .deleted(false)
                .build();

        Equipment equipmentOfOtherUser = Equipment.builder()
                .ownerId(2L)
                .name("Thiết bị user khác")
                .brand("Sony")
                .model("X80L")
                .serialNumber("SONY-USER2-001")
                .status(EquipmentStatus.ACTIVE)
                .conditionStatus(EquipmentConditionStatus.NORMAL)
                .room(livingRoom)
                .equipmentCategory(televisionCategory)
                .deleted(false)
                .build();

        Equipment deletedEquipment = Equipment.builder()
                .ownerId(1L)
                .name("Thiết bị đã xóa")
                .brand("LG")
                .model("OLD")
                .serialNumber("DELETED-001")
                .status(EquipmentStatus.ACTIVE)
                .conditionStatus(EquipmentConditionStatus.NORMAL)
                .room(livingRoom)
                .equipmentCategory(televisionCategory)
                .deleted(true)
                .build();

        equipmentRepository.saveAll(List.of(
                equipment1,
                equipment2,
                equipmentOfOtherUser,
                deletedEquipment
        ));

        mockMvc.perform(get("/api/equipments")
                        .header("X-USER-ID", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].ownerId", is(1)))
                .andExpect(jsonPath("$[1].ownerId", is(1)));
    }
}