package com.Carelio.payment_service.client;

import com.Carelio.payment_service.config.MomoProperties;
import com.Carelio.payment_service.dto.momo.MomoCreateRequest;
import com.Carelio.payment_service.dto.momo.MomoCreateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class MomoClient {

    private final MomoProperties momoProperties;

    private final RestClient restClient = RestClient.create();

    public MomoCreateResponse createPayment(MomoCreateRequest request) {
        return restClient.post()
                .uri(momoProperties.getEndpoint())
                .body(request)
                .retrieve()
                .body(MomoCreateResponse.class);
    }
}