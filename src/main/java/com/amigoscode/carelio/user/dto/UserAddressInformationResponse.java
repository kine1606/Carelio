package com.amigoscode.carelio.user.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAddressInformationResponse
{
    private Long id;
    private Long userId;
    private String contactName;
    private String phoneNumber;
    private String addressLine;
    private String city;
    private Boolean isDefault;
    private Boolean isActive;
}
