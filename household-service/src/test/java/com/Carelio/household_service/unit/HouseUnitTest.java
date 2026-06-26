package com.Carelio.household_service.unit;

import com.Carelio.household_service.dto.request.CreateHouseRequest;
import com.Carelio.household_service.dto.request.UpdateHouseRequest;
import com.Carelio.household_service.dto.response.HouseResponse;
import com.Carelio.household_service.entity.House;
import com.Carelio.household_service.mapper.HouseMapper;
import com.Carelio.household_service.repository.HouseRepository;
import com.Carelio.household_service.service.HouseService;
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
@DisplayName("Unit Tests cho HouseService")
class HouseServiceTest {

    @Mock
    private HouseRepository houseRepository;

    @Mock
    private HouseMapper houseMapper;

    @InjectMocks
    private HouseService houseService;

    private String userId;
    private Long houseId;
    private House house;
    private HouseResponse houseResponse;

    @BeforeEach
    void setUp() {
        userId = "user-customer-uuid-999";
        houseId = 1L;

        house = House.builder()
                .id(houseId)
                .userId(userId)
                .addressLine("123 Đường ABC, Quận 5")
                .isDeleted(false)
                .build();

        houseResponse = new HouseResponse();
        // Giả định HouseResponse của bạn có các trường id và addressLine tương ứng
        // Do file HouseResponse chưa tải lên nên ta set giá trị tượng trưng để assert
    }

    // =========================================================================
    // TEST HÀM CREATE (POST)
    // =========================================================================
    @Nested
    @DisplayName("Tests cho hàm create")
    class CreateTests {
        @Test
        @DisplayName("Nên tạo ngôi nhà thành công khi truyền đúng dữ liệu")
        void shouldCreateHouseSuccessfully_WhenRequestIsValid() {
            // Given
            CreateHouseRequest request = CreateHouseRequest.builder()
                    .addressLine("123 Đường ABC, Quận 5")
                    .build();

            when(houseMapper.toEntity(request)).thenReturn(house);
            when(houseRepository.save(any(House.class))).thenReturn(house);
            when(houseMapper.toResponse(house)).thenReturn(houseResponse);

            // When
            HouseResponse result = houseService.create(userId, request);

            // Then
            assertNotNull(result);
            verify(houseRepository, times(1)).save(any(House.class));
            verify(houseMapper, times(1)).toEntity(request);
        }
    }

    // =========================================================================
    // TEST HÀM GET ALL
    // =========================================================================
    @Nested
    @DisplayName("Tests cho hàm getAll")
    class GetAllTests {
        @Test
        @DisplayName("Nên trả về danh sách nhà chưa bị xóa của User")
        void shouldReturnHouseList_WhenUserHasActiveHouses() {
            // Given
            when(houseRepository.findByUserIdAndIsDeletedFalse(userId)).thenReturn(List.of(house));
            when(houseMapper.toResponseList(any())).thenReturn(List.of(houseResponse));

            // When
            List<HouseResponse> result = houseService.getAll(userId);

            // Then
            assertNotNull(result);
            assertEquals(1, result.size());
            verify(houseRepository, times(1)).findByUserIdAndIsDeletedFalse(userId);
        }
    }

    // =========================================================================
    // TEST HÀM GET BY ID
    // =========================================================================
    @Nested
    @DisplayName("Tests cho hàm getById")
    class GetByIdTests {
        @Test
        @DisplayName("Nên trả về thông tin nhà khi ID tồn tại và đúng User sở hữu")
        void shouldReturnHouseResponse_WhenIdAndUserAreValid() {
            // Given
            when(houseRepository.findByIdAndUserId(houseId, userId)).thenReturn(Optional.of(house));
            when(houseMapper.toResponse(house)).thenReturn(houseResponse);

            // When
            HouseResponse result = houseService.getById(userId, houseId);

            // Then
            assertNotNull(result);
            verify(houseRepository, times(1)).findByIdAndUserId(houseId, userId);
        }

        @Test
        @DisplayName("Nên ném lỗi RuntimeException nếu không tìm thấy nhà")
        void shouldThrowException_WhenHouseNotFound() {
            // Given
            when(houseRepository.findByIdAndUserId(houseId, userId)).thenReturn(Optional.empty());

            // When & Then
            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> houseService.getById(userId, houseId));

            assertEquals("House not found with id: " + houseId, exception.getMessage());
        }
    }

    // =========================================================================
    // TEST HÀM UPDATE (PATCH/PUT)
    // =========================================================================
    @Nested
    @DisplayName("Tests cho hàm update")
    class UpdateTests {
        @Test
        @DisplayName("Nên cập nhật địa chỉ nhà thành công khi truyền dữ liệu mới")
        void shouldUpdateHouseAddress_WhenRequestHasNewAddress() {
            // Given
            UpdateHouseRequest request = UpdateHouseRequest.builder()
                    .addressLine("456 Lê Hồng Phong, Quận 5")
                    .build();

            when(houseRepository.findByIdAndUserId(houseId, userId)).thenReturn(Optional.of(house));
            when(houseRepository.save(any(House.class))).thenReturn(house);
            when(houseMapper.toResponse(house)).thenReturn(houseResponse);

            // When
            HouseResponse result = houseService.update(userId, houseId, request);

            // Then
            assertNotNull(result);
            assertEquals("456 Lê Hồng Phong, Quận 5", house.getAddressLine());
            verify(houseRepository, times(1)).save(house);
        }
    }

    // =========================================================================
    // TEST HÀM DELETE (SOFT DELETE)
    // =========================================================================
    @Nested
    @DisplayName("Tests cho hàm delete")
    class DeleteTests {
        @Test
        @DisplayName("Nên bật cờ isDeleted thành true khi gọi hàm xóa nhà")
        void shouldSetIsDeletedToTrue_WhenDeleteIsCalled() {
            // Given
            when(houseRepository.findByIdAndUserId(houseId, userId)).thenReturn(Optional.of(house));
            when(houseRepository.save(any(House.class))).thenReturn(house);
            when(houseMapper.toResponse(house)).thenReturn(houseResponse);

            // When
            HouseResponse result = houseService.delete(userId, houseId);

            // Then
            assertTrue(house.isDeleted()); // Kiểm tra cờ xóa mềm đã bật thành công
            verify(houseRepository, times(1)).save(house);
        }

        @Test
        @DisplayName("Nên ném lỗi và không thực hiện lưu nếu xóa một ngôi nhà không tồn tại")
        void shouldThrowException_WhenDeletingNonExistentHouse() {
            // Given
            when(houseRepository.findByIdAndUserId(houseId, userId)).thenReturn(Optional.empty());

            // When & Then
            assertThrows(RuntimeException.class, () -> houseService.delete(userId, houseId));
            verify(houseRepository, never()).save(any());
        }
    }
}