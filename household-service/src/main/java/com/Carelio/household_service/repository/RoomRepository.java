package com.Carelio.household_service.repository;


import com.Carelio.household_service.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long>
{
    Optional<Room> findByIdAndOwnerId(Long id, Long ownerId);
    boolean existsByIdAndOwnerId(Long id, Long ownerId);
//    List<Room> findByOwnerIdAndByDeletedFalse(Long ownerId);

    List<Room> findByOwnerIdAndDeletedFalse(Long ownerId);

    List<Room> findByOwnerId(Long ownerId);
}