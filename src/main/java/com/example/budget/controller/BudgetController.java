package com.example.budget.controller;

import com.example.budget.model.BudgetRequest;
import com.example.budget.model.BudgetResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;

@RestController
@RequestMapping("/api/budget")
public class BudgetController {

    @PostMapping("/calculate")
    public ResponseEntity<?> calculate(@RequestBody BudgetRequest request) {
        if (request.getDays() <= 0) {
            return ResponseEntity.badRequest().body("Количество дней должно быть больше нуля.");
        }

        BigDecimal total = request.getDailyCost().multiply(BigDecimal.valueOf(request.getDays()));

        if (request.isIncludeInsurance()) {
            total = total.multiply(new BigDecimal("1.05"));
        }

        total = total.setScale(2, RoundingMode.HALF_UP);
        return ResponseEntity.ok(new BudgetResponse(total));
    }
}
