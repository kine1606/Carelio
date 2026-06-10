package com.Carelio.worker_service.service;

import com.Carelio.worker_service.entity.ServiceSkill;
import com.Carelio.worker_service.entity.ServiceSkillCode;
import com.Carelio.worker_service.repository.ServiceSkillRepository;
import com.Carelio.worker_service.repository.WorkerSkillRepository;
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
}
