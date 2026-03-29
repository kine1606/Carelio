package com.amigoscode.carelio.equipment.entity;

import com.amigoscode.carelio.base.BaseEntity;
import com.amigoscode.carelio.worker.entity.skill.WorkerSkill;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "equipment_category")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class EquipmentCategory extends BaseEntity
{
    private String name;

    @OneToMany(mappedBy = "equipmentCategory")
    private List<Equipment> equipments;

    @OneToMany(mappedBy = "equipmentCategory")
    private Set<WorkerSkill> workerSkills;
}
