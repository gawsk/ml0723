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
            chargeableDays -= getNumberOfWeekends(checkoutDate, dueDate);
        }

        if (!tool.getToolType().getHolidayCharge() && chargeableDays > 0) {
            chargeableDays -= getNumberOfHolidays(checkoutDate, dueDate);
        }

        BigDecimal preDiscountCharge = new BigDecimal(tool.getToolType().getDailyCharge() * chargeableDays);
        preDiscountCharge = preDiscountCharge.setScale(2, RoundingMode.UP);

        BigDecimal discountAmount = preDiscountCharge.multiply(new BigDecimal(discount / 100d));
        discountAmount = discountAmount.setScale(2, RoundingMode.UP);

        BigDecimal finalCharge = preDiscountCharge.subtract(discountAmount);

        return new RentalAgreement(tool, rentalDayCount, checkoutDate, dueDate, chargeableDays, preDiscountCharge.floatValue(),
                                    discount, discountAmount.floatValue(), finalCharge.floatValue());
    }

    private int getNumberOfWeekends(Calendar checkoutDate, Calendar dueDate) {
        return 0;
    }

    private int getNumberOfHolidays(Calendar checkoutDate, Calendar dueDate) {
        return 0;
    }
}
