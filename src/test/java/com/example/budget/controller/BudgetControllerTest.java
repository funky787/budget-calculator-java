package com.example.budget.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BudgetController.class)
class BudgetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private void checkBudget(String cost, int days, boolean insurance, String expected) throws Exception {
        String json = objectMapper.writeValueAsString(Map.of(
                "dailyCost", cost,
                "days", days,
                "includeInsurance", insurance
        ));

        mockMvc.perform(post("/api/budget/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalAmount").value(Double.parseDouble(expected)));
    }

    @Test
    void calculateWithoutInsurance() throws Exception {
        checkBudget("100", 5, false, "500.00");
    }

    @Test
    void calculateWithInsurance() throws Exception {
        checkBudget("100", 5, true, "525.00");
    }

    @Test
    void negativeDaysReturnsBadRequest() throws Exception {
        String json = objectMapper.writeValueAsString(Map.of(
                "dailyCost", 100,
                "days", -1,
                "includeInsurance", false
        ));

        mockMvc.perform(post("/api/budget/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void zeroDailyCostReturnsZero() throws Exception {
        checkBudget("0", 5, false, "0.00");
    }

    @Test
    void fractionalDailyCostKeepsKopecks() throws Exception {
        checkBudget("100.50", 3, false, "301.50");
    }

    @Test
    void zeroDaysReturnsBadRequest() throws Exception {
        String json = objectMapper.writeValueAsString(Map.of(
                "dailyCost", 100,
                "days", 0,
                "includeInsurance", false
        ));

        mockMvc.perform(post("/api/budget/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }
}
