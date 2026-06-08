package com.Carelio.household_service.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class CreateCategoryRequest
{
    @NotBlank(message = "Name is required")
    private String name;
}
