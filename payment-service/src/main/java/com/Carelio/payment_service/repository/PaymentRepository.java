package com.Carelio.payment_service.repository;

import com.Carelio.payment_service.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    boolean existsByOrderIdAndStatus(Long id, String success);

    Optional<Payment> findTopByOrderIdOrderByIdDesc(Long orderId);
}
