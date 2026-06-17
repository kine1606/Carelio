package com.Carelio.household_service.repository;

import com.Carelio.household_service.entity.House;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HouseRepository extends JpaRepository<House, Long>
{
    List<House> findByUserId(Long userId);

    Optional<House> findByIdAndUserId(Long houseId, Long userId);
}
