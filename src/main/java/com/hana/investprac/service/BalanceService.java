package com.hana.investprac.service;

import com.hana.investprac.util.TokenLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class BalanceService {

    private final TokenLoader tokenLoader;

    @Value("${kis.app.key}")
    private String appKey;

    @Value("${kis.app.secret}")
    private String appSecret;

    @Value("${kis.base-url}")
    private String baseUrl;

    @Value("${kis.account.number}")
    private String accountNumber;

    public String getBalance() {
        String token = tokenLoader.loadToken();
        String cano = accountNumber.substring(1, 9);
        String acntPrdtCd = "01";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("authorization", "Bearer " + token);
        headers.set("appkey", appKey);
        headers.set("appsecret", appSecret);
        headers.set("tr_id", "TTTC8434R");
        headers.set("custtype", "P");

        HttpEntity<?> entity = new HttpEntity<>(headers);

        String url = UriComponentsBuilder
                .fromUriString(baseUrl + "/uapi/domestic-stock/v1/trading/inquire-balance")
                .queryParam("CANO", cano)
                .queryParam("ACNT_PRDT_CD", acntPrdtCd)
                .queryParam("AFHR_FLPR_YN", "N")
                .queryParam("OFL_YN", "")
                .queryParam("INQR_DVSN", "02")
                .queryParam("UNPR_DVSN", "01")
                .queryParam("FUND_STTL_ICLD_YN", "N")
                .queryParam("FNCG_AMT_AUTO_RDPT_YN", "N")
                .queryParam("PRCS_DVSN", "00")
                .queryParam("CTX_AREA_FK100", "")
                .queryParam("CTX_AREA_NK100","")
                .build()
                .toUriString();

        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            System.out.println("✅ 잔고 조회 결과:\n" + response.getBody());
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            System.err.println("❌ 잔고 조회 실패: " + e.getResponseBodyAsString());
            throw e;
        }
    }
}

