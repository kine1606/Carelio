package com.Carelio.worker_service.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
//@Table(
//        name = "worker_skill",
//        uniqueConstraints = {
//                @UniqueConstraint(
//                name = "uk_worker_skill_combo",
//                columnNames = {"worker_id", "service_skill_id", "equipment_category_id"}
//        )})
@Table(name = "worker_skill")
@NoArgsConstructor
@AllArgsConstructor
public class WorkerSkill
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne
//    @JoinColumn(name = "worker_id", nullable = false)
//    private Worker worker;
//
//    @ManyToOne
//    @JoinColumn(name = "service_skill_id", nullable = false)
//    private ServiceSkill serviceSkill;
//
//    @ManyToOne
//    @JoinColumn(name = "equipment_category_id", nullable = false)
//    private EquipmentCategory equipmentCategory;

    private Long workerId;
    private Long serviceSkillId;
    private Long equipmentCategoryId;

    @Positive
    private Integer yearExperience;

    @Enumerated(EnumType.STRING)
    private SkillLevel skillLevel;

    @CreatedDate
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist()
    {
        if (skillLevel == null) skillLevel = SkillLevel.BEGINNER;
        createdAt = LocalDateTime.now();
    }
}
