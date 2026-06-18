package com.Carelio.service_order_service.repository;


import com.Carelio.service_order_service.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>
{

    Order findByIdAndUserId(Long orderId, Long userId);

    List<Order> findAllByUserId(Long userId);
}