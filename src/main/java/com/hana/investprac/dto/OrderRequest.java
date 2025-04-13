package com.hana.investprac.dto;

import lombok.Data;

@Data
public class OrderRequest {
    private String symbol; // 종목코드 (예: 005930)
    private int quantity;  // 수량
    private String orderType; // "BUY" 또는 "SELL"
}