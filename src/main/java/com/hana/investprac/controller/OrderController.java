package com.hana.investprac.controller;

import com.hana.investprac.dto.OrderRequest;
import com.hana.investprac.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<String> order(@RequestBody OrderRequest request) {
        String response = orderService.sendOrder(request);
        return ResponseEntity.ok(response);
    }
}

