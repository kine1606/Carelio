package com.amigoscode.carelio.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserAddressInformationRequest
{
    @NotBlank(message = "contact name is required")
    private String contactName;

    @NotBlank(message = "phone number is required")
    private String phoneNumber;

    @NotBlank(message = "address line is required")
    private String addressLine;

    @NotBlank(message = "city is required")
    private String city;

    private Boolean isDefault;
}
