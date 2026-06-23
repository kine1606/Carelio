package com.Carelio.payment_service.repository;

import aj.org.objectweb.asm.commons.Remapper;
import com.Carelio.payment_service.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    boolean existsByOrderIdAndStatus(Long id, String success);

    Remapper findTopByOrderIdOrderByIdDesc(Long orderId);
}
