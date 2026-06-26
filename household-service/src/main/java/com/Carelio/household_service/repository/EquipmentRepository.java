package com.Carelio.household_service.repository;


import com.Carelio.household_service.entity.Equipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long>
{

    Optional<Equipment> findByIdAndRoom_House_UserId(Long id, String userId);

    List<Equipment> findByRoom_Id(Long id);

    Optional<Equipment> findByIdAndRoom_Id(Long equipmentId, Long roomId);

    List<Equipment> findAllByIsDeletedFalseAndRoom_House_UserId(String userId);
    Page<Equipment> findAllByIsDeletedFalseAndRoom_House_UserId(String userId, Pageable pageable);

}