package com.Carelio.service_order_service.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(
        name = "price_catalogs",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"equipmentCategoryId", "serviceSkillId"})
        }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PriceCatalog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long equipmentCategoryId;

    @Column(nullable = false)
    private Long serviceSkillId;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;
}