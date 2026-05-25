package org.hokvanthiv.ecommerce_springboot_api.service.Impl;

import lombok.RequiredArgsConstructor;
import org.hokvanthiv.ecommerce_springboot_api.service.BakongClientService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class BakongClientServiceImpl implements BakongClientService {

    private final WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8000")
            .defaultHeader("Accept", "application/json")
            .defaultHeader("Content-Type", "application/json")
            .build();

    // =========================
    // GENERATE QR
    // =========================
    @Override
    public Map<String, Object> generateQR(Long orderId, Double amount) {

        return webClient.post()
                .uri("/api/bakong/generate") // ✅ correct
                .bodyValue(Map.of(
                        "order_id", orderId,
                        "amount", amount
                ))
                .retrieve()
                .onStatus(
                        status -> status.isError(),
                        response -> response.bodyToMono(String.class)
                                .map(body -> new RuntimeException("Laravel ERROR: " + body))
                )
                .bodyToMono(Map.class)
                .block();
    }

    // =========================
    // CHECK PAYMENT
    // =========================
    @Override
    public Map<String, Object> checkPayment(String md5) {

        return webClient.get()
                .uri("/api/bakong/check/{md5}", md5) // ✅ safer than string concat
                .retrieve()
                .onStatus(
                        status -> status.isError(),
                        response -> response.bodyToMono(String.class)
                                .map(body -> new RuntimeException("Laravel ERROR: " + body))
                )
                .bodyToMono(Map.class)
                .block();
    }
}