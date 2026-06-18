package com.Carelio.service_order_service.service;

import com.Carelio.service_order_service.client.HouseholdClient;
import com.Carelio.service_order_service.client.WorkerClient;
import com.Carelio.service_order_service.client.dto.EquipmentValidationResponse;
import com.Carelio.service_order_service.client.dto.ServiceSkillResponse;
import com.Carelio.service_order_service.dto.request.OrderRequest;
import com.Carelio.service_order_service.dto.response.OrderResponse;
import com.Carelio.service_order_service.entity.Order;
import com.Carelio.service_order_service.mapper.OrderMapper;
import com.Carelio.service_order_service.repository.OrderRepository;
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
    private final OrderRepository orderRepository;
    private final HouseholdClient householdClient;
    private final WorkerClient workerClient;

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

    //GET /api/orders/{id}
    public OrderResponse getById(Long userId, Long orderId)
    {
        Order order = orderRepository.findByIdAndUserId(orderId,userId);
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


}