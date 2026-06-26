package com.Carelio.worker_service.service;

import com.Carelio.worker_service.client.HouseholdClient;
import com.Carelio.worker_service.client.OrderClient;
import com.Carelio.worker_service.client.dto.CategoryResponse;
import com.Carelio.worker_service.client.dto.OrderResponse;
import com.Carelio.worker_service.dto.request.UpdateWorkerProfileRequest;
import com.Carelio.worker_service.dto.request.UpdateWorkerSkillRequest;
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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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
    @CachePut(value = "WORKER_PROFILE_CACHE", key = "#userId")
    public WorkerProfileResponse createWorkerProfile(String userId, WorkerProfileRequest workerProfileRequest)
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
    @Cacheable(value = "WORKER_PROFILE_CACHE", key = "#id")
    public WorkerProfileResponse getById(Long id)
    {
        WorkerProfile workerProfile = workerProfileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Worker profile with id " + id + " not found"));
        return workerProfileMapper.toResponse(workerProfile);
    }

    @Cacheable(value = "WORKER_PROFILE_CACHE", key = "#userId")
    public WorkerProfileResponse getByKeyCloakUserId(String userId)
    {
        WorkerProfile workerProfile = workerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Worker profile with userId " + userId + " not found"));
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
    @CacheEvict(value = "WORKER_SKILLS_LIST_CACHE", key = "#userId")
    public WorkerSkillResponse addWorkerSkill(String userId, WorkerSkillRequest request)
    {
        WorkerProfile profile = workerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Worker profile with id " + userId + " not found"));
        ServiceSkill skill = serviceSkillRepository.findById(request.getServiceSkillId())
                .orElseThrow(() -> new EntityNotFoundException("Skill with id " + request.getServiceSkillId() + " not found"));
        CategoryResponse categoryResponse = householdClient.getCategoryById(request.getEquipmentCategoryId());

        Long workerId = profile.getId();
        boolean exists = workerSkillRepository.existsByWorkerProfile_IdAndServiceSkill_IdAndEquipmentCategoryId
                (
                        workerId,
                        request.getServiceSkillId(),
                        request.getEquipmentCategoryId()
                );
        if (exists) {
            throw new RuntimeException("WorkerSkill already exists");
        }
        WorkerSkill workerSkill = WorkerSkill.builder()
                .workerProfile(profile)
                .serviceSkill(skill)
                .equipmentCategoryId(categoryResponse.getId())
                .equipmentCategoryName(categoryResponse.getName())
                .yearExperience(request.getYearExperience())
                .build();
        WorkerSkill savedSkill = workerSkillRepository.save(workerSkill);
        log.info("Worker skill {} created successfully ", savedSkill.getId());
        return workerSkillMapper.toResponse(savedSkill);
    }
    @Cacheable(value = "WORKER_SKILLS_LIST_CACHE", key = "#userId")
    public List<WorkerSkillResponse> getWorkerSkills(Long workerId)
    {
        WorkerProfile profile = workerProfileRepository.findById(workerId)
                .orElseThrow(() -> new EntityNotFoundException("Worker profile with id " + workerId + " not found"));
        List<WorkerSkill> skills = workerSkillRepository.findByWorkerProfile_Id(profile.getId());
        log.info("Found {} skills", skills.size());
        return workerSkillMapper.toResponseList(skills);
    }

    @Transactional
    @CachePut(value = "WORKER_PROFILE_CACHE", key = "#userId")
    public WorkerProfileResponse acceptOrder(String userId, Long orderId)
    {
        WorkerProfile profile = workerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Worker profile not found with userId " + userId));

        if (profile.getStatus() != WorkerStatus.AVAILABLE) {
            throw new RuntimeException("Worker is not available");
        }

        OrderResponse orderDetail;
        try {
            orderDetail = orderClient.getOrder(orderId);
        } catch (Exception e) {
            log.error("Unavailable to take order information from orderService to validate skill ", e);
            throw new RuntimeException("System interrupted, try again");
        }

        Long workerId = profile.getId();
        boolean hasRequiredSkill = workerSkillRepository
                .existsByWorkerProfile_IdAndEquipmentCategoryIdAndServiceSkill_Id(
                        workerId,
                        orderDetail.getEquipmentCategoryId(),
                        orderDetail.getServiceSkillId()
                );

        if (!hasRequiredSkill) {
            throw new RuntimeException("Worker does not have required skill: " + orderDetail.getServiceSkillId());
        }
        try {
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

    @Transactional
    @CachePut(value = "WORKER_PROFILE_CACHE", key = "#userId")
    public WorkerProfileResponse startOrder(String userId, Long orderId)
    {
        WorkerProfile profile = workerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Worker profile not found with UserId" + userId));

        if (profile.getStatus() != WorkerStatus.ON_THE_WAY) {
            throw new RuntimeException("Worker status: (" + profile.getStatus() + ") is not ready for this order");
        }

        Long workerId = profile.getId();
        try {
            orderClient.startOrder(orderId, workerId);
        } catch (feign.FeignException e) { // Bắt chính xác FeignException
            log.error("Lỗi gửi từ Order Service: " + e.contentUTF8());
            throw new RuntimeException("Order service trả về lỗi: " + e.contentUTF8());
        } catch (Exception e) {
            log.error("Lỗi kết nối mạng: ", e);
            throw new RuntimeException("Unable to claim order, please try again");
        }
        WorkerProfile savedProfile = saveWorkerStatus(profile, WorkerStatus.BUSY);
        log.info("Worker {} starts order {}", workerId, orderId);
        return workerProfileMapper.toResponse(savedProfile);
    }

    @Transactional
    @CachePut(value = "WORKER_PROFILE_CACHE", key = "#userId")
    public WorkerProfileResponse completeOrder(String userId, Long orderId)
    {
        WorkerProfile profile = workerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Worker profile not found with id" + userId));
        if (profile.getStatus() != WorkerStatus.BUSY) {
            throw new RuntimeException("Worker status is not suitable to complete order");
        }
        Long workerId = profile.getId();
        try {
            orderClient.completeOrder(orderId, workerId);
        } catch (Exception e) {
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

    @Transactional
    @CachePut(value="WORKER_PROFILE_CACHE", key = "#userId")
    public WorkerProfileResponse updateWorkerProfile(String userId, UpdateWorkerProfileRequest request)
    {
        WorkerProfile profile = workerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Worker profile not found with userId: " + userId));
        if (request.getBio() != null) profile.setBio(request.getBio());

        profile.setStatus(request.getStatus());
        WorkerProfile savedProfile = workerProfileRepository.save(profile);
        return workerProfileMapper.toResponse(savedProfile);
    }

    @Transactional
    @CacheEvict(value="WORKER_PROFILE_CACHE", key = "#userId")
    public void deleteWorkerProfile(String userId)
    {
        WorkerProfile profile = workerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Worker profile not found with userId: " + userId));
//        profile.setDeleted(true);
        profile.setStatus(WorkerStatus.INACTIVE);
        workerProfileRepository.save(profile);

        log.info("Worker profile of user {} has been soft-deleted successfully", userId);
    }

    @Transactional
    public WorkerSkillResponse updateWorkerSkill(String userId, Long skillId, UpdateWorkerSkillRequest request)
    {
        WorkerSkill workerSkill = workerSkillRepository.findById(skillId)
                .orElseThrow(() -> new EntityNotFoundException("Worker skill not found with id: " + skillId));

        if (!workerSkill.getWorkerProfile().getUserId().equals(userId)) {
            log.warn("Security alert: user {} try to adjust {} other's worker skill!", userId, skillId);
            throw new RuntimeException("You don't have permission to adjust this worker skill");
        }

        ServiceSkill skill = serviceSkillRepository.findById(request.getServiceSkillId())
                .orElseThrow(() -> new EntityNotFoundException("Skill with id " + request.getServiceSkillId() + " not found"));
        CategoryResponse categoryResponse = householdClient.getCategoryById(request.getEquipmentCategoryId());
        if (request.getYearExperience() != null && request.getYearExperience() >= 0) {
            workerSkill.setYearExperience(request.getYearExperience());
        }
        workerSkill.setServiceSkill(skill);
        workerSkill.setEquipmentCategoryId(categoryResponse.getId());
        workerSkill.setEquipmentCategoryName(categoryResponse.getName());
        WorkerSkill savedSkill = workerSkillRepository.save(workerSkill);
        return workerSkillMapper.toResponse(savedSkill);
    }

    @Transactional
    public void deleteWorkerSkill(String userId, Long skillId)
    {
        WorkerSkill skill = workerSkillRepository.findById(skillId)
                .orElseThrow(() -> new EntityNotFoundException("Worker skill not found with id: " + skillId));

        if (!skill.getWorkerProfile().getUserId().equals(userId)) {
            log.warn("Security alert: user {} try to delete {} other's worker skill!", userId, skillId);
            throw new RuntimeException("You don't have permission to delete this worker skill");
        }

        workerSkillRepository.delete(skill);
        log.info("Skill {} of worker {} has been deleted successfully", skillId, userId);
    }
}

