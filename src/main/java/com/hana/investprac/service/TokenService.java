package com.hana.investprac.service;

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
public class TokenService {
    @Value("${kis_app_key}")
    private String appKey;

    @Value("${kis_app_secret}")
    private String appSecret;

    @Value("${kis.base-url}")
    private String baseUrl;

    public String getAccessToken() {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> requestBody = Map.of(
                "grant_type", "client_credentials",
                "appkey", appKey,
                "appsecret", appSecret
        );

        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                baseUrl + "/oauth2/tokenP",
                request,
                Map.class
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            return (String) response.getBody().get("access_token");
        } else {
            throw new RuntimeException("❌ KIS 토큰 발급 실패: " + response.getStatusCode());
        }
    }
}
