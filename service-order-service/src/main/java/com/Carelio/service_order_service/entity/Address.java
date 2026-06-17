package com.Carelio.service_order_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "addresses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column(nullable = false)
    private Long userId;
//    @Column(nullable = false, name = "contact_name")
    private String contactName;
//    @Column(nullable = false, name = "phone_number")
    private String phoneNumber;
//    @Column(nullable = false, name = "address_line")
    private String addressLine;
//    @Column(nullable = false)
//    private String city = "Vietnam";
    private boolean isDefault = false;

    @CreatedDate
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist()
    {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate()
    {
        updatedAt = LocalDateTime.now();
    }
}
