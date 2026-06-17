package com.Carelio.service_order_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "order_attachments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderAttachment
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //    @Column(nullable = false)
    private Long orderId;
    //    @Column(nullable = false)
    private String fileUrl;
    //    @Column(nullable = false)
    private String fileType;
    //    @Column(nullable = false)
    private UploadedByType uploadedBy;

    @CreatedDate
    private LocalDateTime createdAt;

    private boolean isDeleted = false;

    @PrePersist
    public void prePersist()
    {
        createdAt = LocalDateTime.now();
    }
}
