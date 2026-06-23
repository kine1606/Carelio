package com.Carelio.payment_service.controller;

import com.Carelio.payment_service.dto.PaymentRequest;
import com.Carelio.payment_service.dto.PaymentResponse;
import com.Carelio.payment_service.dto.momo.MomoIpnRequest;
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

    @PostMapping("/momo-ipn")
    public ResponseEntity<Void> receiveMomoIpn(@RequestBody MomoIpnRequest ipnRequest) {
        paymentService.processMomoIpn(ipnRequest);
        return ResponseEntity.noContent().build(); // Trả về 204 cho MoMo biết hệ thống đã nhận dữ liệu
    }

    // 2. API cho Frontend check trạng thái thanh toán xem đã thành công chưa để chuyển màn hình
    @GetMapping("/status/{orderId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<String> getPaymentStatus(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long orderId) {
        String userId = jwt.getSubject();
        String status = paymentService.getPaymentStatus(userId, orderId);
        return ResponseEntity.ok(status); // Trả về "SUCCESS", "FAILED" hoặc "PENDING"
    }
}