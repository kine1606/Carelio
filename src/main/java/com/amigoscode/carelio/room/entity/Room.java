package com.amigoscode.carelio.room.entity;

import com.amigoscode.carelio.base.BaseEntity;
import com.amigoscode.carelio.equipment.entity.Equipment;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;
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
