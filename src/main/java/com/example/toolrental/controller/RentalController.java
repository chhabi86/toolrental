package com.example.toolrental.controller;

import com.example.toolrental.model.RentalAgreement;
import com.example.toolrental.service.RentalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * REST controller for handling tool rental checkouts.
 * Provides an endpoint to create a rental agreement for a specified tool.
 * Author: Chhabi Sharma
 */
@RestController
@RequestMapping("/api/rental")
public class RentalController {

    private static final Logger logger = LoggerFactory.getLogger(RentalController.class);
    private final RentalService rentalService;

    /**
     * Constructor for RentalController.
     *
     * @param rentalService the rental service to be used for creating rental agreements
     */
    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    /**
     * Endpoint for checking out a tool.
     * 
     * @param toolCode       the code of the tool to be rented
     * @param rentalDays     the number of days the tool will be rented
     * @param discountPercent the discount percentage to be applied to the rental
     * @param checkoutDate   the date on which the tool is checked out
     * @return a RentalAgreement instance containing the rental details
     */
    @PostMapping("/checkout")
    public RentalAgreement checkout(@RequestParam String toolCode,
                                    @RequestParam int rentalDays,
                                    @RequestParam int discountPercent,
                                    @RequestParam String checkoutDate) {
        logger.info("Received checkout request: toolCode={}, rentalDays={}, discountPercent={}, checkoutDate={}",
                toolCode, rentalDays, discountPercent, checkoutDate);
        
        LocalDate date = LocalDate.parse(checkoutDate);
        RentalAgreement agreement = rentalService.checkout(toolCode, rentalDays, discountPercent, date);
        
        logger.info("Created rental agreement: {}", agreement);
        return agreement;
    }
}
