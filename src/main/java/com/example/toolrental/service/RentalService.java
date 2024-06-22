package com.example.toolrental.service;

import com.example.toolrental.dto.CheckoutRequest;
import com.example.toolrental.model.RentalAgreement;
import com.example.toolrental.model.Tool;
import com.example.toolrental.repository.ToolRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * Service for handling tool rental operations.
 * Author: Chhabi Sharma
 */
@Service
public class RentalService {

    private static final Logger logger = LoggerFactory.getLogger(RentalService.class);
    private final ToolRepository toolRepository;

    /**
     * Constructor for RentalService.
     *
     * @param toolRepository the repository to retrieve tool information
     */
    public RentalService(ToolRepository toolRepository) {
        this.toolRepository = toolRepository;
    }

    /**
     * Creates a rental agreement for the specified tool using the provided checkout request.
     *
     * @param request the checkout request containing tool code, rental days, discount percent, and checkout date
     * @return a RentalAgreement instance containing the rental details
     */
    public RentalAgreement checkout(CheckoutRequest request) {
        logger.info("Checking out tool: toolCode={}, rentalDays={}, discountPercent={}, checkoutDate={}",
                request.getToolCode(), request.getRentalDays(), request.getDiscountPercent(), request.getCheckoutDate());
        
        LocalDate checkoutDate = LocalDate.parse(request.getCheckoutDate());

        validateCheckoutRequest(request);

        Tool tool = toolRepository.findByCode(request.getToolCode());
        if (tool == null) {
            logger.error("Invalid tool code: {}", request.getToolCode());
            throw new IllegalArgumentException("Invalid tool code.");
        }

        LocalDate dueDate = checkoutDate.plusDays(request.getRentalDays());
        int chargeDays = calculateChargeDays(tool, checkoutDate, dueDate);

        double preDiscountCharge = chargeDays * tool.getDailyCharge();
        double discountAmount = preDiscountCharge * request.getDiscountPercent() / 100;
        double finalCharge = preDiscountCharge - discountAmount;

        RentalAgreement agreement = new RentalAgreement();
        agreement.setToolCode(tool.getCode());
        agreement.setToolType(tool.getType());
        agreement.setToolBrand(tool.getBrand());
        agreement.setRentalDays(request.getRentalDays());
        agreement.setCheckoutDate(checkoutDate);
        agreement.setDueDate(dueDate);
        agreement.setDailyRentalCharge(tool.getDailyCharge());
        agreement.setChargeDays(chargeDays);
        agreement.setPreDiscountCharge(preDiscountCharge);
        agreement.setDiscountPercent(request.getDiscountPercent());
        agreement.setDiscountAmount(discountAmount);
        agreement.setFinalCharge(finalCharge);

        logger.info("Created rental agreement: {}", agreement);
        return agreement;
    }

    /**
     * Validates the checkout request parameters.
     *
     * @param request the checkout request containing tool code, rental days, discount percent, and checkout date
     */
    private void validateCheckoutRequest(CheckoutRequest request) {
        if (request.getRentalDays() < 1) {
            logger.error("Invalid rental day count: {}", request.getRentalDays());
            throw new IllegalArgumentException("Rental day count must be 1 or greater.");
        }
        if (request.getDiscountPercent() < 0 || request.getDiscountPercent() > 100) {
            logger.error("Invalid discount percent: {}", request.getDiscountPercent());
            throw new IllegalArgumentException("Discount percent must be between 0 and 100.");
        }
    }

    /**
     * Calculates the number of chargeable days between the start and end dates for the specified tool.
     *
     * @param tool      the tool being rented
     * @param startDate the start date of the rental period
     * @param endDate   the end date of the rental period
     * @return the number of chargeable days
     */
    private int calculateChargeDays(Tool tool, LocalDate startDate, LocalDate endDate) {
        int chargeDays = 0;
        for (LocalDate date = startDate.plusDays(1); !date.isAfter(endDate); date = date.plusDays(1)) {
            boolean isWeekend = date.getDayOfWeek().getValue() >= 6;
            boolean isHoliday = isHoliday(date);

            if ((isWeekend && tool.isWeekendCharge()) || (!isWeekend && !isHoliday && tool.isWeekdayCharge()) || (isHoliday && tool.isHolidayCharge())) {
                chargeDays++;
            }
        }
        logger.debug("Calculated charge days: startDate={}, endDate={}, chargeDays={}", startDate, endDate, chargeDays);
        return chargeDays;
    }

    /**
     * Determines if the given date is a holiday.
     *
     * @param date the date to check
     * @return true if the date is a holiday, false otherwise
     */
    private boolean isHoliday(LocalDate date) {
        int year = date.getYear();
        LocalDate independenceDay = LocalDate.of(year, 7, 4);
        LocalDate laborDay = LocalDate.of(year, 9, 1).with(java.time.DayOfWeek.MONDAY);

        if (independenceDay.getDayOfWeek() == java.time.DayOfWeek.SATURDAY) {
            independenceDay = independenceDay.minusDays(1);
        } else if (independenceDay.getDayOfWeek() == java.time.DayOfWeek.SUNDAY) {
            independenceDay = independenceDay.plusDays(1);
        }

        boolean isHoliday = date.equals(independenceDay) || date.equals(laborDay);
        logger.debug("Checked holiday: date={}, isHoliday={}", date, isHoliday);
        return isHoliday;
    }
}
