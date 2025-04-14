package com.hana.investprac.controller;

import com.hana.investprac.dto.OrderRequest;
import com.hana.investprac.service.OrderService;
import com.hana.investprac.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;
    private final TokenService tokenService;

    @GetMapping("/token")
    public ResponseEntity<String> testToken() {
        String token = tokenService.getAccessToken();
        return ResponseEntity.ok("✅ 연결 성공! 발급된 토큰: \n" + token);
    }
    @GetMapping("/current-price")
    public ResponseEntity<String> getCurrentPrice(@RequestParam String code){
        String price = orderService.getCurrentPrice(code);
        return ResponseEntity.ok("현재 가격: " + price);
    }

}

