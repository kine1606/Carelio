package com.Carelio.worker_service.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;
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
@Builder
public class WorkerSkill
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "worker_id", nullable = false)
    private WorkerProfile workerProfile;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "service_skill_id", nullable = false)
    private ServiceSkill serviceSkill;

    //cross-service
    // different service so can't use relationship @ManyToOne so we just take id of it.
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
