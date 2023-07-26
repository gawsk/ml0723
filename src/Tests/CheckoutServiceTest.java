package Tests;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import Objects.RentalAgreement;
import Repositories.ToolRepository;
import Services.CheckoutServiceImpl;

public class CheckoutServiceTest {

    private CheckoutServiceImpl serviceImpl;
    private ToolRepository toolRepository;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Before
    public void init() {
        serviceImpl = new CheckoutServiceImpl();
        toolRepository = ToolRepository.getInstance();
    }

    @Test
    public void tooLargeDiscount() throws Exception {
        int discount = 101;
        exceptionRule.expect(Exception.class);
        exceptionRule.expectMessage(
                String.format("Invald Discount percent {%d}: Discount percent must be in the range 0-100", discount));

        serviceImpl.checkout("JAKR", 5, discount, LocalDate.now());
    }

    @Test
    public void tooSmallDiscount() throws Exception {
        int discount = -1;
        exceptionRule.expect(Exception.class);
        exceptionRule.expectMessage(
                String.format("Invald Discount percent {%d}: Discount percent must be in the range 0-100", discount));

        serviceImpl.checkout("JAKR", 5, discount, LocalDate.now());
    }

    @Test
    public void rentalDayCountTooSmall() throws Exception {
        int rentalDayCount = 0;
        exceptionRule.expect(Exception.class);
        exceptionRule.expectMessage(
                String.format("Invalid Rental day count {%d}: Rental day count must be 1 or greater.", rentalDayCount));

        serviceImpl.checkout("JAKR", rentalDayCount, 0, LocalDate.now());
    }

    @Test
    public void noHolidayCharge_SingleWeekendHolidayInRange() throws Exception {
        String toolCode = "LADW"; // Yes Weekend Charge, No Holiday Charge
        int rentalDayCount = 3;
        int discount = 10;
        LocalDate checkoutDate = LocalDate.of(2020, 7, 2);

        RentalAgreement rentalAgreementCheckout = serviceImpl.checkout(toolCode, rentalDayCount, discount,
                checkoutDate);

        RentalAgreement rentalAgreementAssert = new RentalAgreement(toolRepository.getTool(toolCode), rentalDayCount,
                checkoutDate, checkoutDate.plusDays(rentalDayCount), 2, 3.98f, discount, 0.40f, 3.58f);

        assertThat(rentalAgreementCheckout).usingRecursiveComparison().isEqualTo(rentalAgreementAssert);
    }
}