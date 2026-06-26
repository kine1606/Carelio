package com.Carelio.household_service.integration;

import com.Carelio.household_service.dto.request.CreateHouseRequest;
import com.Carelio.household_service.dto.request.UpdateHouseRequest;
import com.Carelio.household_service.entity.House;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class HouseIntegrationTest {

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
    private ObjectMapper objectMapper;

    private String userId;
    private String otherUserId;

    @BeforeEach
    void setUp() {
        // Làm sạch Database theo đúng thứ tự quan hệ ràng buộc để tránh dính lỗi ngoại lệ khóa ngoại (Foreign Key)
        equipmentRepository.deleteAll();
        roomRepository.deleteAll();
        houseRepository.deleteAll();

        userId = "user-customer-uuid-999";
        otherUserId = "user-other-uuid-777";
    }

    @Test
    void createHouse_shouldCreateHouseSuccessfully() throws Exception {
        CreateHouseRequest request = CreateHouseRequest.builder()
                .addressLine("123 Đường ABC, Quận 5")
                .build();

        // Giả lập cuộc gọi HTTP POST thực tế, đẩy JSON vào API Controller và lưu xuống DB Postgres trong Container
        mockMvc.perform(post("/api/houses")
                        .param("userId", userId) // Gửi kèm tham số userId khớp với cách Controller của bạn bóc tách từ Token/Header
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.addressLine", is("123 Đường ABC, Quận 5")));
    }

    @Test
    void getHouseById_shouldReturnHouse_whenOwnedByUser() throws Exception {
        House house = House.builder()
                .userId(userId)
                .addressLine("456 Lê Hồng Phong, Quận 5")
                .isDeleted(false)
                .build();
        houseRepository.save(house);

        mockMvc.perform(get("/api/houses/" + house.getId())
                        .param("userId", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.addressLine", is("456 Lê Hồng Phong, Quận 5")));
    }

    @Test
    void updateHouse_shouldModifyAddress_whenOwnedByUser() throws Exception {
        House existingHouse = House.builder()
                .userId(userId)
                .addressLine("Địa chỉ cũ")
                .isDeleted(false)
                .build();
        houseRepository.save(existingHouse);

        UpdateHouseRequest updatedRequest = UpdateHouseRequest.builder()
                .addressLine("Địa chỉ mới sau khi sửa")
                .build();

        // Sử dụng patch hoặc put tùy thuộc vào cấu hình định tuyến Router thực tế trong Controller của bạn
        mockMvc.perform(patch("/api/houses/" + existingHouse.getId())
                        .param("userId", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.addressLine", is("Địa chỉ mới sau khi sửa")));
    }

    @Test
    void deleteHouse_shouldSoftDeleteHouseInDatabase() throws Exception {
        House house = House.builder()
                .userId(userId)
                .addressLine("Ngôi nhà sắp bị xóa mềm")
                .isDeleted(false)
                .build();
        houseRepository.save(house);

        mockMvc.perform(delete("/api/houses/" + house.getId())
                        .param("userId", userId))
                .andExpect(status().isOk());

        // Truy vấn trực tiếp vào DB Postgres của Docker Testcontainers để kiểm tra cờ xóa mềm isDeleted đã chuyển thành true chưa
        House deletedHouse = houseRepository.findById(house.getId()).orElseThrow();
        assertTrue(deletedHouse.isDeleted(), "Ngôi nhà đáng lẽ phải được kích hoạt cờ xóa mềm thành true.");
    }

    @Test
    void getAll_shouldReturnOnlyActiveHousesOfCurrentOwner() throws Exception {
        House house1 = House.builder()
                .userId(userId)
                .addressLine("Nhà số 1 của Owner")
                .isDeleted(false)
                .build();

        House house2 = House.builder()
                .userId(userId)
                .addressLine("Nhà số 2 của Owner")
                .isDeleted(false)
                .build();

        // Tạo 1 ngôi nhà thuộc về User khác hoàn toàn để thử thách bộ lọc
        House houseOfOtherUser = House.builder()
                .userId(otherUserId)
                .addressLine("Nhà của người hàng xóm")
                .isDeleted(false)
                .build();

        // Tạo 1 ngôi nhà của chính User hiện tại nhưng đã bị xóa mềm từ trước
        House deletedHouse = House.builder()
                .userId(userId)
                .addressLine("Ngôi nhà cũ đã bán")
                .isDeleted(true)
                .build();

        houseRepository.saveAll(List.of(house1, house2, houseOfOtherUser, deletedHouse));

        // Gọi API để kiểm tra bộ lọc bảo mật: Hệ thống chỉ được trả về đúng 2 ngôi nhà đang hoạt động của chính User đó
        mockMvc.perform(get("/api/houses")
                        .param("userId", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].addressLine", is("Nhà số 1 của Owner")))
                .andExpect(jsonPath("$[1].addressLine", is("Nhà số 2 của Owner")));
    }
}