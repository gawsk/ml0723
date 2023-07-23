import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;

public class CheckoutServiceImpl {
    public RentalAgreement checkout(String toolCode, int rentalDayCount, int discount, Calendar checkoutDate) throws Exception {
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

        Calendar dueDate = Calendar.getInstance();
        dueDate.setTime(checkoutDate.getTime());
        dueDate.add(Calendar.DAY_OF_MONTH, rentalDayCount);

        int chargeableDays = rentalDayCount;
        if(!tool.getToolType().getWeekendCharge() && chargeableDays > 0) {
            chargeableDays -= getNumberOfWeekends(checkoutDate, rentalDayCount);
        }

        if (!tool.getToolType().getHolidayCharge() && chargeableDays > 0) {
            chargeableDays -= getNumberOfHolidays(checkoutDate, rentalDayCount, dueDate);
        }

        // Sanity check to not have chargeableDays be negative
        chargeableDays = Math.max(0, chargeableDays);

        BigDecimal preDiscountCharge = new BigDecimal(tool.getToolType().getDailyCharge() * chargeableDays);
        preDiscountCharge = preDiscountCharge.setScale(2, RoundingMode.UP);

        BigDecimal discountAmount = preDiscountCharge.multiply(new BigDecimal(discount / 100d));
        discountAmount = discountAmount.setScale(2, RoundingMode.UP);

        BigDecimal finalCharge = preDiscountCharge.subtract(discountAmount);

        return new RentalAgreement(tool, rentalDayCount, checkoutDate, dueDate, chargeableDays, preDiscountCharge.floatValue(),
                                    discount, discountAmount.floatValue(), finalCharge.floatValue());
    }

    private int getNumberOfWeekends(Calendar checkoutDate, int rentalDayCount) {
        int numberOfWeekends = (rentalDayCount / 7) * 2;
        int remainder = rentalDayCount % 7;
        if (remainder > 0) {
            //TODO: Think of better implementation
            //This is okay because it's O(1), but there's likely a cleaner way to implement
            for (int i = 0; i < remainder; i++) {
                checkoutDate.add(Calendar.DAY_OF_WEEK, 1);
                if(checkoutDate.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || checkoutDate.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    numberOfWeekends++;
                }
            }
        }
        return numberOfWeekends;
    }

    private int getNumberOfHolidays(Calendar checkoutDate, int rentalDayCount, Calendar dueDate) {
        //Logic for a year would be different than for less than a year
        int numberOfHolidays = 0;
        if(rentalDayCount <= 365) {

        } else {

        }

        return numberOfHolidays;
    }
}
