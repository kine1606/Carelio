package com.amigoscode.carelio.equipment.repository;

import com.amigoscode.carelio.equipment.entity.EquipmentCategory;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipmentCategoryRepository extends JpaRepository<EquipmentCategory, Long> { }