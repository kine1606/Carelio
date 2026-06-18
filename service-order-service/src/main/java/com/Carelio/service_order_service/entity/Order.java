package com.Carelio.service_order_service.entity;

import com.Carelio.service_order_service.client.dto.ServiceSkillCode;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column(nullable = false)
    private Long userId;
    private Long workerId;        // worker (can be null at first time)


    //snapshot of equipment
    //    @Column(nullable = false)
    private Long houseId;
    private String houseAddressLine;
    //    @Column(nullable = false)
    private Long roomId;
    private String roomName;
//    @Column(nullable = false)
    private Long equipmentId;
    private String equipmentSerialNumber;
    private String equipmentBrand;
//    @Column(nullable = false)
    private Long equipmentCategoryId;
    private String equipmentCategoryName;
    //    @Column(nullable = false)
    private Long serviceSkillId;
    private ServiceSkillCode serviceSkillCode;

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
