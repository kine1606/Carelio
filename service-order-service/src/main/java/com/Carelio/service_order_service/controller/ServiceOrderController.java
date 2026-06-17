package com.amigoscode.carelio.serviceOrder.controller;

import com.amigoscode.carelio.serviceOrder.dto.CreateServiceOrderRequest;
import com.amigoscode.carelio.serviceOrder.dto.ServiceOrderResponse;
import com.amigoscode.carelio.serviceOrder.mapper.ServiceOrderMapper;
import com.amigoscode.carelio.serviceOrder.repository.ServiceOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.amigoscode.carelio.serviceOrder.entity.ServiceOrder;
import com.amigoscode.carelio.serviceOrder.service.ServiceOrderService;
import java.util.List;

@RestController
@RequestMapping("/service-orders")
@RequiredArgsConstructor
public class ServiceOrderController {

    private final ServiceOrderService serviceOrderService;
    private final ServiceOrderMapper serviceOrderMapper;
    @GetMapping
    public List<ServiceOrderResponse> getAll() {
        return serviceOrderMapper.toResponseList(serviceOrderService.getAll());
    }

    @GetMapping("/{id}")
    public ServiceOrderResponse getById(@PathVariable Long id) {
        return serviceOrderMapper.toResponse(serviceOrderService.getById(id));
    }

    @PostMapping
    public ServiceOrderResponse create(@RequestBody CreateServiceOrderRequest req) {
        return serviceOrderMapper.toResponse(serviceOrderService.create(req));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        serviceOrderService.delete(id);
    }
}
