package Services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.Set;

import Repositories.HolidayRepository;
import Repositories.ToolRepository;
import Objects.Holiday;
import Objects.RentalAgreement;
import Objects.Tool;

public class CheckoutServiceImpl {
    public RentalAgreement checkout(String toolCode, int rentalDayCount, int discount, LocalDate checkoutDate) throws Exception {
        // Data Validation
        if(rentalDayCount < 1) {
            throw new Exception(String.format("Invalid Rental day count {%d}: Rental day count must be 1 or greater.", rentalDayCount));
        }
        if(!(discount >= 0 && discount <= 100)) {
            throw new Exception(String.format("Invald Discount percent {%d}: Discount percent must be in the range 0-100", discount));
        }

        ToolRepository toolRepository = ToolRepository.getInstance();

        Tool tool = toolRepository.getTool(toolCode);
        // Validation for tool can go after here to check for a null response

        LocalDate dueDate = checkoutDate.plusDays(rentalDayCount);

        int chargeableDays = rentalDayCount;
        if(!tool.getToolType().getWeekendCharge() && chargeableDays > 0) {
            chargeableDays -= getNumberOfWeekends(checkoutDate, rentalDayCount);
        }

        if (!tool.getToolType().getHolidayCharge() && chargeableDays > 0) {
            chargeableDays -= getNumberOfHolidays(checkoutDate, rentalDayCount, dueDate);
        }

        // Sanity check to not have chargeableDays be negative
        chargeableDays = Math.max(0, chargeableDays);

        //Might need to change rounding to happen at the end when saving the data
        BigDecimal preDiscountCharge = new BigDecimal(tool.getToolType().getDailyCharge() * chargeableDays);
        preDiscountCharge = preDiscountCharge.setScale(2, RoundingMode.HALF_UP);

        BigDecimal discountAmount = preDiscountCharge.multiply(new BigDecimal(discount / 100d));
        discountAmount = discountAmount.setScale(2, RoundingMode.HALF_UP);

        BigDecimal finalCharge = preDiscountCharge.subtract(discountAmount);

        return new RentalAgreement(tool, rentalDayCount, checkoutDate, dueDate, chargeableDays, preDiscountCharge.floatValue(),
                                    discount, discountAmount.floatValue(), finalCharge.floatValue());
    }

    private int getNumberOfWeekends(LocalDate checkoutDate, int rentalDayCount) {
        int numberOfWeekends = (rentalDayCount / 7) * 2;
        int remainder = rentalDayCount % 7;
        if (remainder > 0) {
            for (int i = 0; i < remainder; i++) {
                checkoutDate = checkoutDate.plusDays(1);
                if(checkoutDate.getDayOfWeek() == DayOfWeek.SATURDAY || checkoutDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                    numberOfWeekends++;
                }
            }
        }
        return numberOfWeekends;
    }

    private int getNumberOfHolidays(LocalDate checkoutDate, int rentalDayCount, LocalDate dueDate) {
        int numberOfHolidays = 0;
        HolidayRepository holidays = HolidayRepository.getInstance();

        
        while (checkoutDate.plusYears(1).isBefore(dueDate)) {
            checkoutDate = checkoutDate.plusYears(1);
            numberOfHolidays += holidays.getTotalHolidays();

            // This logic avoids double counting holidays that will be checked in the next section of code that will go month by month for the last year
            if (!checkoutDate.plusYears(1).isBefore(dueDate)) {
                Month month = checkoutDate.getMonth();
                Set<Holiday> holidaysSet = holidays.getAllHolidaysInMonth(month);
                if (holidaysSet != null) {
                    for(Holiday holiday : holidaysSet) {
                        if (checkoutDate.isBefore(holiday.getDateInYear(checkoutDate.getYear()))) {
                            numberOfHolidays--;
                        }
                    }
                }
            }
        }

        // Could combine logics with different looping logic, but prefer how straightforward splitting them up is to understand
        if(checkoutDate.getYear() < dueDate.getYear()) {
            for(int month = checkoutDate.getMonthValue(); month <= Month.DECEMBER.getValue(); month++) {
                Set<Holiday> holidaysSet = holidays.getAllHolidaysInMonth(Month.of(month));
                if(holidaysSet != null) {
                    for(Holiday holiday : holidaysSet) {
                        LocalDate holidayDate = holiday.getDateInYear(checkoutDate.getYear());
                        if(holidayDate.isAfter(checkoutDate)) {
                            numberOfHolidays++;
                        }
                    }
                }
            }

            for(int month = Month.JANUARY.getValue(); month <= dueDate.getMonthValue(); month++) {
                Set<Holiday> holidaysSet = holidays.getAllHolidaysInMonth(Month.of(month));
                if(holidaysSet != null) {
                    for(Holiday holiday : holidaysSet) {
                        LocalDate holidayDate = holiday.getDateInYear(dueDate.getYear());
                        //isBefore or isEqual === !isAfter
                        if(!holidayDate.isAfter(dueDate)) {
                            numberOfHolidays++;
                        }
                    }
                }
            }
        } else {
            for(int month = checkoutDate.getMonthValue(); month <= dueDate.getMonthValue(); month++) {
                Set<Holiday> holidaysSet = holidays.getAllHolidaysInMonth(Month.of(month));
                if(holidaysSet != null) {
                    for(Holiday holiday : holidaysSet) {
                        LocalDate holidayDate = holiday.getDateInYear(checkoutDate.getYear());
                        //isBefore or isEqual === !isAfter
                        if(holidayDate.isAfter(checkoutDate) && !holidayDate.isAfter(dueDate)) {
                            numberOfHolidays++;
                        }
                    }
                }
            }
        }
        return numberOfHolidays;
    }
}
