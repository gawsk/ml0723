package Services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import Repositories.HolidayRepository;
import Repositories.ToolRepository;
import Objects.Holiday;
import Objects.RentalAgreement;
import Objects.Tool;

public class CheckoutServiceImpl {
    private ToolRepository toolRepository;
    private HolidayRepository holidayRepository;

    public CheckoutServiceImpl() {
        toolRepository = ToolRepository.getInstance();
        holidayRepository = HolidayRepository.getInstance();
    }

    public CheckoutServiceImpl(ToolRepository toolRepository, HolidayRepository holidayRepository) {
        this.toolRepository = toolRepository;
        this.holidayRepository = holidayRepository;
    }

    public RentalAgreement checkout(String toolCode, int rentalDayCount, int discount, LocalDate checkoutDate) throws Exception {
        // Data Validation
        if(rentalDayCount < 1) {
            throw new Exception(String.format("Invalid Rental day count {%d}: Rental day count must be 1 or greater.", rentalDayCount));
        }
        if(!(discount >= 0 && discount <= 100)) {
            throw new Exception(String.format("Invald Discount percent {%d}: Discount percent must be in the range 0-100", discount));
        }

        Tool tool = toolRepository.getTool(toolCode);
        if(tool == null) {
            throw new Exception(String.format("Invalid Tool code {%s}: Tool code must be valid in the database", toolCode));
        }

        LocalDate dueDate = checkoutDate.plusDays(rentalDayCount);

        int chargeableDays = rentalDayCount;
        if(!tool.getToolType().getWeekendCharge() && chargeableDays > 0) {
            chargeableDays -= getNumberOfWeekends(checkoutDate, rentalDayCount);
        }

        if (!tool.getToolType().getHolidayCharge() && chargeableDays > 0) {
            chargeableDays -= getNumberOfHolidays(checkoutDate, rentalDayCount, dueDate, tool.getToolType().getWeekendCharge());
        }


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

    private int getNumberOfHolidays(LocalDate checkoutDate, int rentalDayCount, LocalDate dueDate, boolean weekendCharge) {
        int numberOfHolidays = 0;
        LocalDate currentMonth = checkoutDate.plusDays(1);

        while (currentMonth.getYear() < dueDate.getYear() || (currentMonth.getMonthValue() <= dueDate.getMonthValue() && currentMonth.getYear() == dueDate.getYear())) {
            Set<Holiday> holidaysSet = holidayRepository.getAllHolidaysInMonth(currentMonth.getMonth());
            if (holidaysSet != null) {
                //This set makes sure we aren't double counting multiple holidays on the same date
                Set<LocalDate> holidayDates = new HashSet<LocalDate>();
                for(Holiday holiday : holidaysSet) {
                    LocalDate holidayDate = holiday.getDateInYear(currentMonth.getYear());
                    if (holidayDates.add(holidayDate)) {
                        //isBefore || isEqual == !isAfter
                        if(holidayDate.isAfter(checkoutDate) && !holidayDate.isAfter(dueDate)) {
                            if (!weekendCharge && (holidayDate.getDayOfWeek() == DayOfWeek.SATURDAY || holidayDate.getDayOfWeek() == DayOfWeek.SUNDAY)) {
                                continue;
                            }
                            numberOfHolidays++;
                        }
                    }
                }
            }
            currentMonth = currentMonth.plusMonths(1);
        }

        return numberOfHolidays;
    }
}