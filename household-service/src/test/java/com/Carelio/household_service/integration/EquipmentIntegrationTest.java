package com.Carelio.household_service;

import com.Carelio.household_service.dto.request.CreateEquipmentRequest;
import com.Carelio.household_service.entity.*;
import com.Carelio.household_service.repository.EquipmentCategoryRepository;
import com.Carelio.household_service.repository.EquipmentRepository;
import com.Carelio.household_service.repository.HouseRepository;
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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class EquipmentIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private EquipmentCategoryRepository equipmentCategoryRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private String userId;
    private String otherUserId;
    private House userHouse;
    private Room livingRoom;
    private Room bedroom;
    private EquipmentCategory airConditionerCategory;
    private EquipmentCategory televisionCategory;

    @BeforeEach
    void setUp() {
        // Xóa sạch theo thứ tự quan hệ ràng buộc khóa ngoại (Rất quan trọng trong Integration Test)
        equipmentRepository.deleteAll();
        roomRepository.deleteAll();
        houseRepository.deleteAll();
        equipmentCategoryRepository.deleteAll();

        userId = "user-customer-uuid-111";
        otherUserId = "user-other-uuid-222";

        // Dựng dữ liệu phân tầng: House -> Room -> Equipment chuẩn bài bản
        userHouse = House.builder()
                .userId(userId)
                .addressLine("123 Đường ABC, Quận 5")
                .build();
        houseRepository.save(userHouse);

        livingRoom = Room.builder()
                .name("Phòng khách")
                .house(userHouse)
                .build();

        bedroom = Room.builder()
                .name("Phòng ngủ")
                .house(userHouse)
                .build();

        roomRepository.saveAll(List.of(livingRoom, bedroom));

        airConditionerCategory = EquipmentCategory.builder()
                .name("Máy lạnh")
                .build();

        televisionCategory = EquipmentCategory.builder()
                .name("Tivi")
                .build();

        equipmentCategoryRepository.saveAll(List.of(airConditionerCategory, televisionCategory));
    }

    @Test
    void createEquipment_shouldCreateEquipmentSuccessfully() throws Exception {
        CreateEquipmentRequest request = CreateEquipmentRequest.builder()
                .name("Máy lạnh phòng khách")
                .brand("Daikin")
                .model("FTKC35UAVMV")
                .serialNumber("DAIKIN-LR-001")
                .roomId(livingRoom.getId())
                .equipmentCategoryId(airConditionerCategory.getId())
                .build();

        // Giả lập cuộc gọi HTTP POST thực tế đi qua API và lưu thẳng vào DB Postgres của Docker Testcontainers
        mockMvc.perform(post("/api/equipments")
                        .param("userId", userId) // Truyền userId phù hợp với cấu hình Controller của bạn
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Máy lạnh phòng khách")))
                .andExpect(jsonPath("$.brand", is("Daikin")))
                .andExpect(jsonPath("$.model", is("FTKC35UAVMV")))
                .andExpect(jsonPath("$.serialNumber", is("DAIKIN-LR-001")));
    }

    @Test
    void getAllEquipment_shouldReturnOnlyActiveEquipmentOfCurrentOwner() throws Exception {
        // 1. Thiết bị hợp lệ của User hiện tại
        Equipment equipment1 = Equipment.builder()
                .name("Máy lạnh phòng khách")
                .brand("Daikin")
                .model("FTKC35UAVMV")
                .serialNumber("DAIKIN-LR-001")
                .status(EquipmentStatus.ACTIVE)
                .conditionStatus(EquipmentConditionStatus.NORMAL)
                .room(livingRoom)
                .equipmentCategory(airConditionerCategory)
                .isDeleted(false)
                .build();

        Equipment equipment2 = Equipment.builder()
                .name("Tivi phòng ngủ")
                .brand("Samsung")
                .model("UA55CU8000")
                .serialNumber("SAMSUNG-TV-001")
                .status(EquipmentStatus.ACTIVE)
                .conditionStatus(EquipmentConditionStatus.NORMAL)
                .room(bedroom)
                .equipmentCategory(televisionCategory)
                .isDeleted(false)
                .build();

        // 2. Tạo một nhà riêng biệt thuộc về User khác để test bộ lọc
        House otherHouse = House.builder().userId(otherUserId).addressLine("456 Lê Hồng Phong").build();
        houseRepository.save(otherHouse);
        Room otherRoom = Room.builder().name("Phòng user khác").house(otherHouse).build();
        roomRepository.save(otherRoom);

        Equipment equipmentOfOtherUser = Equipment.builder()
                .name("Thiết bị user khác")
                .brand("Sony")
                .model("X80L")
                .serialNumber("SONY-USER2-001")
                .status(EquipmentStatus.ACTIVE)
                .conditionStatus(EquipmentConditionStatus.NORMAL)
                .room(otherRoom)
                .equipmentCategory(televisionCategory)
                .isDeleted(false)
                .build();

        // 3. Thiết bị của User hiện tại nhưng đã bị Xóa mềm (isDeleted = true)
        Equipment deletedEquipment = Equipment.builder()
                .name("Thiết bị đã xóa")
                .brand("LG")
                .model("OLD")
                .serialNumber("DELETED-001")
                .status(EquipmentStatus.ACTIVE)
                .conditionStatus(EquipmentConditionStatus.NORMAL)
                .room(livingRoom)
                .equipmentCategory(televisionCategory)
                .isDeleted(true)
                .build();

        equipmentRepository.saveAll(List.of(
                equipment1,
                equipment2,
                equipmentOfOtherUser,
                deletedEquipment
        ));

        // Kiểm tra xem API đầu ra có lọc chính xác: Chỉ lấy 2 món, bỏ món của người khác và món đã xóa mềm
        mockMvc.perform(get("/api/equipments")
                        .param("userId", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Máy lạnh phòng khách")))
                .andExpect(jsonPath("$[1].name", is("Tivi phòng ngủ")));
    }
}