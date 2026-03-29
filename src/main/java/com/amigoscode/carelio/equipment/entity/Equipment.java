package com.amigoscode.carelio.equipment.entity;

import com.amigoscode.carelio.base.BaseEntity;
import com.amigoscode.carelio.room.entity.Room;
import com.amigoscode.carelio.serviceOrder.entity.ServiceOrder;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @ManyToOne
    @JoinColumn(name = "equipment_category_id", nullable = false)
    private EquipmentCategory equipmentCategory;

    @OneToMany(mappedBy = "equipment")
    private List<ServiceOrder> serviceOrders;
}
