package com.example.budget.model;

import java.math.BigDecimal;

public class BudgetRequest {
    private BigDecimal dailyCost;
    private int days;
    private boolean includeInsurance;

    public BigDecimal getDailyCost() {
        return dailyCost;
    }

    public void setDailyCost(BigDecimal dailyCost) {
        this.dailyCost = dailyCost;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public boolean isIncludeInsurance() {
        return includeInsurance;
    }

    public void setIncludeInsurance(boolean includeInsurance) {
        this.includeInsurance = includeInsurance;
    }
}
