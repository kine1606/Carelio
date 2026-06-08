package com.Carelio.household_service;

import com.Carelio.household_service.entity.Room;
import com.Carelio.household_service.repository.EquipmentRepository;
import com.Carelio.household_service.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
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

@Import(TestcontainersConfiguration.class)
@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
class RoomIntegrationTest {

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
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        equipmentRepository.deleteAll();
        roomRepository.deleteAll();
    }

    @Test
    void createRoom_shouldCreateRoomWithOwnerIdFromHeader() throws Exception {
        String requestBody = """
                {
                    "name": "Phòng khách",
                    "floor": 1,
                    "description": "Phòng chính"
                }
                """;

        mockMvc.perform(post("/api/rooms")
                        .header("X-USER-ID", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ownerId", is(1)))
                .andExpect(jsonPath("$.name", is("Phòng khách")))
                .andExpect(jsonPath("$.floor", is(1)))
                .andExpect(jsonPath("$.description", is("Phòng chính")));
    }

    @Test
    void createRoom_missingOwnerIdHeader_shouldReturnBadRequest() throws Exception {
        String requestBody = """
            {
                "name": "Phòng lỗi",
                "floor": 1
            }
            """;

        mockMvc.perform(post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest()); // Or .isUnauthorized() depending on your global handler
    }
    @Test
    void createRoom_invalidData_shouldReturnBadRequest() throws Exception {
        String invalidRequestBody = """
            {
                "name": "", 
                "floor": -5.5
            }
            """;

        mockMvc.perform(post("/api/rooms")
                        .header("X-USER-ID", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getRoomById_shouldReturnRoom_whenOwnedByUser() throws Exception {
        Room room = roomRepository.save(Room.builder()
                .ownerId(1L).name("Phòng bếp").floor(1).deleted(false).build());

        mockMvc.perform(get("/api/rooms/" + room.getId())
                        .header("X-USER-ID", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Phòng bếp")));
    }

//    @Test
//    void getRoomById_shouldReturnNotFoundOrForbidden_whenNotOwnedByUser() throws Exception {
//        Room room = roomRepository.save(Room.builder()
//                .ownerId(2L).name("Phòng bí mật").floor(1).deleted(false).build());
//
//        mockMvc.perform(get("/api/rooms/" + room.getId())
//                        .header("X-USER-ID", 1L)) // User 1 accessing User 2's room
//                .andExpect(status().isNotFound()); // or isForbidden()
//    }

    @Test
    void updateRoom_shouldModifyAllowedFields_whenOwnedByUser() throws Exception {
        Room existingRoom = roomRepository.save(Room.builder()
                .ownerId(1L).name("Phòng cũ").floor(1).deleted(false).build());

        String updatedBody = """
            {
                "name": "Phòng mới đổi tên",
                "floor": 1,
                "description": "Đã sửa đổi"
            }
            """;

                                mockMvc.perform(put("/api/rooms/" + existingRoom.getId()) // Or .put() depending on your route
                        .header("X-USER-ID", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Phòng mới đổi tên")))
                .andExpect(jsonPath("$.description", is("Đã sửa đổi")));
    }

    @Test
    void deleteRoom_shouldSoftDeleteRoom() throws Exception {
        Room room = roomRepository.save(Room.builder()
                .ownerId(1L).name("Phòng sắp xóa").floor(1).deleted(false).build());

        mockMvc.perform(delete("/api/rooms/" + room.getId())
                        .header("X-USER-ID", 1L))
                .andExpect(status().isNoContent()); // or .isOk()

        // Assert directly against the DB to verify it was a SOFT delete
        Room deletedRoom = roomRepository.findById(room.getId()).orElseThrow();
        assertTrue(deletedRoom.getDeleted() == true, "The room should be soft-deleted in the database.");
    }

    @Test
    void getAll_shouldReturnOnlyRoomsOfCurrentOwner() throws Exception {
        Room room1 = Room.builder()
                .ownerId(1L)
                .name("Phòng khách")
                .floor(1)
                .description("Phòng của owner 1")
                .deleted(false)
                .build();

        Room room2 = Room.builder()
                .ownerId(1L)
                .name("Phòng ngủ")
                .floor(2)
                .description("Phòng ngủ owner 1")
                .deleted(false)
                .build();

        Room roomOfOtherUser = Room.builder()
                .ownerId(2L)
                .name("Phòng khách user khác")
                .floor(1)
                .description("Không được trả về")
                .deleted(false)
                .build();

        Room deletedRoom = Room.builder()
                .ownerId(1L)
                .name("Phòng đã xóa")
                .floor(3)
                .description("Không được trả về")
                .deleted(true)
                .build();

        roomRepository.saveAll(List.of(room1, room2, roomOfOtherUser, deletedRoom));

        mockMvc.perform(get("/api/rooms")
                        .header("X-USER-ID", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].ownerId", is(1)))
                .andExpect(jsonPath("$[1].ownerId", is(1)));
    }
}