package com.Carelio.household_service;

import com.Carelio.household_service.dto.request.CreateRoomRequest;
import com.Carelio.household_service.dto.request.UpdateRoomRequest;
import com.Carelio.household_service.entity.House;
import com.Carelio.household_service.entity.Room;
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

import java.util.ArrayList;
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
class RoomIntegrationTest {

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
    private House userHouse;
    private House otherHouse;

    @BeforeEach
    void setUp() {
        // Làm sạch Database theo đúng thứ tự ràng buộc khóa ngoại
        equipmentRepository.deleteAll();
        roomRepository.deleteAll();
        houseRepository.deleteAll();

        userId = "user-customer-uuid-888";
        otherUserId = "user-other-uuid-999";

        // Tạo sẵn dữ liệu Ngôi nhà làm điểm tựa cho các Phòng
        userHouse = House.builder()
                .userId(userId)
                .addressLine("123 Đường ABC, Quận 5")
                .build();

        otherHouse = House.builder()
                .userId(otherUserId)
                .addressLine("456 Lê Hồng Phong, Quận 5")
                .build();

        houseRepository.saveAll(List.of(userHouse, otherHouse));
    }

    @Test
    void createRoom_shouldCreateRoomSuccessfully() throws Exception {
        CreateRoomRequest request = CreateRoomRequest.builder()
                .name("Phòng khách")
                .floor(1)
                .description("Phòng chính")
                .houseId(userHouse.getId())
                .build();

        mockMvc.perform(post("/api/rooms")
                        .param("userId", userId) // Khớp với cách nhận tham số từ Controller của bạn
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Phòng khách")))
                .andExpect(jsonPath("$.floor", is(1)))
                .andExpect(jsonPath("$.description", is("Phòng chính")))
                .andExpect(jsonPath("$.houseId", is(userHouse.getId().intValue())));
    }

    @Test
    void getRoomById_shouldReturnRoom_whenOwnedByUser() throws Exception {
        Room room = Room.builder()
                .name("Phòng bếp")
                .floor(1)
                .house(userHouse)
                .isDeleted(false)
                .build();
        roomRepository.save(room);

        mockMvc.perform(get("/api/rooms/" + room.getId())
                        .param("userId", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Phòng bếp")))
                .andExpect(jsonPath("$.houseId", is(userHouse.getId().intValue())));
    }

    @Test
    void updateRoom_shouldModifyAllowedFields_whenOwnedByUser() throws Exception {
        Room existingRoom = Room.builder()
                .name("Phòng cũ")
                .floor(1)
                .description("Mô tả cũ")
                .house(userHouse)
                .isDeleted(false)
                .build();
        roomRepository.save(existingRoom);

        UpdateRoomRequest updatedRequest = UpdateRoomRequest.builder()
                .name("Phòng mới đổi tên")
                .floor(1)
                .description("Đã sửa đổi")
                .build();

        // Sử dụng patch hoặc put tùy theo cấu hình EndPoint Controller thực tế của bạn
        mockMvc.perform(patch("/api/rooms/" + existingRoom.getId())
                        .param("userId", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Phòng mới đổi tên")))
                .andExpect(jsonPath("$.description", is("Đã sửa đổi")));
    }

    @Test
    void deleteRoom_shouldSoftDeleteRoom() throws Exception {
        Room room = Room.builder()
                .name("Phòng sắp xóa")
                .floor(1)
                .house(userHouse)
                .equipments(new ArrayList<>()) // Đảm bảo phòng trống để không bị dính bẫy chặn logic
                .isDeleted(false)
                .build();
        roomRepository.save(room);

        mockMvc.perform(delete("/api/rooms/" + room.getId())
                        .param("userId", userId))
                .andExpect(status().isOk());

        // Truy vấn trực tiếp vào DB của Testcontainers để kiểm tra cờ xóa mềm isDeleted
        Room deletedRoom = roomRepository.findById(room.getId()).orElseThrow();
        assertTrue(deletedRoom.getIsDeleted(), "Phòng đáng lẽ phải được bật cờ xóa mềm thành true.");
    }

    @Test
    void getAll_shouldReturnOnlyRoomsOfCurrentOwner() throws Exception {
        Room room1 = Room.builder()
                .name("Phòng khách")
                .floor(1)
                .description("Phòng của owner 1")
                .house(userHouse)
                .isDeleted(false)
                .build();

        Room room2 = Room.builder()
                .name("Phòng ngủ")
                .floor(2)
                .description("Phòng ngủ owner 1")
                .house(userHouse)
                .isDeleted(false)
                .build();

        // Phòng của User khác
        Room roomOfOtherUser = Room.builder()
                .name("Phòng khách user khác")
                .floor(1)
                .house(otherHouse)
                .isDeleted(false)
                .build();

        // Phòng của chính User hiện tại nhưng đã bị xóa mềm trước đó
        Room deletedRoom = Room.builder()
                .name("Phòng đã xóa")
                .floor(3)
                .house(userHouse)
                .isDeleted(true)
                .build();

        roomRepository.saveAll(List.of(room1, room2, roomOfOtherUser, deletedRoom));

        // Thực hiện cuộc gọi API để kiểm tra bộ lọc bảo mật
        mockMvc.perform(get("/api/rooms")
                        .param("userId", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2))) // Phải lọc chuẩn xác, chỉ trả về đúng 2 phòng hoạt động
                .andExpect(jsonPath("$[0].name", is("Phòng khách")))
                .andExpect(jsonPath("$[1].name", is("Phòng ngủ")));
    }
}