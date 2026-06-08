package com.amigoscode.carelio.equipment.repository;


import com.amigoscode.carelio.equipment.entity.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long>
{
    List<Equipment> findAllByDeletedFalse();
}