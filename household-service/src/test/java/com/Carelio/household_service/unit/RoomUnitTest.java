package com.Carelio.household_service.unit;

import com.Carelio.household_service.dto.request.CreateRoomRequest;
import com.Carelio.household_service.dto.request.UpdateRoomRequest;
import com.Carelio.household_service.dto.response.RoomResponse;
import com.Carelio.household_service.entity.Equipment;
import com.Carelio.household_service.entity.House;
import com.Carelio.household_service.entity.Room;
import com.Carelio.household_service.mapper.RoomMapper;
import com.Carelio.household_service.repository.HouseRepository;
import com.Carelio.household_service.repository.RoomRepository;
import com.Carelio.household_service.service.RoomService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit Tests cho RoomService")
class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;
    @Mock
    private RoomMapper roomMapper;
    @Mock
    private HouseRepository houseRepository;

    @InjectMocks
    private RoomService roomService;

    private String userId;
    private Long roomId;
    private Long houseId;

    private House house;
    private Room room;
    private RoomResponse roomResponse;

    @BeforeEach
    void setUp() {
        userId = "user-customer-uuid-888";
        roomId = 10L;
        houseId = 20L;

        house = House.builder()
                .id(houseId)
                .userId(userId)
                .addressLine("456 Lê Hồng Phong, Quận 5")
                .build();

        room = Room.builder()
                .id(roomId)
                .name("Phòng Khách")
                .floor(1)
                .description("Phòng sinh hoạt chung")
                .house(house)
                .equipments(new ArrayList<>()) // Mặc định chưa có thiết bị nào bên trong
                .isDeleted(false)
                .build();

        roomResponse = new RoomResponse();
        roomResponse.setName(room.getName());
        roomResponse.setFloor(room.getFloor());
        roomResponse.setHouseId(houseId);
    }

    // =========================================================================
    // TEST HÀM GET ALL
    // =========================================================================
    @Nested
    @DisplayName("Tests cho hàm getAll")
    class GetAllTests {
        @Test
        @DisplayName("Nên trả về danh sách phòng chưa bị xóa của User")
        void shouldReturnRoomList_WhenUserHasActiveRooms() {
            // Given
            when(roomRepository.findBySubmittingMethodOrSimilar(userId)).thenReturn(List.of(room));
            when(roomMapper.toResponseList(any())).thenReturn(List.of(roomResponse));

            // When
            List<RoomResponse> result = roomService.getAll(userId);

            // Then
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("Phòng Khách", result.get(0).getName());
            verify(roomRepository, times(1)).findByHouse_UserIdAndIsDeletedFalse(userId);
        }
    }

    // =========================================================================
    // TEST HÀM GET BY ID
    // =========================================================================
    @Nested
    @DisplayName("Tests cho hàm getById")
    class GetByIdTests {
        @Test
        @DisplayName("Nên trả về phòng chính xác khi tìm thấy ID và thuộc quyền sở hữu")
        void shouldReturnRoomResponse_WhenIdAndUserAreValid() {
            // Given
            when(roomRepository.findByIdAndHouse_UserId(roomId, userId)).thenReturn(Optional.of(room));
            when(roomMapper.toResponse(room)).thenReturn(roomResponse);

            // When
            RoomResponse result = roomService.getById(userId, roomId);

            // Then
            assertNotNull(result);
            assertEquals(1, result.getFloor());
            verify(roomRepository, times(1)).findByIdAndHouse_UserId(roomId, userId);
        }

        @Test
        @DisplayName("Nên ném lỗi EntityNotFoundException nếu phòng không tồn tại hoặc sai chủ")
        void shouldThrowException_WhenRoomNotFoundOrWrongUser() {
            // Given
            when(roomRepository.findByIdAndHouse_UserId(roomId, userId)).thenReturn(Optional.empty());

            // When & Then
            assertThrows(EntityNotFoundException.class, () -> roomService.getById(userId, roomId));
        }
    }

    // =========================================================================
    // TEST HÀM CREATE ROOM
    // =========================================================================
    @Nested
    @DisplayName("Tests cho hàm createRoom")
    class CreateRoomTests {
        @Test
        @DisplayName("Nên tạo phòng thành công khi House ID hợp lệ và thuộc sở hữu")
        void shouldCreateRoomSuccessfully_WhenHouseIdOwnedByUser() {
            // Given
            CreateRoomRequest request = CreateRoomRequest.builder()
                    .name("Phòng Khách")
                    .houseId(houseId)
                    .floor(1)
                    .build();

            when(houseRepository.findByIdAndUserId(houseId, userId)).thenReturn(Optional.of(house));
            when(roomMapper.toEntity(request)).thenReturn(room);
            when(roomRepository.save(any(Room.class))).thenReturn(room);
            when(roomMapper.toResponse(room)).thenReturn(roomResponse);

            // When
            RoomResponse result = roomService.createRoom(userId, request);

            // Then
            assertNotNull(result);
            verify(roomRepository, times(1)).save(any(Room.class));
            assertEquals("Phòng Khách", result.getName());
        }

        @Test
        @DisplayName("Nên ném lỗi khi cố tạo phòng vào một Ngôi nhà không tồn tại/không thuộc sở hữu")
        void shouldThrowException_WhenHouseNotFoundOnCreate() {
            CreateRoomRequest request = CreateRoomRequest.builder().houseId(houseId).build();
            when(houseRepository.findByIdAndUserId(houseId, userId)).thenReturn(Optional.empty());

            assertThrows(EntityNotFoundException.class, () -> roomService.createRoom(userId, request));
            verify(roomRepository, never()).save(any());
        }
    }

    // =========================================================================
    // TEST HÀM UPDATE ROOM
    // =========================================================================
    @Nested
    @DisplayName("Tests cho hàm updateRoom")
    class UpdateRoomTests {
        @Test
        @DisplayName("Nên cập nhật thông tin phòng thành công khi các trường không null")
        void shouldUpdateRoomFields_WhenRequestHasData() {
            // Given
            UpdateRoomRequest req = UpdateRoomRequest.builder()
                    .name("Phòng Ngủ Master")
                    .floor(2)
                    .description("Cập nhật mô tả")
                    .build();

            when(roomRepository.findByIdAndHouse_UserId(roomId, userId)).thenReturn(Optional.of(room));
            when(roomRepository.save(any(Room.class))).thenReturn(room);
            when(roomMapper.toResponse(room)).thenReturn(roomResponse);

            // When
            RoomResponse result = roomService.updateRoom(userId, roomId, req);

            // Then
            assertNotNull(result);
            assertEquals("Phòng Ngủ Master", room.getName());
            assertEquals(2, room.getFloor());
            assertEquals("Cập nhật mô tả", room.getDescription());
            verify(roomRepository, times(1)).save(room);
        }
    }

    // =========================================================================
    // TEST HÀM SOFT DELETE ROOM (CỰC KỲ QUAN TRỌNG 🎯)
    // =========================================================================
    @Nested
    @DisplayName("Tests cho hàm softDelete")
    class SoftDeleteTests {
        @Test
        @DisplayName("Nên đổi cờ isDeleted thành true khi phòng trống (Không có thiết bị)")
        void shouldSoftDeleteRoomSuccessfully_WhenRoomIsEmpty() {
            // Given
            when(roomRepository.findByIdAndHouse_UserId(roomId, userId)).thenReturn(Optional.of(room));
            when(roomRepository.save(any(Room.class))).thenReturn(room);
            when(roomMapper.toResponse(room)).thenReturn(roomResponse);

            // When
            RoomResponse result = roomService.softDelete(userId, roomId);

            // Then
            assertTrue(room.getIsDeleted());
            verify(roomRepository, times(1)).save(room);
        }

        @Test
        @DisplayName("Nên ném lỗi RuntimeException và chặn xóa phòng nếu phòng vẫn còn thiết bị")
        void shouldThrowExceptionAndBlockDelete_WhenRoomHasEquipments() {
            // Given: Nhét thêm 1 thiết bị giả lập vào phòng để kích hoạt bẫy chặn logic
            Equipment mockEquipment = Equipment.builder().id(99L).name("Quạt trần").build();
            room.setEquipments(List.of(mockEquipment));

            when(roomRepository.findByIdAndHouse_UserId(roomId, userId)).thenReturn(Optional.of(room));

            // When & Then
            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> roomService.softDelete(userId, roomId));

            assertEquals("Cannot delete this room because it has equipments", exception.getMessage());
            assertFalse(room.getIsDeleted()); // Đảm bảo cờ xóa mềm vẫn phải là false
            verify(roomRepository, never()).save(any()); // Đảm bảo hoàn toàn không gọi lệnh lưu xuống DB
        }
    }
}