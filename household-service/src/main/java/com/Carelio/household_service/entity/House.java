package com.Carelio.household_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "equipment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class House
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String addressLine;

    @OneToMany(mappedBy="room_id", cascade = CascadeType.ALL)
    private Set<Room> rooms;
}



