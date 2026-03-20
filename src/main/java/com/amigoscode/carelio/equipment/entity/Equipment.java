package com.amigoscode.carelio.equipment.entity;

import com.amigoscode.carelio.room.entity.Room;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "equipment")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Equipment
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String brand;
    private String model;
    private String serialNumber;

    @Enumerated(EnumType.STRING)
    private EquipmentStatus status;
    @Enumerated(EnumType.STRING)
    private EquipmentConditionStatus conditionStatus;

    @CreatedDate
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

//    @ManyToOne
//    @JoinColumn(name = "room_id")
//    private Room room;
}
