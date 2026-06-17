package com.Carelio.service_order_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequest
{
    private String contactName;
    private String phoneNumber;
    private String addressLine;
    private boolean isDefault;
}
