package com.Carelio.worker_service.entity;


import jakarta.persistence.*;
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
    private String equipmentCategoryName;

    private Integer yearExperience;

    @Enumerated(EnumType.STRING)
    private SkillLevel skillLevel;

    @CreatedDate
    private LocalDateTime createdAt;

    @PostUpdate
    public void postUpdate()
    {
        if(yearExperience <= 1)
            skillLevel = SkillLevel.BEGINNER;
        else if(yearExperience <= 3)
            skillLevel = SkillLevel.INTERMEDIATE;
        else skillLevel = SkillLevel.ADVANCED;
    }
    @PrePersist
    public void prePersist()
    {
        if(yearExperience > 2)
            skillLevel = SkillLevel.ADVANCED;
        else if (yearExperience <= 2 && yearExperience >= 1)
            skillLevel = SkillLevel.INTERMEDIATE;
        else
        {
            skillLevel = SkillLevel.BEGINNER;
            yearExperience = 1;
        }
        createdAt = LocalDateTime.now();
    }
}
