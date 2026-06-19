package com.Carelio.worker_service.service;

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
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class WorkerService
{
    private final WorkerProfileRepository workerProfileRepository;
    private final WorkerSkillRepository workerSkillRepository;
    private final ServiceSkillRepository serviceSkillRepository;
    private final HouseholdClient householdClient;

    private final WorkerProfileMapper workerProfileMapper;
    private final WorkerSkillMapper workerSkillMapper;

    private final OrderClient orderClient;

    // POST /api/workers
    @Transactional
    public WorkerProfileResponse createWorkerProfile(Long userId, WorkerProfileRequest workerProfileRequest)
    {
        if (workerProfileRepository.existsByUserId(userId)) {
            throw new RuntimeException("User already has worker profile");
        }
        WorkerProfile profile = workerProfileMapper.toEntity(workerProfileRequest);
        profile.setUserId(userId);
        WorkerProfile savedProfile = workerProfileRepository.save(profile);
        log.info("Worker profile {} created successfully ", savedProfile.getId());
        return workerProfileMapper.toResponse(savedProfile);
    }

    // GET /api/workers/{id}
    public WorkerProfileResponse getById(Long id)
    {
        WorkerProfile workerProfile = workerProfileRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Worker profile with id " + id + " not found"));
        return workerProfileMapper.toResponse(workerProfile);
    }

    //GET /api/workers
    public List<WorkerProfileResponse> getAllWorkerProfiles()
    {
        List<WorkerProfile> profileList = workerProfileRepository.findAll();
        log.info("Found {} profiles", profileList.size());
        return workerProfileMapper.toResponseList(profileList);
    }

    //    POST /api/workers/{workerId}/skills
    @Transactional
    public WorkerSkillResponse addWorkerSkill(Long workerId, WorkerSkillRequest request)
    {
        WorkerProfile profile = workerProfileRepository.findById(workerId)
                .orElseThrow(() -> new EntityNotFoundException("Worker profile with id " + workerId + " not found"));
        ServiceSkill skill = serviceSkillRepository.findById(request.getServiceSkillId())
                .orElseThrow(() -> new EntityNotFoundException("Skill with id " + request.getServiceSkillId() + " not found"));
        CategoryResponse categoryResponse = householdClient.getCategoryById(request.getEquipmentCategoryId());

        log.info("id: " + categoryResponse.getId());
        log.info("name: " + categoryResponse.getName());

        boolean exists = workerSkillRepository.existsByWorkerProfile_IdAndServiceSkill_IdAndEquipmentCategoryId
                (
                        workerId,
                        request.getServiceSkillId(),
                        request.getEquipmentCategoryId()
                );
        if (exists) {
            throw new RuntimeException("Worker skill already exists");
        }
        WorkerSkill workerSkill = WorkerSkill.builder()
                .workerProfile(profile)
                .serviceSkill(skill)
                .equipmentCategoryId(categoryResponse.getId())
                .equipmentCategoryName(categoryResponse.getName())
                .yearExperience(request.getYearExperience()).skillLevel(request.getSkillLevel())
                .build();
        WorkerSkill savedSkill = workerSkillRepository.save(workerSkill);
        log.info("Worker skill {} created successfully ", savedSkill.getId());
        return workerSkillMapper.toResponse(savedSkill);
    }

    public List<WorkerSkillResponse> getWorkerSkill(Long workerId)
    {
        WorkerProfile profile = workerProfileRepository.findById(workerId)
                .orElseThrow(() -> new EntityNotFoundException("Worker profile with id " + workerId + " not found"));
        List<WorkerSkill> skills = workerSkillRepository.findByWorkerProfile_Id(profile.getId());
        log.info("Found {} skills", skills.size());
        return workerSkillMapper.toResponseList(skills);
    }

    public WorkerProfileResponse updateRating(Long workerId, Integer rating)
    {
        if (rating == null) rating = 5;
        WorkerProfile profile = workerProfileRepository.findById(workerId)
                .orElseThrow(() -> new EntityNotFoundException("Worker profile with id " + workerId + " not found"));
        profile.setRatingAvg((profile.getRatingAvg() + rating) / 2);
        WorkerProfile savedProfile = workerProfileRepository.save(profile);
        log.info("Worker profile {} updated successfully ", savedProfile.getId());
        return workerProfileMapper.toResponse(savedProfile);
    }

    public WorkerProfileResponse acceptOrder(Long workerId, Long orderId) {
        WorkerProfile profile = workerProfileRepository.findById(workerId)
                .orElseThrow(() -> new EntityNotFoundException("Worker profile not found with id " + workerId));

        if (profile.getStatus() != WorkerStatus.AVAILABLE) {
            throw new RuntimeException("Worker is not available");
        }

        OrderResponse orderDetail;
        try {
            orderDetail = orderClient.getOrder(orderId);
        } catch (Exception e)
        {
            log.error("Unavailable to take order information from orderService to validate skill ", e);
            throw new RuntimeException("System interrupted, try again");
        }

        boolean hasRequiredSkill = workerSkillRepository.existsByWorkerProfile_IdAndEquipmentCategoryIdAndServiceSkill_Id(
                workerId,
                orderDetail.getEquipmentCategoryId(),
                orderDetail.getServiceSkillId()
        );

        if (!hasRequiredSkill) {
            throw new RuntimeException("Worker does not have required skill: " + orderDetail.getServiceSkillId());
        }
        try
        {
            orderClient.acceptOrder(orderId, workerId);
        } catch (feign.FeignException e) {
            log.error("Lỗi gửi từ Order Service: " + e.contentUTF8());
            throw new RuntimeException("Order service trả về lỗi: " + e.contentUTF8());
        } catch (Exception e) {
            log.error("Lỗi kết nối mạng: ", e);
            throw new RuntimeException("Unable to claim order, please try again");
        }

        WorkerProfile savedProfile = saveWorkerStatus(profile, WorkerStatus.ON_THE_WAY);

        log.info("Worker {} take order {} and change status to ON_THE_WAY", workerId, orderId);
        return workerProfileMapper.toResponse(savedProfile);
    }

    public WorkerProfileResponse startOrder(Long workerId, Long orderId)
    {
        WorkerProfile profile = workerProfileRepository.findById(workerId)
                .orElseThrow(() -> new EntityNotFoundException("Worker profile not found with id" + workerId));

        if (profile.getStatus() != WorkerStatus.ON_THE_WAY) {
            throw new RuntimeException("Worker status: (" +profile.getStatus()+ ") is not ready for this order");
        }

        try
        {
            orderClient.startOrder(orderId,workerId);
        } catch (feign.FeignException e)
        { // Bắt chính xác FeignException
            log.error("Lỗi gửi từ Order Service: " + e.contentUTF8());
            throw new RuntimeException("Order service trả về lỗi: " + e.contentUTF8());
        } catch (Exception e) {
            log.error("Lỗi kết nối mạng: ", e);
            throw new RuntimeException("Unable to claim order, please try again");
        }
        WorkerProfile savedProfile = saveWorkerStatus(profile, WorkerStatus.BUSY);
        log.info("Worker starts order", workerId, orderId);
        return workerProfileMapper.toResponse(savedProfile);
    }

    public WorkerProfileResponse completeOrder(Long workerId, Long orderId)
    {
        WorkerProfile profile = workerProfileRepository.findById(workerId)
                .orElseThrow(() -> new EntityNotFoundException("Worker profile not found with id" + workerId));
        if (profile.getStatus() != WorkerStatus.BUSY)
        {
            throw new RuntimeException("Worker status is not suitable to complete order");
        }

        try
        {
            orderClient.completeOrder(orderId, workerId);
        } catch (Exception e)
        {
            log.error("Can not call OrderService to claim order", e);
            throw new RuntimeException("Unable to complete order, please try again");
        }
        WorkerProfile savedProfile = saveWorkerCompletion(profile);

        log.info("Worker {} have done order: {}, change status to AVAILABLE", workerId, orderId);
        return workerProfileMapper.toResponse(savedProfile);
    }

    @Transactional
    public WorkerProfile saveWorkerStatus(WorkerProfile profile, WorkerStatus status)
    {
        profile.setStatus(status);
        return workerProfileRepository.save(profile);
    }

    @Transactional
    public WorkerProfile saveWorkerCompletion(WorkerProfile profile)
    {
        profile.setStatus(WorkerStatus.AVAILABLE);
        profile.setTotalJobs(profile.getTotalJobs() + 1);
        return workerProfileRepository.save(profile);
    }
}

