package com.amigoscode.carelio.worker.entity;

import com.amigoscode.carelio.base.BaseEntity;
import com.amigoscode.carelio.payment.entity.PaymentMethod;
import com.amigoscode.carelio.payment.entity.PaymentStatus;
import com.amigoscode.carelio.payment.entity.PaymentType;
import com.amigoscode.carelio.service_order.entity.ServiceOrder;
import com.amigoscode.carelio.user.entity.user.User;
import com.amigoscode.carelio.worker.entity.skill.WorkerSkill;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "worker_profile")
public class Worker extends BaseEntity
{
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    private Long totalJobs;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkerStatus workerStatus;

    private String bio;

    @OneToMany(mappedBy = "worker", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<WorkerSkill> workerSkills;

    @OneToOne(mappedBy = "assignedWorker")
    private ServiceOrder serviceOrder;
}
