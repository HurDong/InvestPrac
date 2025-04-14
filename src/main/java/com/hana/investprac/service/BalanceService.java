package com.hana.investprac.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hana.investprac.dto.BalanceResponse;
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
    private final ObjectMapper mapper;

    @Value("${kis.app.key}")
    private String appKey;

    @Value("${kis.app.secret}")
    private String appSecret;

    @Value("${kis.base-url}")
    private String baseUrl;

    @Value("${kis.account.number}")
    private String accountNumber;

    public String getBalance() throws JsonProcessingException {
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

        String url = UriComponentsBuilder.fromUriString(baseUrl + "/uapi/domestic-stock/v1/trading/inquire-balance").queryParam("CANO", cano).queryParam("ACNT_PRDT_CD", acntPrdtCd).queryParam("AFHR_FLPR_YN", "N").queryParam("OFL_YN", "").queryParam("INQR_DVSN", "02").queryParam("UNPR_DVSN", "01").queryParam("FUND_STTL_ICLD_YN", "N").queryParam("FNCG_AMT_AUTO_RDPT_YN", "N").queryParam("PRCS_DVSN", "00").queryParam("CTX_AREA_FK100", "").queryParam("CTX_AREA_NK100", "").build().toUriString();

        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            JsonNode root = mapper.readTree(response.getBody());
            JsonNode output1 = root.path("output1").get(0); // 첫 번째 종목
            JsonNode output2 = root.path("output2").get(0); // 요약 정보

            BalanceResponse dto = new BalanceResponse();

            // 📦 종목 정보
            dto.setProductName(output1.path("prdt_name").asText());
            dto.setQuantity(output1.path("hldg_qty").asText());
            dto.setAvgBuyPrice(output1.path("pchs_avg_pric").asText());
            dto.setCurrentPrice(output1.path("prpr").asText());
            dto.setEvalAmount(output1.path("evlu_amt").asText());
            dto.setProfitAmount(output1.path("evlu_pfls_amt").asText());
            dto.setProfitRate(output1.path("evlu_pfls_rt").asText());

            // 💰 계좌 요약
            dto.setTotalCash(output2.path("dnca_tot_amt").asText());
            dto.setStockEvalAmount(output2.path("scts_evlu_amt").asText());
            dto.setTotalEvalAmount(output2.path("tot_evlu_amt").asText());
            dto.setTotalProfit(output2.path("evlu_pfls_smtl_amt").asText());
            dto.printPretty();
            return response.getBody();
        } catch (HttpStatusCodeException | JsonProcessingException e) {
            throw e;
        }
    }
}

