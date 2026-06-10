package com.amigoscode.carelio.serviceOrder.dto;

import com.amigoscode.carelio.base.BaseResponse;
import com.amigoscode.carelio.equipment.dto.response.EquipmentSummaryResponse;
import com.amigoscode.carelio.serviceOrder.entity.ServiceOrderStatus;
import com.amigoscode.carelio.serviceOrder.entity.ServiceType;
import com.amigoscode.carelio.user.entity.user.UserAddressInformation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceOrderResponse extends BaseResponse
{
    private Long id;
    private String title;
    private String description;
    private ServiceOrderStatus status;
    private Set<ServiceType> serviceTypes;
    private UserAddressInformation userAddressInformation;
    private EquipmentSummaryResponse equipment;
//    private WorkerSummaryResponse worker;
}
