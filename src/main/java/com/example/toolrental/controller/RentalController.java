package com.example.toolrental.controller;

import com.example.toolrental.dto.CheckoutRequest;
import com.example.toolrental.model.RentalAgreement;
import com.example.toolrental.service.RentalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;

/**
 * Controller for handling tool rental operations.
 * Author: Chhabi Sharma
 */
@RestController
@RequestMapping("/api/rental")
public class RentalController {

    private static final Logger logger = LoggerFactory.getLogger(RentalController.class);
    private final RentalService rentalService;

    /**
     * Constructs a RentalController with the specified RentalService.
     *
     * @param rentalService the service to handle rental operations
     */
    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    /**
     * Handles the checkout process for renting a tool.
     *
     * @param request the checkout request containing tool code, rental days, discount percent, and checkout date
     * @return a RentalAgreement containing the rental details
     */
    @PostMapping("/checkout")
    public RentalAgreement checkout(@Valid @RequestBody CheckoutRequest request) {
        logger.info("Received checkout request: toolCode={}, rentalDays={}, discountPercent={}, checkoutDate={}",
                request.getToolCode(), request.getRentalDays(), request.getDiscountPercent(), request.getCheckoutDate());
        
        LocalDate date = LocalDate.parse(request.getCheckoutDate());
        RentalAgreement agreement = rentalService.checkout(request);
        
        logger.info("Created rental agreement: toolCode={}, toolType={}, toolBrand={}, rentalDays={}, checkoutDate={}, dueDate={}, dailyRentalCharge={}, chargeDays={}, preDiscountCharge={}, discountPercent={}, discountAmount={}, finalCharge={}",
                agreement.getToolCode(), agreement.getToolType(), agreement.getToolBrand(), agreement.getRentalDays(),
                agreement.getCheckoutDate(), agreement.getDueDate(), agreement.getDailyRentalCharge(), agreement.getChargeDays(),
                agreement.getPreDiscountCharge(), agreement.getDiscountPercent(), agreement.getDiscountAmount(), agreement.getFinalCharge());
        
        return agreement;
    }
}
