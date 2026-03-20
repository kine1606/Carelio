package com.amigoscode.carelio.room.entity;

import com.amigoscode.carelio.equipment.entity.Equipment;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "room")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room
{
    @Id
    @GeneratedValue
    private Long id;

    @Column (nullable = false)
    private String name;

    private String description;

    @Column(nullable = true)
    private Integer floor;

    @CreatedDate
    private Date createdAt;
    @UpdateTimestamp
    private Date updatedAt;

    @OneToMany(mappedBy = "room")
    List<Equipment> equipments;
}
