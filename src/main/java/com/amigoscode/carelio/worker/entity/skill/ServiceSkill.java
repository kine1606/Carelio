package com.amigoscode.carelio.worker.entity.skill;

import com.amigoscode.carelio.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Table(name = "service_skill")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ServiceSkill extends BaseEntity
{
    @Enumerated(EnumType.STRING)
    private ServiceSkillCode serviceSkillCode;

    @OneToMany(mappedBy = "serviceSkill")
    private Set<ServiceSkill> serviceSkills;
}
