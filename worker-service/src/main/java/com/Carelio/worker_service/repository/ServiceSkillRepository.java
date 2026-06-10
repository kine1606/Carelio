package com.Carelio.worker_service.repository;

import com.Carelio.worker_service.entity.ServiceSkill;
import com.Carelio.worker_service.entity.ServiceSkillCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceSkillRepository extends JpaRepository<ServiceSkill, Long>
{
    Optional<ServiceSkill> findByServiceSkillCode(ServiceSkillCode serviceSkillCode);

    boolean existsByServiceSkillCode(ServiceSkillCode serviceSkillCode);
}
