package com.example.toolrental.service;

import com.example.toolrental.dto.CheckoutRequest;
import com.example.toolrental.model.RentalAgreement;
import com.example.toolrental.model.Tool;
import com.example.toolrental.repository.ToolRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RentalServiceTest {

    private RentalService rentalService;

    @BeforeEach
    void setUp() {
        // Mock or initialize ToolRepository as needed for testing
        ToolRepository toolRepository = new ToolRepository();
        rentalService = new RentalService(toolRepository);
    }

    @Test
    void testCheckoutInvalidDiscount() {
        CheckoutRequest request = new CheckoutRequest("JAKR", 5, 101, "2015-09-03");
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                rentalService.checkout(request));
        assertEquals("Discount percent must be between 0 and 100.", exception.getMessage());
    }

    @Test
    void testCheckoutLadderWithDiscount() {
        CheckoutRequest request = new CheckoutRequest("LADW", 3, 10, "2020-07-02");
        RentalAgreement agreement = rentalService.checkout(request);
        assertEquals("LADW", agreement.getToolCode());
        assertEquals(3, agreement.getRentalDays());
        assertEquals(LocalDate.of(2020, 7, 5), agreement.getDueDate());
        assertEquals(2, agreement.getChargeDays());
        assertEquals(3.98, agreement.getPreDiscountCharge(), 0.01);
        assertEquals(0.40, agreement.getDiscountAmount(), 0.01);
        assertEquals(3.58, agreement.getFinalCharge(), 0.01);
    }

    @Test
    void testCheckoutChainsawWithDiscount() {
        CheckoutRequest request = new CheckoutRequest("CHNS", 5, 25, "2015-07-02");
        RentalAgreement agreement = rentalService.checkout(request);
        assertEquals("CHNS", agreement.getToolCode());
        assertEquals(5, agreement.getRentalDays());
        assertEquals(LocalDate.of(2015, 7, 7), agreement.getDueDate());
        assertEquals(3, agreement.getChargeDays());
        assertEquals(4.47, agreement.getPreDiscountCharge(), 0.01);
        assertEquals(1.12, agreement.getDiscountAmount(), 0.01);
        assertEquals(3.35, agreement.getFinalCharge(), 0.01);
    }
}
