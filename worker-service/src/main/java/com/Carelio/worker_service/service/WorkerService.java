package com.Carelio.worker_service.service;

import com.Carelio.worker_service.dto.request.WorkerProfileRequest;
import com.Carelio.worker_service.dto.response.WorkerProfileResponse;
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

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional(readOnly = true)
public class WorkerService
{
    private final WorkerProfileRepository workerProfileRepository;
    private final WorkerSkillRepository workerSkillRepository;
    private final ServiceSkillRepository serviceSkillRepository;
    private final WorkerProfileMapper workerProfileMapper;
    public WorkerProfileResponse createWorkerProfile(Long userId, WorkerProfileRequest workerProfileRequest)
    {
        if(workerProfileRepository.existsById(userId))
        {
            throw new RuntimeException("User already has worker profile");
        }
        WorkerProfile profile = workerProfileMapper.toEntity(workerProfileRequest);
        profile.setUserId(userId);
        WorkerProfile savedProfile =  workerProfileRepository.save(profile);
        log.info("Worker profile {} created successfully ",  savedProfile.getId());
        return workerProfileMapper.toResponse(savedProfile);
    }

    public WorkerProfileResponse getById(Long id)
    {
        WorkerProfile workerProfile = workerProfileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Worker profile with id " + id + " not found"));
        return workerProfileMapper.toResponse(workerProfile);
    }
    public List<WorkerProfileResponse> getAllWorkerProfiles()
    {
        List<WorkerProfile> profileList = workerProfileRepository.findAll();
        log.info("Found {} profiles", profileList.size());
        return  workerProfileMapper.toResponseList(profileList);
    }
}
