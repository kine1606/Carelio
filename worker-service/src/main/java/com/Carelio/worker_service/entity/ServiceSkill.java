package com.Carelio.worker_service.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Table(name = "service_skill")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceSkill
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ServiceSkillCode serviceSkillCode;

    @OneToMany(mappedBy = "serviceSkill")
    private Set<WorkerSkill> workerSkills;
}
