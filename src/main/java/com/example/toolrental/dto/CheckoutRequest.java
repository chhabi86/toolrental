package com.example.toolrental.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CheckoutRequest {

    @NotBlank(message = "Tool code is required")
    private String toolCode;

    @Min(value = 1, message = "Rental day count must be 1 or greater")
    @NotNull(message = "Rental days are required")
    private Integer rentalDays;

    @Min(value = 0, message = "Discount percent must be between 0 and 100")
    @Max(value = 100, message = "Discount percent must be between 0 and 100")
    @NotNull(message = "Discount percent is required")
    private Integer discountPercent;

    @NotBlank(message = "Checkout date is required")
    private String checkoutDate;

}
