package com.Carelio.service_order_service.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "order_reviews",
        uniqueConstraints = @UniqueConstraint(columnNames = "order_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderReview

{
    // 1 order get reviewed once
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column(nullable = false)
    private Long orderId;
//    @Column(nullable = false)
    private Long userId;
//    @Column(nullable = false)
    private Long workerId;

    @Range(min = 1, max = 5, message = "Rating has to be in range from 1 to 5")
    private Integer rating;
    private String comment;

    @CreatedDate
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist()
    {
        createdAt = LocalDateTime.now();
    }
}
