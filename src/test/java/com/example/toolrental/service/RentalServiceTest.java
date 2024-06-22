package com.example.toolrental.service;

import com.example.toolrental.model.RentalAgreement;
import com.example.toolrental.repository.ToolRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class RentalServiceTest {
    private RentalService rentalService;

    @BeforeEach
    void setUp() {
        rentalService = new RentalService(new ToolRepository());
    }

    @Test
    void testCheckoutInvalidDiscount() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                rentalService.checkout("JAKR", 5, 101, LocalDate.of(2015, 9, 3)));
        assertEquals("Discount percent must be between 0 and 100.", exception.getMessage());
    }

    @Test
    void testCheckoutLadderWithDiscount() {
        RentalAgreement agreement = rentalService.checkout("LADW", 3, 10, LocalDate.of(2020, 7, 2));
        assertEquals("LADW", agreement.getToolCode());
        assertEquals(3, agreement.getRentalDays());
        assertEquals(LocalDate.of(2020, 7, 5), agreement.getDueDate());
        assertEquals(2, agreement.getChargeDays());
        assertEquals(3.98, agreement.getPreDiscountCharge());
        assertEquals(0.40, agreement.getDiscountAmount());
        assertEquals(3.58, agreement.getFinalCharge());
    }

    @Test
    void testCheckoutChainsawWithDiscount() {
        RentalAgreement agreement = rentalService.checkout("CHNS", 5, 25, LocalDate.of(2015, 7, 2));
        assertEquals("CHNS", agreement.getToolCode());
        assertEquals(5, agreement.getRentalDays());
        assertEquals(LocalDate.of(2015, 7, 7), agreement.getDueDate());
        assertEquals(3, agreement.getChargeDays());
        assertEquals(4.47, agreement.getPreDiscountCharge());
        assertEquals(1.12, agreement.getDiscountAmount());
        assertEquals(3.35, agreement.getFinalCharge());
    }

}
