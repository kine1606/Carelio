package com.Carelio.service_order_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderReviewRequest
{
//    @NotNull(message = "Rating is required")
//    @Range(min = 1, max = 5, message = "Rating has to be in range from 1 to 5")
    private Integer rating;
    private String comment;
}
