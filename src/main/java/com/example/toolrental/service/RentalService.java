package com.example.toolrental.service;

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
     * Creates a rental agreement for the specified tool.
     *
     * @param toolCode       the code of the tool to be rented
     * @param rentalDays     the number of days the tool will be rented
     * @param discountPercent the discount percentage to be applied to the rental
     * @param checkoutDate   the date on which the tool is checked out
     * @return a RentalAgreement instance containing the rental details
     */
    public RentalAgreement checkout(String toolCode, int rentalDays, int discountPercent, LocalDate checkoutDate) {
        logger.info("Checking out tool: toolCode={}, rentalDays={}, discountPercent={}, checkoutDate={}",
                toolCode, rentalDays, discountPercent, checkoutDate);
        
        if (rentalDays < 1) {
            logger.error("Invalid rental day count: {}", rentalDays);
            throw new IllegalArgumentException("Rental day count must be 1 or greater.");
        }
        if (discountPercent < 0 || discountPercent > 100) {
            logger.error("Invalid discount percent: {}", discountPercent);
            throw new IllegalArgumentException("Discount percent must be between 0 and 100.");
        }

        Tool tool = toolRepository.findByCode(toolCode);
        if (tool == null) {
            logger.error("Invalid tool code: {}", toolCode);
            throw new IllegalArgumentException("Invalid tool code.");
        }

        LocalDate dueDate = checkoutDate.plusDays(rentalDays);
        int chargeDays = calculateChargeDays(tool, checkoutDate, dueDate);

        double preDiscountCharge = chargeDays * tool.getDailyCharge();
        double discountAmount = preDiscountCharge * discountPercent / 100;
        double finalCharge = preDiscountCharge - discountAmount;

        RentalAgreement agreement = new RentalAgreement();
        agreement.setToolCode(tool.getCode());
        agreement.setToolType(tool.getType());
        agreement.setToolBrand(tool.getBrand());
        agreement.setRentalDays(rentalDays);
        agreement.setCheckoutDate(checkoutDate);
        agreement.setDueDate(dueDate);
        agreement.setDailyRentalCharge(tool.getDailyCharge());
        agreement.setChargeDays(chargeDays);
        agreement.setPreDiscountCharge(preDiscountCharge);
        agreement.setDiscountPercent(discountPercent);
        agreement.setDiscountAmount(discountAmount);
        agreement.setFinalCharge(finalCharge);

        logger.info("Created rental agreement: {}", agreement);
        return agreement;
    }

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
