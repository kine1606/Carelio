package com.Carelio.worker_service.repository;


import com.Carelio.worker_service.entity.WorkerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkerProfileRepository extends JpaRepository<WorkerProfile, Long>
{
    boolean existsByUserId(String userId);

    Optional<WorkerProfile> findByUserId(String userId);
}
