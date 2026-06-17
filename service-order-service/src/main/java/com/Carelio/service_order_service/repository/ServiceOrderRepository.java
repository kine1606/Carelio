package com.amigoscode.carelio.serviceOrder.repository;

import com.amigoscode.carelio.serviceOrder.entity.ServiceOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceOrderRepository extends JpaRepository<ServiceOrder, Long> {
    List<ServiceOrder> findAllByDeletedFalse();
}