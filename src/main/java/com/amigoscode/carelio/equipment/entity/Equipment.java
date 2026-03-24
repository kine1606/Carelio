package com.amigoscode.carelio.equipment.entity;

import com.amigoscode.carelio.base.BaseEntity;
import com.amigoscode.carelio.room.entity.Room;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "equipment")
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Equipment extends BaseEntity
{

    @Column(nullable = false)
    private String name;

    private String brand;
    private String model;
    private String serialNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EquipmentStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EquipmentConditionStatus conditionStatus;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @ManyToOne
    @JoinColumn(name = "equipment_category_id", nullable = false)
    private EquipmentCategory equipmentCategory;
}
