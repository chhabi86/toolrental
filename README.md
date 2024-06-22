# Tool Rental Application

This is a simple tool rental application built with Java and Spring Boot. The application allows users to rent tools for a specified number of days, applying discounts and generating rental agreements.

## Features

- Rent tools with different daily charges.
- Apply discounts to the rental charges.
- Exclude charges for weekends and holidays based on the tool type.
- Generate rental agreements with detailed information.

## Tools

The following tools are available for rental:

| Tool Code | Tool Type  | Brand  |
|-----------|------------|--------|
| CHNS      | Chainsaw   | Stihl  |
| LADW      | Ladder     | Werner |
| JAKD      | Jackhammer | DeWalt |
| JAKR      | Jackhammer | Ridgid |

## Daily Charges

| Tool Type  | Daily Charge | Weekday Charge | Weekend Charge | Holiday Charge |
|------------|--------------|----------------|----------------|----------------|
| Ladder     | $1.99        | Yes            | Yes            | No             |
| Chainsaw   | $1.49        | Yes            | No             | Yes            |
| Jackhammer | $2.99        | Yes            | No             | No             |

## Holidays

- Independence Day: July 4th (observed on the closest weekday if it falls on a weekend)
- Labor Day: First Monday in September

## API Endpoints

### Checkout Tool

**URL:** `/api/rental/checkout`

**Method:** `POST`

**Parameters:**

- `toolCode` (String): The code of the tool to be rented.
- `rentalDays` (int): The number of days the tool will be rented.
- `discountPercent` (int): The discount percentage to be applied.
- `checkoutDate` (String): The date of checkout in `yyyy-MM-dd` format.

**Response:**

Returns a `RentalAgreement` object with the following fields:

- `toolCode`
- `toolType`
- `toolBrand`
- `rentalDays`
- `checkoutDate`
- `dueDate`
- `dailyRentalCharge`
- `chargeDays`
- `preDiscountCharge`
- `discountPercent`
- `discountAmount`
- `finalCharge`

## Running the Application

To run the application, use the following command:

```bash
mvn spring-boot:run
