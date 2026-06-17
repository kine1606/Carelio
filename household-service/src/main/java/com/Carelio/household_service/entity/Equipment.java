package com.Carelio.household_service.entity;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "equipment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Equipment
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    private Long userId;

    @Column(name = "name")
    private String name;

    @Column(name = "brand")
    private String brand;

    @Column(name = "model")
    private String model;

    @Column(name = "serial_number")
    private String serialNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private EquipmentStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "condition_status", nullable = false)
    private EquipmentConditionStatus conditionStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @ManyToOne
    @JoinColumn(name = "equipment_category_id", nullable = false)
    private EquipmentCategory equipmentCategory;

    @CreatedDate
//    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
    // soft delete
    private Boolean deleted = false;


    @PrePersist
    public void prePersist()
    {
        if(status == null)
            status = EquipmentStatus.ACTIVE;
        if(conditionStatus == null)
            conditionStatus = EquipmentConditionStatus.NORMAL;

        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate()
    {
        updatedAt = LocalDateTime.now();
    }
}
