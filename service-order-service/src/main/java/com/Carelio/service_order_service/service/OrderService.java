package com.Carelio.service_order_service.service;

import com.Carelio.service_order_service.client.HouseholdClient;
import com.Carelio.service_order_service.client.WorkerClient;
import com.Carelio.service_order_service.client.dto.EquipmentValidationResponse;
import com.Carelio.service_order_service.client.dto.ServiceSkillResponse;
import com.Carelio.service_order_service.dto.request.*;
import com.Carelio.service_order_service.dto.response.OrderAttachmentResponse;
import com.Carelio.service_order_service.dto.response.OrderResponse;
import com.Carelio.service_order_service.dto.response.OrderReviewResponse;
import com.Carelio.service_order_service.entity.Order;
import com.Carelio.service_order_service.entity.OrderAttachment;
import com.Carelio.service_order_service.entity.OrderReview;
import com.Carelio.service_order_service.entity.ServiceOrderStatus;
import com.Carelio.service_order_service.mapper.OrderAttachmentMapper;
import com.Carelio.service_order_service.mapper.OrderMapper;
import com.Carelio.service_order_service.mapper.OrderReviewMapper;
import com.Carelio.service_order_service.repository.OrderAttachmentRepository;
import com.Carelio.service_order_service.repository.OrderRepository;
import com.Carelio.service_order_service.repository.OrderReviewRepository;
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
public class OrderService
{
    private final OrderMapper orderMapper;
    private final OrderAttachmentMapper orderAttachmentMapper;
    private final OrderReviewMapper orderReviewMapper;
    private final OrderRepository orderRepository;
    private final OrderAttachmentRepository orderAttachmentRepository;
    private final OrderReviewRepository orderReviewRepository;
    private final HouseholdClient householdClient;
    private final WorkerClient workerClient;

    //===========================================CRUD==================================================
    //GET /api/orders/{id}
    public OrderResponse getById(Long userId, Long orderId)
    {
        Order order = getOrderEntity(userId, orderId);
        log.info("Order found with id: {} and userId: {}", orderId, userId);
        return orderMapper.toResponse(order);
    }

    //GET /api/orders
    public List<OrderResponse> getAll(Long userId)
    {
        List<Order> orders = orderRepository.findAllByUserId(userId);
        log.info("Found {} orders", orders.size());

        return orderMapper.toResponseList(orders);
    }
    //POST /api/orders
    public OrderResponse createOrder(Long userId, OrderRequest request)
    {
        EquipmentValidationResponse evResponse = householdClient.validate(
                userId,
                request.getEquipmentId(),
                request.getRoomId(),
                request.getHouseId()
        );

        ServiceSkillResponse ssResponse =  workerClient.getServiceSkill(
                request.getServiceSkillId()
        );
        Order order = orderMapper.toEntity(request, userId,evResponse, ssResponse);
        Order saved =  orderRepository.save(order);
        log.info("Order created successfully: {}", order);

        return orderMapper.toResponse(saved);
    }

    //DELETE /api/orders/{id}
    public OrderResponse deleteOrder(Long userId, Long orderId)
    {
        Order order = getOrderEntity(userId, orderId);
        order.setStatus(ServiceOrderStatus.CANCELLED);
        Order saved =  orderRepository.save(order);
        log.info("Order deleted successfully: {}", order);

        return orderMapper.toResponse(saved);
    }

    //PATCH /api/orders/{id}
    public OrderResponse updateOrder(Long userId, Long orderId, UpdateOrderRequest request)
    {
        Order order = getOrderEntity(userId, orderId);
        order.setTitle(request.getTitle());
        order.setDescription(request.getDescription());
        order.setScheduledAt(request.getScheduledTime());

        Order saved =  orderRepository.save(order);
        log.info("Order updated successfully: {}", order);
        return orderMapper.toResponse(saved);
    }

    private Order getOrderEntity(Long userId, Long orderId)
    {
        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));
        return order;
    }
    //===========================================CRUD==================================================
    //PATCH /api/orders/{id}/assign?workerId={workerid}
//    public OrderResponse assignWorker(Long userId, Long orderId, Long workerId)
//    {
//    }
    //PATCH /api/orders/{id}/status
    public OrderResponse updateStatus(Long userId, Long orderId, UpdateOrderStatusRequest request)
    {
        Order order = getOrderEntity(userId, orderId);
        order.setStatus(request.getStatus());
        Order saved =  orderRepository.save(order);
        log.info("Order updated status successfully: {}", order);
        return  orderMapper.toResponse(saved);
    }

//===========================================Order Attachment==================================================
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
//===========================================Order Review==================================================

    //POST /api/orders/{orderId}/reviews
    public OrderReviewResponse createReview(Long userId, Long orderId, OrderReviewRequest request)
    {
        Order order = getOrderEntity(userId, orderId);
        if (order.getStatus() != ServiceOrderStatus.COMPLETED) {
            throw new IllegalStateException("Order must be COMPLETED before submitting a review.");
        }
        if (orderReviewRepository.findByOrderIdAndUserId(orderId, userId).isPresent()) {
            throw new IllegalStateException("You have already reviewed this order.");
        }
        OrderReview review = OrderReview.builder()
                .orderId(orderId)
                .userId(userId)
                .workerId(order.getWorkerId())
                .rating(request.getRating())
                .comment(request.getComment())
                .build();
        OrderReview saved = orderReviewRepository.save(review);

        // update RatingAvg of worker after done reviewing
        workerClient.updateRating(review.getWorkerId(), review.getRating());
        log.info("Review created successfully for orderId: {}", orderId);
        return orderReviewMapper.toResponse(saved);
    }

    //GET /api/orders/{orderId}/reviews
    public List<OrderReviewResponse> getReviews(Long userId, Long orderId)
    {
        getOrderEntity(userId, orderId);
        List<OrderReview> reviews = orderReviewRepository.findAllByOrderId(orderId);
        log.info("Found {} reviews for orderId: {}", reviews.size(), orderId);
        return orderReviewMapper.toResponseList(reviews);
    }
}