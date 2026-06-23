package com.Carelio.payment_service.mapper;

import com.Carelio.payment_service.dto.PaymentRequest;
import com.Carelio.payment_service.dto.PaymentResponse;
import com.Carelio.payment_service.entity.Payment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "Spring")
public interface PaymentMapper
{
    Payment toEntity(PaymentRequest request);
    PaymentResponse toResponse(Payment payment);
}
