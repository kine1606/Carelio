package com.Carelio.household_service.repository;


import com.Carelio.household_service.entity.Room;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long>
{
    boolean existsByIdAndHouse_UserId(Long roomId, String userId);

    Optional<Room> findByIdAndHouse_UserId(@NotNull(message = "Room id is required") Long roomId, String userId);

    List<Room> findByHouse_UserId(String ownerId);

    Page<Room> findByHouse_UserId(String ownerId, Pageable pageable);

    Optional<Room> findByIdAndHouse_Id(Long roomId,  Long  houseId);

    List<Room> findByHouse_UserIdAndIsDeletedFalse(String userId);

//    Object findBySubmittingMethodOrSimilar(String userId);
}