package com.Carelio.service_order_service.service;

import com.Carelio.service_order_service.client.WorkerClient;
import com.Carelio.service_order_service.client.dto.WorkerProfileResponse;
import com.Carelio.service_order_service.dto.request.OrderAttachmentRequest;
import com.Carelio.service_order_service.dto.request.UpdateAttachmentRequest;
import com.Carelio.service_order_service.dto.response.OrderAttachmentResponse;
import com.Carelio.service_order_service.entity.Order;
import com.Carelio.service_order_service.entity.OrderAttachment;
import com.Carelio.service_order_service.mapper.OrderAttachmentMapper;
import com.Carelio.service_order_service.repository.OrderAttachmentRepository;
import com.Carelio.service_order_service.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class OrderAttachmentService {

    private final OrderAttachmentMapper orderAttachmentMapper;
    private final OrderRepository orderRepository;
    private final OrderAttachmentRepository orderAttachmentRepository;

    private final WorkerClient workerClient;
    private Order getOrderEntity(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));
    }

    private void validateOrderAccess(Order order, String userId) {
        if (order.getUserId().equals(userId))
        {
            return;
        }

        if (order.getWorkerId() != null)
        {
            try {
                WorkerProfileResponse workerProfile = workerClient.getWorkerByKeycloakId(userId);

                if (workerProfile.getId().equals(order.getWorkerId())) {
                    return;
                }
            } catch (Exception e) {
                log.error("Lỗi khi xác thực thông tin Thợ từ Worker Service", e);
            }
        }
        log.warn("CẢNH BÁO BẢO MẬT: Người dùng {} không có quyền truy cập đơn hàng {}", userId, order.getId());
        throw new RuntimeException("Bạn không có quyền truy cập dữ liệu của đơn hàng này!");
    }

    // POST /api/service-orders/{orderId}/attachments
    @Transactional
    @CacheEvict(value = "ORDER_ATTACHMENTS_LIST_CACHE", key = "#orderId")
    public OrderAttachmentResponse createAttachment(String userId, Long orderId, OrderAttachmentRequest request) {
        Order order = getOrderEntity(orderId);

        validateOrderAccess(order, userId);

        OrderAttachment attachment = OrderAttachment.builder()
                .fileUrl(request.getFileUrl())
                .fileType(request.getFileType())
                .uploadedBy(userId)
                .orderId(orderId)
                .build();

        OrderAttachment saved = orderAttachmentRepository.save(attachment);
        log.info("User {} uploaded attachment successfully for order: {}", userId, orderId);
        return orderAttachmentMapper.toResponse(saved);
    }

    // GET /api/service-orders/{orderId}/attachments
    @Cacheable(value = "ORDER_ATTACHMENTS_LIST_CACHE", key = "#orderId")
    public List<OrderAttachmentResponse> getAttachments(String userId, Long orderId) {
        Order order = getOrderEntity(orderId);

        validateOrderAccess(order, userId);

        List<OrderAttachment> attachments = orderAttachmentRepository.findAllByOrderId(orderId);
        log.info("Found {} attachments for orderId: {}", attachments.size(), orderId);
        return orderAttachmentMapper.toResponseList(attachments);
    }

    // GET /api/service-orders/{orderId}/attachments/{attachmentId}
    @Cacheable(value = "SINGLE_ATTACHMENT_CACHE", key = "#attachmentId")
    public OrderAttachmentResponse getAttachmentById(String userId, Long orderId, Long attachementId)
    {
        Order order = getOrderEntity(orderId);

        validateOrderAccess(order, userId);

        OrderAttachment attachment = orderAttachmentRepository.findById(attachementId)
                .orElseThrow(() -> new EntityNotFoundException("Attachment not found with id: " + attachementId));
        return orderAttachmentMapper.toResponse(attachment);
    }

    //PATCH /api/service-orders/{orderId}/attachments/{attachmentId}

    @Transactional
    @CacheEvict(value = {"ORDER_ATTACHMENTS_LIST_CACHE", "SINGLE_ATTACHMENT_CACHE"}, key = "#orderId", beforeInvocation = false)
    public OrderAttachmentResponse updateAttachment(String userId, Long orderId, Long attachmentId, UpdateAttachmentRequest request)
    {
        Order order = getOrderEntity(orderId);
        validateOrderAccess(order, userId);
        OrderAttachment attachment = orderAttachmentRepository.findByIdAndOrderId(attachmentId, orderId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Attachment not found with id: " + attachmentId + " for orderId: " + orderId));
        if(request.getFileType() != null)
            attachment.setFileType(request.getFileType());
        if(request.getFileUrl() != null)
            attachment.setFileUrl(request.getFileUrl());
        if(request.getUploadedBy() != null)
            attachment.setUploadedBy(userId);
        OrderAttachment saved =  orderAttachmentRepository.save(attachment);
        log.info("Attachment {} updated successfully by user {}", attachmentId, userId);
        return  orderAttachmentMapper.toResponse(saved);
    }

    // DELETE /api/service-orders/{orderId}/attachments/{attachmentId}
    @Transactional
    @CacheEvict(value = {"ORDER_ATTACHMENTS_LIST_CACHE", "SINGLE_ATTACHMENT_CACHE"}, key = "#orderId")
    public void deleteAttachment(String userId, Long orderId, Long attachmentId)
    {
        Order order = getOrderEntity(orderId);

        validateOrderAccess(order, userId);

        OrderAttachment attachment = orderAttachmentRepository.findByIdAndOrderId(attachmentId, orderId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Attachment not found with id: " + attachmentId + " for orderId: " + orderId));

        if (!attachment.getUploadedBy().equals(userId)) {
            throw new RuntimeException("Bạn không thể xóa tệp đính kèm do người khác tải lên!");
        }

        orderAttachmentRepository.delete(attachment);
        log.info("Attachment {} deleted successfully by user {}", attachmentId, userId);
    }
}