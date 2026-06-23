package com.Carelio.worker_service.service;

import com.Carelio.worker_service.dto.response.WorkerProfileResponse;
import com.Carelio.worker_service.entity.ServiceSkill;
import com.Carelio.worker_service.entity.ServiceSkillCode;
import com.Carelio.worker_service.entity.WorkerProfile;
import com.Carelio.worker_service.mapper.WorkerProfileMapper;
import com.Carelio.worker_service.repository.ServiceSkillRepository;
import com.Carelio.worker_service.repository.WorkerProfileRepository;
import com.Carelio.worker_service.repository.WorkerSkillRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class InternalWorkerService
{
    private final ServiceSkillRepository serviceSkillRepository;
    private final WorkerSkillRepository workerSkillRepository;
    private final WorkerProfileRepository workerProfileRepository;
    private final WorkerProfileMapper workerProfileMapper;
    public boolean canHandle(Long workerId, ServiceSkillCode serviceCode, Long categoryId)
    {
        ServiceSkill skill = serviceSkillRepository.findByServiceSkillCode(serviceCode)
                .orElseThrow(() -> new RuntimeException("Service skill not found"));

        return workerSkillRepository.existsByWorkerProfile_IdAndServiceSkill_IdAndEquipmentCategoryId(
                workerId,
                skill.getId(),
                categoryId
        );
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

}
