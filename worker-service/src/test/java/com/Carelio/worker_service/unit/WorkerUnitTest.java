package com.Carelio.worker_service.unit;

import com.Carelio.worker_service.client.HouseholdClient;
import com.Carelio.worker_service.client.OrderClient;
import com.Carelio.worker_service.client.dto.CategoryResponse;
import com.Carelio.worker_service.client.dto.OrderResponse;
import com.Carelio.worker_service.dto.request.WorkerProfileRequest;
import com.Carelio.worker_service.dto.request.WorkerSkillRequest;
import com.Carelio.worker_service.dto.response.WorkerProfileResponse;
import com.Carelio.worker_service.dto.response.WorkerSkillResponse;
import com.Carelio.worker_service.entity.ServiceSkill;
import com.Carelio.worker_service.entity.WorkerProfile;
import com.Carelio.worker_service.entity.WorkerSkill;
import com.Carelio.worker_service.entity.WorkerStatus;
import com.Carelio.worker_service.mapper.WorkerProfileMapper;
import com.Carelio.worker_service.mapper.WorkerSkillMapper;
import com.Carelio.worker_service.repository.ServiceSkillRepository;
import com.Carelio.worker_service.repository.WorkerProfileRepository;
import com.Carelio.worker_service.repository.WorkerSkillRepository;
import com.Carelio.worker_service.service.WorkerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit Tests cho WorkerService")
class WorkerServiceTest {

    @Mock
    private WorkerProfileRepository workerProfileRepository;
    @Mock
    private WorkerSkillRepository workerSkillRepository;
    @Mock
    private ServiceSkillRepository serviceSkillRepository;
    @Mock
    private HouseholdClient householdClient;
    @Mock
    private OrderClient orderClient;
    @Mock
    private WorkerProfileMapper workerProfileMapper;
    @Mock
    private WorkerSkillMapper workerSkillMapper;

    @InjectMocks
    private WorkerService workerService;

    private String userId;
    private Long workerId;
    private Long skillId;
    private Long categoryId;
    private Long orderId;

    private WorkerProfile profile;
    private ServiceSkill serviceSkill;
    private WorkerSkill workerSkill;
    private WorkerProfileResponse profileResponse;
    private WorkerSkillResponse skillResponse;
    private OrderResponse orderResponse;
    private CategoryResponse categoryResponse;

    @BeforeEach
    void setUp() {
        userId = "worker-keycloak-uuid-555";
        workerId = 1L;
        skillId = 2L;
        categoryId = 3L;
        orderId = 100L;

        profile = WorkerProfile.builder()
                .id(workerId)
                .userId(userId)
                .bio("Kỹ thuật viên sửa điện lạnh 5 năm kinh nghiệm")
                .status(WorkerStatus.AVAILABLE)
                .totalJobs(10)
                .ratingAvg(4.8)
                .build();

        serviceSkill = new ServiceSkill();
        serviceSkill.setId(skillId);

        workerSkill = WorkerSkill.builder()
                .id(10L)
                .workerProfile(profile)
                .serviceSkill(serviceSkill)
                .equipmentCategoryId(categoryId)
                .equipmentCategoryName("Máy Lạnh")
                .yearExperience(3)
                .build();

        profileResponse = WorkerProfileResponse.builder()
                .id(workerId)
                .userId(userId)
                .status(WorkerStatus.AVAILABLE)
                .bio(profile.getBio())
                .build();

        skillResponse = new WorkerSkillResponse();
        skillResponse.setId(10L);
        skillResponse.setEquipmentCategoryId(categoryId);
        skillResponse.setEquipmentCategoryName("Máy Lạnh");

        orderResponse = new OrderResponse();
        orderResponse.setOrderId(orderId);
        orderResponse.setEquipmentCategoryId(categoryId);
        orderResponse.setServiceSkillId(skillId);

        categoryResponse = new CategoryResponse();
        categoryResponse.setId(categoryId);
        categoryResponse.setName("Máy Lạnh");
    }

    // =========================================================================
    // SECTION 1: QUAN LY HO SO THO (WORKER PROFILE TESTS)
    // =========================================================================
    @Nested
    @DisplayName("Tests cho luồng Hồ sơ Thợ")
    class WorkerProfileTests {

        @Test
        @DisplayName("Tạo hồ sơ thành công khi User chưa đăng ký")
        void shouldCreateProfileSuccessfully_WhenNotExists() {
            WorkerProfileRequest request = WorkerProfileRequest.builder().bio("Hello").build();
            when(workerProfileRepository.existsByUserId(userId)).thenReturn(false);
            when(workerProfileMapper.toEntity(request)).thenReturn(profile);
            when(workerProfileRepository.save(any(WorkerProfile.class))).thenReturn(profile);
            when(workerProfileMapper.toResponse(profile)).thenReturn(profileResponse);

            WorkerProfileResponse result = workerService.createWorkerProfile(userId, request);

            assertNotNull(result);
            assertEquals(userId, result.getUserId());
            verify(workerProfileRepository, times(1)).save(any(WorkerProfile.class));
        }

        @Test
        @DisplayName("Nên ném lỗi RuntimeException khi User cố tình tạo 2 hồ sơ thợ")
        void shouldThrowException_WhenProfileAlreadyExists() {
            WorkerProfileRequest request = WorkerProfileRequest.builder().build();
            when(workerProfileRepository.existsByUserId(userId)).thenReturn(true);

            assertThrows(RuntimeException.class, () -> workerService.createWorkerProfile(userId, request));
            verify(workerProfileRepository, never()).save(any());
        }

        @Test
        @DisplayName("Tìm kiếm hồ sơ theo chuỗi mã Keycloak UUID thành công")
        void shouldReturnProfile_WhenFindByKeycloakId() {
            when(workerProfileRepository.findByUserId(userId)).thenReturn(Optional.of(profile));
            when(workerProfileMapper.toResponse(profile)).thenReturn(profileResponse);

            WorkerProfileResponse result = workerService.getByKeyCloakUserId(userId);

            assertNotNull(result);
            verify(workerProfileRepository, times(1)).findByUserId(userId);
        }
    }

    // =========================================================================
    // SECTION 2: MA TRAN KY NANG (WORKER SKILLS TESTS)
    // =========================================================================
    @Nested
    @DisplayName("Tests cho luồng Kỹ năng Thợ")
    class WorkerSkillTests {

        @Test
        @DisplayName("Thêm kỹ năng mới thành công và xóa bộ đệm danh sách kĩ năng cũ")
        void shouldAddWorkerSkillSuccessfully_WhenValid() {
            WorkerSkillRequest request = WorkerSkillRequest.builder()
                    .serviceSkillId(skillId)
                    .equipmentCategoryId(categoryId)
                    .yearExperience(3)
                    .build();

            when(workerProfileRepository.findByUserId(userId)).thenReturn(Optional.of(profile));
            when(serviceSkillRepository.findById(skillId)).thenReturn(Optional.of(serviceSkill));
            when(householdClient.getCategoryById(categoryId)).thenReturn(categoryResponse);
            when(workerSkillRepository.existsByWorkerProfile_IdAndServiceSkill_IdAndEquipmentCategoryId(any(), any(), any()))
                    .thenReturn(false);
            when(workerSkillRepository.save(any(WorkerSkill.class))).thenReturn(workerSkill);
            when(workerSkillMapper.toResponse(any(WorkerSkill.class))).thenReturn(skillResponse);

            WorkerSkillResponse result = workerService.addWorkerSkill(userId, request);

            assertNotNull(result);
            verify(workerSkillRepository, times(1)).save(any(WorkerSkill.class));
        }
    }

    // =========================================================================
    // SECTION 3: TIEN TRINH DIEU PHOI DON HANG (WORKFLOW & STATE MACHINE)
    // =========================================================================
    @Nested
    @DisplayName("Tests cho luồng Nhận / Chạy đơn hàng")
    class OrderWorkflowTests {

        @Test
        @DisplayName("Thợ nhận đơn thành công -> Đổi trạng thái sang ON_THE_WAY")
        void shouldAcceptOrderSuccessfully_WhenWorkerIsAvailableAndHasSkill() {
            when(workerProfileRepository.findByUserId(userId)).thenReturn(Optional.of(profile));
            when(orderClient.getOrder(orderId)).thenReturn(orderResponse);
            when(workerSkillRepository.existsByWorkerProfile_IdAndEquipmentCategoryIdAndServiceSkill_Id(
                    workerId, categoryId, skillId)).thenReturn(true);
            when(workerProfileRepository.save(any(WorkerProfile.class))).thenReturn(profile);
            when(workerProfileMapper.toResponse(any(WorkerProfile.class))).thenReturn(profileResponse);

            WorkerProfileResponse result = workerService.acceptOrder(userId, orderId);

            assertNotNull(result);
            verify(orderClient, times(1)).acceptOrder(orderId, workerId);
            assertEquals(WorkerStatus.ON_THE_WAY, profile.getStatus());
        }

        @Test
        @DisplayName("Chặn nhận đơn và ném lỗi nếu Thợ đang bận đơn khác (Không ở trạng thái AVAILABLE)")
        void shouldThrowException_WhenWorkerIsNotAvailable() {
            profile.setStatus(WorkerStatus.BUSY); // Giả lập thợ đang bận làm việc
            when(workerProfileRepository.findByUserId(userId)).thenReturn(Optional.of(profile));

            assertThrows(RuntimeException.class, () -> workerService.acceptOrder(userId, orderId));
            verify(orderClient, never()).acceptOrder(anyLong(), anyLong());
        }

        @Test
        @DisplayName("Chặn nhận đơn nếu Thợ không có kỹ năng chuyên môn phù hợp với thiết bị")
        void shouldThrowException_WhenWorkerLacksRequiredSkill() {
            when(workerProfileRepository.findByUserId(userId)).thenReturn(Optional.of(profile));
            when(orderClient.getOrder(orderId)).thenReturn(orderResponse);
            when(workerSkillRepository.existsByWorkerProfile_IdAndEquipmentCategoryIdAndServiceSkill_Id(
                    workerId, categoryId, skillId)).thenReturn(false); // Thợ không có kỹ năng này

            assertThrows(RuntimeException.class, () -> workerService.acceptOrder(userId, orderId));
            verify(orderClient, never()).acceptOrder(anyLong(), anyLong());
        }

        @Test
        @DisplayName("Thợ bấm Bắt đầu làm việc -> Chuyển trạng thái sang BUSY")
        void shouldStartOrderSuccessfully_WhenStatusIsOnTheWay() {
            profile.setStatus(WorkerStatus.ON_THE_WAY); // Thợ phải đang đi đường mới được bấm bắt đầu làm
            when(workerProfileRepository.findByUserId(userId)).thenReturn(Optional.of(profile));
            when(workerProfileRepository.save(any(WorkerProfile.class))).thenReturn(profile);
            when(workerProfileMapper.toResponse(any(WorkerProfile.class))).thenReturn(profileResponse);

            WorkerProfileResponse result = workerService.startOrder(userId, orderId);

            assertNotNull(result);
            verify(orderClient, times(1)).startOrder(orderId, workerId);
            assertEquals(WorkerStatus.BUSY, profile.getStatus());
        }

        @Test
        @DisplayName("Thợ bấm Hoàn thành công việc -> Trả về AVAILABLE và tăng số lượng Total Jobs")
        void shouldCompleteOrderSuccessfully_WhenStatusIsBusy() {
            profile.setStatus(WorkerStatus.BUSY);
            int initialJobs = profile.getTotalJobs();

            when(workerProfileRepository.findByUserId(userId)).thenReturn(Optional.of(profile));
            when(workerProfileRepository.save(any(WorkerProfile.class))).thenReturn(profile);
            when(workerProfileMapper.toResponse(any(WorkerProfile.class))).thenReturn(profileResponse);

            WorkerProfileResponse result = workerService.completeOrder(userId, orderId);

            assertNotNull(result);
            verify(orderClient, times(1)).completeOrder(orderId, workerId);
            assertEquals(WorkerStatus.AVAILABLE, profile.getStatus());
            assertEquals(initialJobs + 1, profile.getTotalJobs()); // Đảm bảo đếm tăng 1 công việc thành công
        }
    }
}