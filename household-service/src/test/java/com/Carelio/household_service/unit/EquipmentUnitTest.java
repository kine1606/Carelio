package com.Carelio.household_service.service;

import com.Carelio.household_service.dto.request.CreateEquipmentRequest;
import com.Carelio.household_service.dto.request.UpdateEquipmentRequest;
import com.Carelio.household_service.dto.response.EquipmentResponse;
import com.Carelio.household_service.dto.response.EquipmentValidationResponse;
import com.Carelio.household_service.entity.*;
import com.Carelio.household_service.mapper.EquipmentMapper;
import com.Carelio.household_service.repository.EquipmentCategoryRepository;
import com.Carelio.household_service.repository.EquipmentRepository;
import com.Carelio.household_service.repository.HouseRepository;
import com.Carelio.household_service.repository.RoomRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit Tests cho EquipmentService")
class EquipmentServiceTest {

    @Mock
    private EquipmentRepository equipmentRepository;
    @Mock
    private RoomRepository roomRepository;
    @Mock
    private EquipmentMapper equipmentMapper;
    @Mock
    private EquipmentCategoryRepository equipmentCategoryRepository;
    @Mock
    private HouseRepository houseRepository;

    @InjectMocks
    private EquipmentService equipmentService;

    private String userId;
    private Long equipmentId;
    private Long roomId;
    private Long categoryId;
    private Long houseId;

    private Equipment equipment;
    private Room room;
    private EquipmentCategory category;
    private House house;
    private EquipmentResponse equipmentResponse;

    @BeforeEach
    void setUp() {
        userId = "user-uuid-123";
        equipmentId = 1L;
        roomId = 2L;
        categoryId = 3L;
        houseId = 4L;

        house = House.builder().id(houseId).addressLine("123 Đường ABC, Quận 5").userId(userId).build();
        room = Room.builder().id(roomId).name("Phòng Khách").house(house).build();
        category = EquipmentCategory.builder().id(categoryId).name("Máy Lạnh").build();

        equipment = Equipment.builder()
                .id(equipmentId)
                .name("Máy lạnh Daikin 1.5 HP")
                .brand("Daikin")
                .model("FTKB35WAVMV")
                .serialNumber("DK-9999")
                .status(EquipmentStatus.ACTIVE)
                .conditionStatus(EquipmentConditionStatus.NORMAL)
                .room(room)
                .equipmentCategory(category)
                .isDeleted(false)
                .build();

        equipmentResponse = new EquipmentResponse();
        equipmentResponse.setName(equipment.getName());
        equipmentResponse.setBrand(equipment.getBrand());
        equipmentResponse.setStatus(equipment.getStatus());
    }

    // =========================================================================
    // TEST HÀM GET ALL
    // =========================================================================
    @Nested
    @DisplayName("Tests cho hàm getAll")
    class GetAllTests {
        @Test
        @DisplayName("Nên trả về danh sách thiết bị khi User hợp lệ")
        void shouldReturnEquipmentList_WhenUserHasEquipments() {
            // Given
            when(equipmentRepository.findAllByIsDeletedFalseAndRoom_House_UserId(userId))
                    .thenReturn(List.of(equipment));
            when(equipmentMapper.toResponseList(any())).thenReturn(List.of(equipmentResponse));

            // When
            List<EquipmentResponse> result = equipmentService.getAll(userId);

            // Then
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("Máy lạnh Daikin 1.5 HP", result.get(0).getName());
            verify(equipmentRepository, times(1)).findAllByIsDeletedFalseAndRoom_House_UserId(userId);
        }
    }

    // =========================================================================
    // TEST HÀM GET BY ID
    // =========================================================================
    @Nested
    @DisplayName("Tests cho hàm getById")
    class GetByIdTests {
        @Test
        @DisplayName("Nên trả về thiết bị khi tìm thấy ID và thuộc quyền sở hữu của User")
        void shouldReturnEquipment_WhenIdExistsAndOwnedByUser() {
            // Given
            when(equipmentRepository.findByIdAndRoom_House_UserId(equipmentId, userId)).thenReturn(Optional.of(equipment));
            when(equipmentMapper.toResponse(equipment)).thenReturn(equipmentResponse);

            // When
            EquipmentResponse result = equipmentService.getById(userId, equipmentId);

            // Then
            assertNotNull(result);
            assertEquals("Daikin", result.getName());
            verify(equipmentRepository, times(1)).findByIdAndRoom_House_UserId(equipmentId, userId);
        }

        @Test
        @DisplayName("Nên ném lỗi EntityNotFoundException khi không tìm thấy thiết bị")
        void shouldThrowException_WhenEquipmentNotFound() {
            // Given
            when(equipmentRepository.findByIdAndRoom_House_UserId(equipmentId, userId)).thenReturn(Optional.empty());

            // When & Then
            assertThrows(EntityNotFoundException.class, () -> equipmentService.getById(userId, equipmentId));
        }
    }

    // =========================================================================
    // TEST HÀM CREATE EQUIPMENT
    // =========================================================================
    @Nested
    @DisplayName("Tests cho hàm createEquipment")
    class CreateEquipmentTests {
        @Test
        @DisplayName("Nên tạo thiết bị thành công khi truyền đúng Room và Category")
        void shouldCreateEquipmentSuccessfully_WhenRequestIsValid() {
            // Given
            CreateEquipmentRequest req = CreateEquipmentRequest.builder()
                    .roomId(roomId)
                    .equipmentCategoryId(categoryId)
                    .name("Máy lạnh Daikin 1.5 HP")
                    .brand("Daikin")
                    .build();

            when(roomRepository.findByIdAndHouse_UserId(roomId, userId)).thenReturn(Optional.of(room));
            when(equipmentCategoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
            when(equipmentMapper.toEntity(req)).thenReturn(equipment);
            when(equipmentRepository.save(any(Equipment.class))).thenReturn(equipment);
            when(equipmentMapper.toResponse(equipment)).thenReturn(equipmentResponse);

            // When
            EquipmentResponse result = equipmentService.createEquipment(userId, req);

            // Then
            assertNotNull(result);
            verify(equipmentRepository, times(1)).save(any(Equipment.class));
        }

        @Test
        @DisplayName("Nên ném lỗi khi không tìm thấy Phòng hoặc Phòng không thuộc về User")
        void shouldThrowException_WhenRoomNotFoundInCreate() {
            CreateEquipmentRequest req = CreateEquipmentRequest.builder().roomId(roomId).build();
            when(roomRepository.findByIdAndHouse_UserId(roomId, userId)).thenReturn(Optional.empty());

            assertThrows(EntityNotFoundException.class, () -> equipmentService.createEquipment(userId, req));
            verify(equipmentRepository, never()).save(any());
        }
    }

    // =========================================================================
    // TEST HÀM UPDATE EQUIPMENT
    // =========================================================================
    @Nested
    @DisplayName("Tests cho hàm updateEquipment")
    class UpdateEquipmentTests {
        @Test
        @DisplayName("Nên cập nhật thành công các trường thông tin được truyền lên")
        void shouldUpdateEquipmentSuccessfully_WhenRequestContainsUpdates() {
            // Given
            UpdateEquipmentRequest req = UpdateEquipmentRequest.builder()
                    .name("Tên máy lạnh mới")
                    .status(EquipmentStatus.INACTIVE)
                    .build();

            when(equipmentRepository.findByIdAndRoom_House_UserId(equipmentId, userId)).thenReturn(Optional.of(equipment));
            when(equipmentRepository.save(any(Equipment.class))).thenReturn(equipment);
            when(equipmentMapper.toResponse(equipment)).thenReturn(equipmentResponse);

            // When
            EquipmentResponse result = equipmentService.updateEquipment(userId, equipmentId, req);

            // Then
            assertNotNull(result);
            verify(equipmentRepository, times(1)).save(equipment);
            assertEquals(EquipmentStatus.INACTIVE, equipment.getStatus());
            assertEquals("Tên máy lạnh mới", equipment.getName());
        }
    }

    // =========================================================================
    // TEST HÀM DELETE EQUIPMENT (SOFT DELETE)
    // =========================================================================
    @Nested
    @DisplayName("Tests cho hàm deleteEquipment")
    class DeleteEquipmentTests {
        @Test
        @DisplayName("Nên bật cờ isDeleted thành true khi xóa thiết bị")
        void shouldSetIsDeletedToTrue_WhenDeleteIsCalled() {
            // Given
            when(equipmentRepository.findByIdAndRoom_House_UserId(equipmentId, userId)).thenReturn(Optional.of(equipment));
            when(equipmentRepository.save(any(Equipment.class))).thenReturn(equipment);
            when(equipmentMapper.toResponse(equipment)).thenReturn(equipmentResponse);

            // When
            EquipmentResponse result = equipmentService.deleteEquipment(userId, equipmentId);

            // Then
            assertTrue(equipment.getIsDeleted());
            verify(equipmentRepository, times(1)).save(equipment);
        }
    }

    // =========================================================================
    // TEST HÀM VALIDATE EQUIPMENT (FEIGN CLIENT CHUYÊN DÙNG)
    // =========================================================================
    @Nested
    @DisplayName("Tests cho hàm validateEquipment")
    class ValidateEquipmentTests {
        @Test
        @DisplayName("Nên trả về thông tin map chuỗi chuẩn xác phục vụ Order Service")
        void shouldReturnValidationResponse_WhenAllIdsMatch() {
            // Given
            when(houseRepository.findByIdAndUserId(houseId, userId)).thenReturn(Optional.of(house));
            when(roomRepository.findByIdAndHouse_Id(roomId, houseId)).thenReturn(Optional.of(room));
            when(equipmentRepository.findByIdAndRoom_Id(equipmentId, roomId)).thenReturn(Optional.of(equipment));

            // When
            EquipmentValidationResponse result = equipmentService.validateEquipment(userId, equipmentId, roomId, houseId);

            // Then
            assertNotNull(result);
            assertEquals("123 Đường ABC, Quận 5", result.getHouseAddressLine());
            assertEquals("Phòng Khách", result.getRoomName());
            assertEquals("DK-9999", result.getEquipmentSerialNumber());
        }
    }
}