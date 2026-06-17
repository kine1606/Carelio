package com.Carelio.household_service.repository;


import com.Carelio.household_service.entity.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long>
{
    List<Equipment> findByRoom_IdAndRoom_House_UserId(Long roomId, Long userId);

    List<Equipment> findAllByDeletedFalseAndRoom_House_UserId(Long userId);

    Optional<Equipment> findByIdAndRoom_House_UserId(Long id, Long userId);

    List<Equipment> findByRoom_Id(Long id);
}