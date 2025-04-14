package com.hana.investprac.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hana.investprac.dto.OrderRequest;
import com.hana.investprac.dto.StockPriceResponse;
import com.hana.investprac.util.TokenLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final TokenService tokenService;
    private final TokenLoader tokenLoader;

    @Value("${kis_app_key}")
    private String appKey;

    @Value("${kis_app_secret}")
    private String appSecret;

    @Value("${kis.account.number}")
    private String accountNumber;

    @Value("${kis.base-url}")
    private String baseUrl;


    public String getCurrentPrice(String stockCode) {
        String token = tokenLoader.loadToken();
        System.out.println("Token: " + token);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("authorization", "Bearer " + token);
        headers.set("appkey", appKey);
        headers.set("appsecret", appSecret);
        headers.set("tr_id", "FHKST01010100");
        headers.set("custtype", "P");

        HttpEntity<?> entity = new HttpEntity<>(headers);

        String url = UriComponentsBuilder.fromUriString(baseUrl + "/uapi/domestic-stock/v1/quotations/inquire-price").queryParam("fid_cond_mrkt_div_code", "J") // KOSPI
                .queryParam("fid_input_iscd", stockCode)   // 종목 코드
                .build().toUriString();

        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            System.out.println("Response: " + response.getBody());

            // JSON 응답을 DTO로 매핑
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            JsonNode output = root.get("output");

            StockPriceResponse dto = new StockPriceResponse();
            dto.setStockName(output.get("bstp_kor_isnm").asText()); // 업종명
            dto.setCurrentPrice(output.get("stck_prpr").asText()); // 현재가
            dto.setDiffFromYesterday(output.get("prdy_vrss").asText()); // 전일 대비
            dto.setRateChange(output.get("prdy_ctrt").asText()); // 등락률
            dto.setOpenPrice(output.get("stck_oprc").asText()); // 시가
            dto.setHighPrice(output.get("stck_hgpr").asText()); // 고가
            dto.setLowPrice(output.get("stck_lwpr").asText()); // 저가
            dto.setVolume(output.get("acml_vol").asText()); // 거래량
            dto.setAmount(output.get("acml_tr_pbmn").asText()); // 거래대금
            dto.setPer(output.get("per").asText()); // PER
            dto.setPbr(output.get("pbr").asText()); // PBR

            // 콘솔에 예쁘게 출력
            dto.printPretty();
            return response.getBody();
        } catch (HttpServerErrorException e) {
            System.err.println("API 호출 실패: " + e.getResponseBodyAsString());
            throw e;
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

//        return response.getBody();
    }
}

