package com.hana.investprac.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hana.investprac.service.BalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account")
public class BalanceController {

    private final BalanceService balanceService;

    @GetMapping("/balance")
    public ResponseEntity<String> getBalance() throws JsonProcessingException {
        String result = balanceService.getBalance();
        return ResponseEntity.ok(result);
    }
}
