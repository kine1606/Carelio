package com.Carelio.payment_service.factory;

import java.math.BigDecimal;

public interface PaymentProcessor
{
    String getPaymentMethod();
    boolean pay(Long orderId, BigDecimal amount);
}
