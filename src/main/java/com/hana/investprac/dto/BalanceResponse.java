package com.hana.investprac.dto;

import lombok.Data;

@Data
public class BalanceResponse {
    // 🧾 종목 정보 (1종목만 보유 중일 경우 or 대표 종목만 보여줄 때)
    private String productName;
    private String quantity;
    private String avgBuyPrice;
    private String currentPrice;
    private String evalAmount;
    private String profitAmount;
    private String profitRate;

    // 💰 계좌 요약 정보
    private String totalCash;
    private String stockEvalAmount;
    private String totalEvalAmount;
    private String totalProfit;

    public void printPretty() {
        System.out.println("📊 [주식 잔고 조회 결과]");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.printf("🔹 종목명      : %s\n", productName);
        System.out.printf("🔹 보유수량    : %,d주\n", Long.parseLong(quantity));
        System.out.printf("🔹 평균매입가  : %,.2f원\n", Double.parseDouble(avgBuyPrice));
        System.out.printf("🔹 현재가      : %,.2f원\n", Double.parseDouble(currentPrice));
        System.out.printf("🔹 평가금액    : %,.2f원\n", Double.parseDouble(evalAmount));
        System.out.printf("🔹 손익        : %,.2f원 (%s%%)\n", Double.parseDouble(profitAmount), profitRate);
        System.out.println();

        System.out.println("💰 [계좌 요약]");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.printf("예수금 총액     : %,.2f원\n", Double.parseDouble(totalCash));
        System.out.printf("주식평가금액     : %,.2f원\n", Double.parseDouble(stockEvalAmount));
        System.out.printf("총평가금액       : %,.2f원\n", Double.parseDouble(totalEvalAmount));
        System.out.printf("총손익           : %,.2f원\n", Double.parseDouble(totalProfit));
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }

}
