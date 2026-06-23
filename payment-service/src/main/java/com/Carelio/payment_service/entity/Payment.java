package com.Carelio.payment_service.entity;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long orderId;
    //    private Long userId;
    private BigDecimal amount;
    private String paymentMethod;
    private String status;
    private String transactionId;
    private String payUrl;
    private LocalDateTime paidAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist()
    {
//        this.status = this.status == null ? PaymentStatus.PENDING : this.status;
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        paidAt = LocalDateTime.now();
    }
}
