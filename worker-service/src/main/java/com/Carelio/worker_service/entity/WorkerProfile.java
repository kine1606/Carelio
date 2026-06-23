package com.Carelio.worker_service.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "worker_profile")
public class WorkerProfile
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    private Integer totalJobs;
    private Double ratingAvg;
    private String bio;
    private WorkerStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
//
//    private boolean isDeleted = false;

    @PrePersist
    public void prePersist() {
        if (totalJobs == null) totalJobs = 0;
        if (ratingAvg == null) ratingAvg = 0.0;
        if (status == null) status = WorkerStatus.AVAILABLE;

        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}