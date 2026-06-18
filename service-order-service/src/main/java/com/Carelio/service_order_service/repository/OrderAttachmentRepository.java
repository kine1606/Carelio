package com.Carelio.service_order_service.repository;

import com.Carelio.service_order_service.entity.OrderAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderAttachmentRepository extends JpaRepository<OrderAttachment, Long>
{
    List<OrderAttachment> findAllByOrderId(Long orderId);
    Optional<OrderAttachment> findByIdAndOrderId(Long id, Long orderId);
}
