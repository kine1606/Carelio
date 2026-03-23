package com.amigoscode.carelio.equipment.entity;

import com.amigoscode.carelio.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "equiment_category")
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
}
