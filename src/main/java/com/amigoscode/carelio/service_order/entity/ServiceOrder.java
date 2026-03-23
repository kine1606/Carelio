package com.amigoscode.carelio.service_order.entity;

import com.amigoscode.carelio.base.BaseEntity;
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

    @Column(nullable = false)
    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServiceOrderStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServiceType serviceType;
}
