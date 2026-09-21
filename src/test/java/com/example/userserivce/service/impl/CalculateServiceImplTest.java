package com.example.userserivce.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculateServiceImplTest {

    private CalculateServiceImpl calculateService;

    @BeforeEach
    void setUp() {
        calculateService = new CalculateServiceImpl();
    }

    @Test
    void sum_shouldReturnTotal_whenNumbersArePositive() {
        Integer result = calculateService.sum(2, 3);

        assertEquals(5, result);
    }

    @Test
    void sum_shouldReturnNegativeTotal_whenNumbersAreNegative() {
        Integer result = calculateService.sum(-2, -3);

        assertEquals(-5, result);
    }

    @Test
    void sum_shouldReturnOtherNumber_whenOneNumberIsZero() {
        Integer result = calculateService.sum(0, 7);

        assertEquals(7, result);
    }
}
