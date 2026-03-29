package com.amigoscode.carelio.service_order.entity;

import com.amigoscode.carelio.base.BaseEntity;
import com.amigoscode.carelio.equipment.entity.Equipment;
import com.amigoscode.carelio.user.entity.user.UserAddressInformation;
import com.amigoscode.carelio.worker.entity.Worker;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "service_order")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class ServiceOrder extends BaseEntity
{

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "service_order_status", nullable = false)
    private ServiceOrderStatus status;

    @ElementCollection(targetClass = ServiceType.class)
    @CollectionTable(
            name = "service_order_service_types",
            joinColumns = @JoinColumn(name = "service_order_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "service_type", nullable = false)
    private Set<ServiceType> serviceTypes = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_address_information_id")
    private UserAddressInformation userAddressInformation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_worker_id")
    private Worker assignedWorker;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_id", nullable = false)
    private Equipment equipment;

//    @ManyToOne
//    @JoinColumn(name = "workspace_id")
//    private Workspace workspace;
}
