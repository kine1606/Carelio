package com.Carelio.service_order_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;          // customerId
    private Long workerId;        // worker (can be null at first time)

    //snapshot of equipment
    private Long roomId;
    private Long equipmentId;

    private Long serviceSkillId;       // REPAIR / CLEAN / MAINTAIN / INSTALLATION / PART_REPLACEMENT
    private Long equipmentCategoryId;

    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private ServiceOrderStatus status;

    private LocalDateTime scheduledAt;

    private BigDecimal price;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address address;

    @PrePersist
    public void prePersist()
    {
        if (status == null) {
            status = workerId == null ? ServiceOrderStatus.POSTED : ServiceOrderStatus.CLAIMED;
        }
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate()
    {
        updatedAt = LocalDateTime.now();
    }
}
