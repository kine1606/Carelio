package com.Carelio.payment_service.service;

import com.Carelio.payment_service.client.MomoClient;
import com.Carelio.payment_service.client.OrderClient;
import com.Carelio.payment_service.client.dto.OrderResponse;
import com.Carelio.payment_service.config.MomoProperties;
import com.Carelio.payment_service.dto.PaymentRequest;
import com.Carelio.payment_service.dto.PaymentResponse;
import com.Carelio.payment_service.dto.momo.HmacUtil;
import com.Carelio.payment_service.dto.momo.MomoCreateRequest;
import com.Carelio.payment_service.dto.momo.MomoCreateResponse;
import com.Carelio.payment_service.entity.Payment;
import com.Carelio.payment_service.factory.PaymentFactory;
import com.Carelio.payment_service.factory.PaymentProcessor;
import com.Carelio.payment_service.mapper.PaymentMapper;
import com.Carelio.payment_service.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderClient orderClient;
    private final PaymentMapper paymentMapper;
    private final PaymentFactory paymentFactory;
    private final MomoProperties momoProperties;
    private final MomoClient momoClient;

    // POST /api/payment
    @Transactional
    public PaymentResponse createPayment(String userId, PaymentRequest request)
    {
        OrderResponse order = orderClient.getOrderById(request.getOrderId());

        if (!order.getUserId().equals(userId)) {
            log.warn("CẢNH BÁO BẢO MẬT: Người dùng {} cố tình thanh toán cho đơn hàng của người khác {}", userId, order.getId());
            throw new RuntimeException("Bạn không có quyền thực hiện thanh toán cho đơn hàng này!");
        }

        boolean alreadyPaid = paymentRepository.existsByOrderIdAndStatus(order.getId(), "SUCCESS");
        if (alreadyPaid) {
            throw new IllegalStateException("Đơn hàng này đã được thanh toán thành công trước đó!");
        }

        if ("MOMO".equalsIgnoreCase(request.getPaymentMethod()))
        {
            return createMomoPayment(order, request);
        }

        PaymentProcessor processor = paymentFactory.getProcessor(request.getPaymentMethod());


        boolean success = processor.pay(order.getId(), order.getPrice());

        LocalDateTime now = LocalDateTime.now();

        Payment payment = Payment.builder()
                .orderId(order.getId())
                .amount(order.getPrice()) // Sửa trường giá tiền
                .paymentMethod(request.getPaymentMethod())
                .status(success ? "SUCCESS" : "FAILED")
                .transactionId(UUID.randomUUID().toString())
                .paidAt(success ? now : null)
                .createdAt(now)
                .updatedAt(now)
                .build();

        Payment savedPayment = paymentRepository.save(payment);

        if (success) {
            orderClient.markOrderSuccess(order.getId());
            log.info("Thanh toán trực tiếp đơn hàng {} thành công.", order.getId());
        } else {
            orderClient.markOrderFailed(order.getId());
            log.info("Thanh toán trực tiếp đơn hàng {} thất bại.", order.getId());
        }

        return paymentMapper.toResponse(savedPayment);
    }

    private PaymentResponse createMomoPayment(OrderResponse order, PaymentRequest request) {
        LocalDateTime now = LocalDateTime.now();

        Payment payment = Payment.builder()
                .orderId(order.getId())
                .amount(order.getPrice()) // Sửa trường giá tiền
                .paymentMethod("MOMO")
                .status("PENDING")
                .transactionId("MOMO-" + UUID.randomUUID())
                .createdAt(now)
                .updatedAt(now)
                .build();

        Payment savedPayment = paymentRepository.save(payment);

        String requestId = savedPayment.getTransactionId();
        String momoOrderId = "ORDER-" + order.getId() + "-PAYMENT-" + savedPayment.getId();

        String orderInfo = "Thanh toan don hang Carelio số: #" + order.getId();
        String extraData = "";

        Long amount = order.getPrice().longValue();

        String rawSignature =
                "accessKey=" + momoProperties.getAccessKey().trim() +
                        "&amount=" + amount +
                        "&extraData=" + extraData +
                        "&ipnUrl=" + momoProperties.getIpnUrl().trim() +
                        "&orderId=" + momoOrderId +
                        "&orderInfo=" + orderInfo +
                        "&partnerCode=" + momoProperties.getPartnerCode().trim() +
                        "&redirectUrl=" + momoProperties.getRedirectUrl().trim() +
                        "&requestId=" + requestId +
                        "&requestType=" + momoProperties.getRequestType().trim();

        String signature = HmacUtil.hmacSha256(
                rawSignature,
                momoProperties.getSecretKey().trim()
        );

        MomoCreateRequest momoRequest = MomoCreateRequest.builder()
                .partnerCode(momoProperties.getPartnerCode())
                .partnerName("Carelio Service")
                .storeId("CARELIO_STORE")
                .requestId(requestId)
                .amount(amount)
                .orderId(momoOrderId)
                .orderInfo(orderInfo)
                .redirectUrl(momoProperties.getRedirectUrl())
                .ipnUrl(momoProperties.getIpnUrl())
                .lang("vi")
                .requestType(momoProperties.getRequestType())
                .autoCapture(true)
                .extraData(extraData)
                .signature(signature)
                .build();

        MomoCreateResponse momoResponse = momoClient.createPayment(momoRequest);

        if (momoResponse == null || momoResponse.getResultCode() == null) {
            savedPayment.setStatus("FAILED");
            savedPayment.setUpdatedAt(LocalDateTime.now());
            paymentRepository.save(savedPayment);
            throw new RuntimeException("Phản hồi từ MoMo không hợp lệ");
        }

        if (momoResponse.getResultCode() != 0) {
            savedPayment.setStatus("FAILED");
            savedPayment.setUpdatedAt(LocalDateTime.now());
            paymentRepository.save(savedPayment);
            throw new RuntimeException("Khởi tạo thanh toán MoMo thất bại: " + momoResponse.getMessage());
        }

        savedPayment.setPayUrl(momoResponse.getPayUrl());
        savedPayment.setUpdatedAt(LocalDateTime.now());

        Payment updatedPayment = paymentRepository.save(savedPayment);
        return mapToResponse(updatedPayment);
    }

    private PaymentResponse mapToResponse(Payment payment) {
        if (payment == null) return null;

        PaymentResponse response = new PaymentResponse();
        response.setId(payment.getId());
        response.setOrderId(payment.getOrderId());
        response.setAmount(payment.getAmount());
        response.setPaymentMethod(payment.getPaymentMethod());
        response.setStatus(payment.getStatus());
        response.setTransactionId(payment.getTransactionId());
        response.setPayUrl(payment.getPayUrl());
        response.setPaidAt(payment.getPaidAt());
        response.setCreatedAt(payment.getCreatedAt());
        response.setUpdatedAt(payment.getUpdatedAt());
        return response;
    }
}