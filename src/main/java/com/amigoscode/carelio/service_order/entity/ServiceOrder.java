package com.amigoscode.carelio.service_order.entity;

import com.amigoscode.carelio.base.BaseEntity;
import com.amigoscode.carelio.user.entity.user.UserAddressInformation;
import com.amigoscode.carelio.worker.entity.Worker;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "service_order")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class ServiceOrder extends BaseEntity
{

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "service_order_status", nullable = false)
    private ServiceOrderStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "service_type", nullable = false)
    private ServiceType serviceType;

    @OneToOne
    @JoinColumn(name = "user_address_information_id")
    private UserAddressInformation userAddressInformation;

    @OneToOne
    @JoinColumn(name = "assigned_worker_id")
    private Worker assignedWorker;
}
