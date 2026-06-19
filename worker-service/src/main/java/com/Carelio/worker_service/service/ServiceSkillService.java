package com.Carelio.worker_service.service;

import com.Carelio.worker_service.dto.request.ServiceSkillRequest;
import com.Carelio.worker_service.dto.response.ServiceSkillResponse;
import com.Carelio.worker_service.entity.ServiceSkill;
import com.Carelio.worker_service.mapper.ServiceSkillMapper;
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
public class ServiceSkillService
{
    private final WorkerProfileRepository workerProfileRepository;
    private final WorkerSkillRepository workerSkillRepository;
    private final ServiceSkillRepository serviceSkillRepository;
    private final ServiceSkillMapper serviceSkillMapper;
    public ServiceSkillResponse createServiceSkill(ServiceSkillRequest req)
    {
        if(serviceSkillRepository.existsByServiceSkillCode(req.getServiceSkillCode()))
        {
            throw new RuntimeException(req.getServiceSkillCode() + "{} already exists" );
        }
        ServiceSkill serviceSkill = serviceSkillMapper.toEntity(req);
        return serviceSkillMapper.toResponse(serviceSkill);
    }

    public List<ServiceSkillResponse> getServiceSkills()
    {
        List<ServiceSkill> serviceSkills = serviceSkillRepository.findAll();
        log.info("Found {} serviceSkills", serviceSkills.size());
        return serviceSkillMapper.toResponseList(serviceSkills);
    }
    public ServiceSkillResponse getServiceSkillById(Long serviceSkillId)
    {
        ServiceSkill skill= serviceSkillRepository.findById(serviceSkillId)
                .orElseThrow(() -> new EntityNotFoundException("Service Skill not found with id: " + serviceSkillId));
        return  serviceSkillMapper.toResponse(skill);
    }
}
