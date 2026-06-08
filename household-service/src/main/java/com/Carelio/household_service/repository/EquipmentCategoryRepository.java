package com.Carelio.household_service.repository;


import com.Carelio.household_service.entity.EquipmentCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipmentCategoryRepository extends JpaRepository<EquipmentCategory, Long>
{ }