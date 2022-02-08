package com.github.vsyx.premiumcalculator.controller;

import java.math.BigDecimal;

import com.github.vsyx.premiumcalculator.PremiumCalculator;
import com.github.vsyx.premiumcalculator.models.Policy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CalculatorController {
    @Autowired
    private PremiumCalculator calculator;

    @PostMapping("/calculate")
    public ResponseEntity<BigDecimal> calculatePolicy(@RequestBody Policy policy) {
        return ResponseEntity.ok(calculator.calculate(policy));
    }
}
