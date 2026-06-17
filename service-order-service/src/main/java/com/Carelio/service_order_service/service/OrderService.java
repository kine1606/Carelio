package com.amigoscode.carelio.serviceOrder.service;

import com.amigoscode.carelio.equipment.entity.Equipment;
import com.amigoscode.carelio.equipment.service.EquipmentService;
import com.amigoscode.carelio.serviceOrder.dto.CreateServiceOrderRequest;
import com.amigoscode.carelio.serviceOrder.entity.ServiceOrderStatus;
import com.amigoscode.carelio.serviceOrder.mapper.ServiceOrderMapper;
import com.amigoscode.carelio.user.entity.user.UserAddressInformation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.amigoscode.carelio.serviceOrder.entity.ServiceOrder;
import com.amigoscode.carelio.serviceOrder.repository.ServiceOrderRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService
{

    private final ServiceOrderRepository serviceOrderRepository;
    private final ServiceOrderMapper serviceOrderMapper;
//    private final UserAddressInformationService uaiService;
//    private final WorkerService workerService;
    private final EquipmentService  equipmentService;
    public List<ServiceOrder> getAll()
    {
        return serviceOrderRepository.findAllByDeletedFalse();
    }

    public ServiceOrder getById(Long id)
    {
        return serviceOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service order not found"));
    }

    public ServiceOrder create(CreateServiceOrderRequest req)
    {
        Equipment equipment = equipmentService.getById(req.getEquipmentId());
//        UserAddressInformation address = uaiService.getById(req.getUserAddressInformationId());
        ServiceOrder serviceOrder = serviceOrderMapper.toEntity(req);
        serviceOrder.setEquipment(equipment);
//        serviceOrder.setUserAddressInformation(address);
        serviceOrder.setStatus(ServiceOrderStatus.POSTED);
        serviceOrder.setCreatedAt(LocalDateTime.now());
        return  serviceOrderRepository.save(serviceOrder);
    }

    public void delete(Long id)
    {
        serviceOrderRepository.deleteById(id);
    }
}