package com.Carelio.worker_service.repository;

import com.Carelio.worker_service.entity.WorkerSkill;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkerSkillRepository extends JpaRepository<WorkerSkill, Long> {
    List<WorkerSkill> findByWorkerProfile_Id(Long id);

    boolean existsByWorkerProfile_IdAndServiceSkill_IdAndEquipmentCategoryId(Long workerId, @NotNull Long serviceSkillId, @NotNull Long equipmentCategoryId);

    boolean existsByWorkerProfile_IdAndEquipmentCategoryIdAndServiceSkill_Id(Long workerId, Long equipmentCategoryId, Long serviceSkillId);
}
