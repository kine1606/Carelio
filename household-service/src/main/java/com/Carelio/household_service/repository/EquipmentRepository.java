package com.Carelio.household_service.repository;


import com.Carelio.household_service.entity.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long>
{
    Optional<Equipment> findByIdAndOwnerId(Long id, Long ownerId);

    List<Equipment> findAllByDeletedFalseAndOwnerId(Long ownerId);

    List<Equipment> findByRoomIdAndOwnerId(Long roomId, Long ownerId);
}