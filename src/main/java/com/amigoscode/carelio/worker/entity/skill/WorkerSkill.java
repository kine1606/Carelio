package com.amigoscode.carelio.worker.entity.skill;

import com.amigoscode.carelio.base.BaseEntity;
import com.amigoscode.carelio.equipment.entity.EquipmentCategory;
import com.amigoscode.carelio.worker.entity.Worker;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
        name = "worker_skill",
        uniqueConstraints = {
                @UniqueConstraint(
                name = "uk_worker_skill_combo",
                columnNames = {"worker_id", "service_skill_id", "equipment_category_id"}
        )})
@NoArgsConstructor
@AllArgsConstructor
public class WorkerSkill extends BaseEntity
{
    @ManyToOne
    @JoinColumn(name = "worker_id", nullable = false)
    private Worker worker;

    @ManyToOne
    @JoinColumn(name = "service_skill_id", nullable = false)
    private ServiceSkill serviceSkill;

    @ManyToOne
    @JoinColumn(name = "equipment_category_id", nullable = false)
    private EquipmentCategory equipmentCategory;

    private Integer yearExperience;
    @Enumerated(EnumType.STRING)
    private SkillLevel skillLevel;
}
