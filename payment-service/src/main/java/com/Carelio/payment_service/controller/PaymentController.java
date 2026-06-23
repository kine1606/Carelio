package com.Carelio.payment_service.controller;

import com.Carelio.payment_service.dto.PaymentRequest;
import com.Carelio.payment_service.dto.PaymentResponse;
import com.Carelio.payment_service.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<PaymentResponse> createPayment(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody @Valid PaymentRequest request) {

        String userId = jwt.getSubject();
        return ResponseEntity.ok(paymentService.createPayment(userId, request));
    }
}