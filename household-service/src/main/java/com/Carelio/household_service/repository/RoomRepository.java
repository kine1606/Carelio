package com.Carelio.household_service.repository;


import com.Carelio.household_service.entity.Room;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long>
{
    boolean existsByIdAndHouse_UserId(Long roomId, Long userId);

    Optional<Room> findByIdAndHouse_UserId(@NotNull(message = "Room id is required") Long roomId, Long userId);

    List<Room> findByHouse_UserId(Long ownerId);
}