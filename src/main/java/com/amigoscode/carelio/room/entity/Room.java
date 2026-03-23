package com.amigoscode.carelio.room.entity;

import com.amigoscode.carelio.base.BaseEntity;
import com.amigoscode.carelio.equipment.entity.Equipment;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "room")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room extends BaseEntity
{
    @Column(nullable = false)
    private String name;

    private String description;
    private Integer floor;

    @OneToMany(mappedBy = "room")
    List<Equipment> equipments;
}
