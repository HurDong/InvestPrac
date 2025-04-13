package com.hana.investprac.service;

import com.hana.investprac.dto.OrderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final TokenService tokenService;

    @Value("${kis.app.key}")
    private String appKey;

    @Value("${kis.account.number}")
    private String accountNumber;

    @Value("${kis.base-url}")
    private String baseUrl;

    public String sendOrder(OrderRequest request) {
        String token = tokenService.getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("authorization", "Bearer " + token);
        headers.set("appkey", appKey);
        headers.set("tr_id", request.getOrderType().equalsIgnoreCase("BUY") ? "VTTC0802U" : "VTTC0801U"); // 모의투자용
        headers.set("custtype", "P");

        Map<String, Object> body = Map.of(
                "CANO", accountNumber.substring(0, 8),
                "ACNT_PRDT_CD", accountNumber.substring(8),
                "PDNO", request.getSymbol(),
                "ORD_DVSN", "01", // 시장가 주문
                "ORD_QTY", String.valueOf(request.getQuantity())
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl + "/uapi/domestic-stock/v1/trading/order-cash",
                entity,
                String.class
        );

        return response.getBody();
    }
}

