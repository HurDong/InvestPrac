package com.hana.investprac.dto;

import lombok.Data;

@Data
public class StockPriceResponse {
    private String stockName;
    private String currentPrice;
    private String diffFromYesterday;
    private String rateChange;
    private String openPrice;
    private String highPrice;
    private String lowPrice;
    private String volume;
    private String amount;
    private String per;
    private String pbr;

public void printPretty() {
    System.out.println("📈 [" + stockName + "] 현재가 시세 정보");
    System.out.println("────────────────────────────────────");
    System.out.printf("현재가      : %,d 원\n", Integer.parseInt(currentPrice));
    System.out.printf("전일대비    : %+d 원 (▲ %s%%)\n", Integer.parseInt(diffFromYesterday), rateChange);
    System.out.printf("시가        : %,d 원\n", Integer.parseInt(openPrice));
    System.out.printf("고가        : %,d 원\n", Integer.parseInt(highPrice));
    System.out.printf("저가        : %,d 원\n", Integer.parseInt(lowPrice));
    System.out.printf("거래량      : %,d 주\n", Long.parseLong(volume));
    System.out.printf("거래대금    : %,d 원\n", Long.parseLong(amount));
    System.out.printf("PER         : %s\n", per);
    System.out.printf("PBR         : %s\n", pbr);
    System.out.println("────────────────────────────────────");
}
}

