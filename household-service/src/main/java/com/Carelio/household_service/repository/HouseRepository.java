package com.Carelio.household_service.repository;

import com.Carelio.household_service.entity.House;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HouseRepository extends JpaRepository<House, Long>
{
    List<House> findByUserId(String userId);

    Optional<House> findByIdAndUserId(Long houseId, String userId);

    List<House> findByUserIdAndIsDeletedFalse(String userId);
    Page<House> findByUserIdAndIsDeletedFalse(String userId,  Pageable pageable);
}
