package com.example.toolrental.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Tool {
    private String code;
    private String type;
    private String brand;
    
    //charges based on type
    private double dailyCharge;
    private boolean isWeekdayCharge;
    private boolean isWeekendCharge;
    private boolean isHolidayCharge;
}

