package com.Carelio.worker_service.integration;

import com.Carelio.worker_service.client.HouseholdClient;
import com.Carelio.worker_service.client.OrderClient;
import com.Carelio.worker_service.client.dto.CategoryResponse;
import com.Carelio.worker_service.client.dto.OrderResponse;
import com.Carelio.worker_service.dto.request.WorkerProfileRequest;
import com.Carelio.worker_service.dto.request.WorkerSkillRequest;
import com.Carelio.worker_service.entity.ServiceSkill;
import com.Carelio.worker_service.entity.ServiceSkillCode;
import com.Carelio.worker_service.entity.WorkerProfile;
import com.Carelio.worker_service.entity.WorkerStatus;
import com.Carelio.worker_service.repository.ServiceSkillRepository;
import com.Carelio.worker_service.repository.WorkerProfileRepository;
import com.Carelio.worker_service.repository.WorkerSkillRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import tools.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class WorkerIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WorkerProfileRepository workerProfileRepository;

    @Autowired
    private WorkerSkillRepository workerSkillRepository;

    @Autowired
    private ServiceSkillRepository serviceSkillRepository;

    @Autowired
    private ObjectMapper objectMapper;

    // 🌟 GIẢ LẬP FEIGN CLIENT: Tránh việc test bị sập do không gọi được sang service khác
    @MockitoBean
    private HouseholdClient householdClient;

    @MockitoBean
    private OrderClient orderClient;

    private String userId;
    private Long orderId;
    private Long categoryId;
    private ServiceSkill cleaningSkill;

    @BeforeEach
    void setUp() {
        // Dọn sạch data test cũ theo đúng thứ tự ràng buộc khóa ngoại
        workerSkillRepository.deleteAll();
        workerProfileRepository.deleteAll();
        serviceSkillRepository.deleteAll();

        userId = "worker-keycloak-uuid-555";
        orderId = 100L;
        categoryId = 3L;

        // Tạo sẵn dữ liệu danh mục kỹ năng mẫu trong DB Integration
        cleaningSkill = new ServiceSkill();
        cleaningSkill.setServiceSkillCode(ServiceSkillCode.CLEANING); // Giả định bạn có Enum này
        serviceSkillRepository.save(cleaningSkill);
    }

    // =========================================================================
    // INTEGRATION TEST: TẠO HỒ SƠ THỢ
    // =========================================================================
    @Test
    void createWorkerProfile_shouldSaveToPostgresAndReturnResponse() throws Exception {
        WorkerProfileRequest request = WorkerProfileRequest.builder()
                .bio("Chuyên viên vệ sinh máy lạnh, máy giặt khu vực Q5")
                .build();

        mockMvc.perform(post("/api/workers")
                        .param("userId", userId) // Khớp cấu hình nhận diện Identity từ Gateway truyền xuống
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(userId)))
                .andExpect(jsonPath("$.bio", is("Chuyên viên vệ sinh máy lạnh, máy giặt khu vực Q5")))
                .andExpect(jsonPath("$.status", is("AVAILABLE")))
                .andExpect(jsonPath("$.totalJobs", is(0)));
    }

    // =========================================================================
    // INTEGRATION TEST: THÊM KỸ NĂNG CHO THỢ (MOCK FEIGN HOUSEHOLD)
    // =========================================================================
    @Test
    void addWorkerSkill_shouldSaveSkill_whenFeignClientReturnsCategory() throws Exception {
        // Dựng sẵn 1 hồ sơ thợ nằm trong DB Postgres thực tế
        WorkerProfile profile = WorkerProfile.builder()
                .userId(userId).bio("Kỹ thuật viên").status(WorkerStatus.AVAILABLE).totalJobs(0).build();
        workerProfileRepository.save(profile);

        WorkerSkillRequest request = WorkerSkillRequest.builder()
                .serviceSkillId(cleaningSkill.getId())
                .equipmentCategoryId(categoryId)
                .yearExperience(3)
                .build();

        // Cấu hình Mock cho Feign Client của Household Service phản hồi dữ liệu giả lập mẫu
        CategoryResponse mockCategory = new CategoryResponse();
        mockCategory.setId(categoryId);
        mockCategory.setName("Máy Lạnh");
        when(householdClient.getCategoryById(categoryId)).thenReturn(mockCategory);

        mockMvc.perform(post("/api/workers/skills")
                        .param("userId", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.equipmentCategoryName", is("Máy Lạnh")))
                .andExpect(jsonPath("$.yearExperience", is(3)));
    }

    // =========================================================================
    // INTEGRATION TEST: THỢ NHẬN ĐƠN (MOCK LUỒNG ĐỔI TRẠNG THÁI MÁY - STATE MACHINE)
    // =========================================================================
    @Test
    void acceptOrder_shouldUpdateWorkerStatusToOnTheWay_whenSkillMatches() throws Exception {
        // 1. Tạo hồ sơ thợ sẵn sàng nhận việc
        WorkerProfile profile = WorkerProfile.builder()
                .userId(userId).bio("Kỹ thuật viên").status(WorkerStatus.AVAILABLE).totalJobs(0).build();
        workerProfileRepository.save(profile);

        // 2. Gán sẵn kỹ năng tương ứng vào DB để vượt qua bước validate skill cứng
        com.Carelio.worker_service.entity.WorkerSkill skill = com.Carelio.worker_service.entity.WorkerSkill.builder()
                .workerProfile(profile)
                .serviceSkill(cleaningSkill)
                .equipmentCategoryId(categoryId)
                .equipmentCategoryName("Máy Lạnh")
                .yearExperience(2)
                .build();
        workerSkillRepository.save(skill);

        // 3. Giả lập luồng trả dữ liệu của Order Service thông qua Feign Client
        OrderResponse mockOrder = new OrderResponse();
        mockOrder.setOrderId(orderId);
        mockOrder.setEquipmentCategoryId(categoryId);
        mockOrder.setServiceSkillId(cleaningSkill.getId());
        when(orderClient.getOrder(orderId)).thenReturn(mockOrder);

        // Do hàm gửi đi không trả kết quả quan trọng, ta cấu hình doNothing cho Feign Client đầu gọi lệnh
        doNothing().when(orderClient).acceptOrder(anyLong(), anyLong());

        // 4. Thực hiện cuộc gọi API thực tế đổi trạng thái sang ON_THE_WAY
        mockMvc.perform(post("/api/workers/orders/" + orderId + "/accept")
                        .param("userId", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("ON_THE_WAY"))); // Bộ đệm Redis Config mới cũng sẽ tự động lưu chữ ON_THE_WAY này!
    }
}