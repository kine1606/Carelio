package com.Carelio.service_order_service.repository;

import com.Carelio.service_order_service.entity.OrderReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderReviewRepository extends JpaRepository<OrderReview, Long>
{
    List<OrderReview> findAllByOrderId(Long orderId);
    Optional<OrderReview> findByOrderIdAndUserId(Long orderId, Long userId);
}
