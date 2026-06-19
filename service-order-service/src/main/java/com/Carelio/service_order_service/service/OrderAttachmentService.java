package com.Carelio.service_order_service.service;

import com.Carelio.service_order_service.dto.request.OrderAttachmentRequest;
import com.Carelio.service_order_service.dto.response.OrderAttachmentResponse;
import com.Carelio.service_order_service.entity.Order;
import com.Carelio.service_order_service.entity.OrderAttachment;
import com.Carelio.service_order_service.mapper.OrderAttachmentMapper;
import com.Carelio.service_order_service.repository.OrderAttachmentRepository;
import com.Carelio.service_order_service.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class OrderAttachmentService
{
    private final OrderAttachmentMapper orderAttachmentMapper;
    private final OrderRepository orderRepository;
    private final OrderAttachmentRepository orderAttachmentRepository;
    private Order getOrderEntity(Long userId, Long orderId)
    {
        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));
        return order;
    }

    //POST /api/orders/{id}/attachments
    public OrderAttachmentResponse createAttachment(Long userId, Long orderId, OrderAttachmentRequest request)
    {
        getOrderEntity(userId, orderId);
        OrderAttachment attachment = OrderAttachment.builder()
                .fileUrl(request.getFileUrl())
                .fileType(request.getFileType())
                .uploadedBy(request.getUploadedBy())
                .orderId(orderId).build();
        OrderAttachment saved = orderAttachmentRepository.save(attachment);
        log.info("Attachment created successfully: {}", attachment.getId());
        return orderAttachmentMapper.toResponse(saved);
    }

    //GET /api/orders/{id}/attachments
    public List<OrderAttachmentResponse> getAttachments(Long userId, Long orderId)
    {
        // Verify the order belongs to this user
        getOrderEntity(userId, orderId);
        List<OrderAttachment> attachments = orderAttachmentRepository.findAllByOrderId(orderId);
        log.info("Found {} attachments for orderId: {}", attachments.size(), orderId);
        return orderAttachmentMapper.toResponseList(attachments);
    }

    //DELETE /api/orders/{id}/attachments/{attachmentId}
    public void deleteAttachment(Long userId, Long orderId, Long attachmentId)
    {
        // Verify the order belongs to this user
        getOrderEntity(userId, orderId);
        OrderAttachment attachment = orderAttachmentRepository.findByIdAndOrderId(attachmentId, orderId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Attachment not found with id: " + attachmentId + " for orderId: " + orderId));
        orderAttachmentRepository.delete(attachment);
        log.info("Attachment deleted successfully: {}", attachmentId);
    }
}
