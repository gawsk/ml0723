package Tests;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Objects.RentalAgreement;
import Objects.Tool;
import Objects.ToolType;

public class RentalAgreementTest {

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    private String toolCodeString;
    private String toolTypeString;
    private String brandString;
    private String rentalDayCountString;
    private String checkOutDateString;
    private String dueDateString;
    private String dailyRentalChargeString;
    private String chargeableDaysString;
    private String preDiscountChargeString;
    private String discountPercentString;
    private String discountAmountString;
    private String finalChargeString;
    private Tool tool;
    private ToolType toolType;
    private String agreement;

    @Before
    public void init() {
        agreement = String.join(System.lineSeparator(),
                "Tool code: %s",
                "Tool type: %s",
                "Tool brand: %s",
                "Rental Days: %s",
                "Check out date: %s",
                "Due date: %s",
                "Daily rental charge: $%s",
                "Charge days: %s",
                "Pre-discount charge: $%s",
                "Discount percent: %s%%",
                "Discount Amount: $%s",
                "Final charge: $%s");
        toolCodeString = "CODE";
        toolTypeString = "ToolType";
        brandString = "Brand";
        rentalDayCountString = "1";
        checkOutDateString = "07/28/23";
        dueDateString = "07/29/23";
        dailyRentalChargeString = "1.00";
        chargeableDaysString = "1";
        preDiscountChargeString = "1.00";
        discountPercentString = "0";
        discountAmountString = "0.00";
        finalChargeString = "1.00";

        toolType = new ToolType(toolTypeString, 1, false, false, false);
        tool = new Tool(toolCodeString, toolType, brandString);

        System.setOut(new PrintStream(outputStream));
    }

    @After
    public void cleanUp() {
        System.setOut(standardOut);
    }

    @Test
    public void commasPresentInLargeNumbers() {
        toolType = new ToolType(toolTypeString, 1000000, false, false, false);
        tool.setToolType(toolType);
        RentalAgreement rentalAgreement = new RentalAgreement(tool, 1, LocalDate.of(2023, 7, 28),
                LocalDate.of(2023, 7, 29), 1, 1000000f, 0, 0f, 1000000f);
        rentalAgreement.printAgreementValues();
        dailyRentalChargeString = "1,000,000.00";
        preDiscountChargeString = "1,000,000.00";
        finalChargeString = "1,000,000.00";
        assertEquals(formattedAgreement(), outputStream.toString());
    }

    @Test
    public void zeroAfterDecimalNumbers() {
        RentalAgreement rentalAgreement = new RentalAgreement(tool, 1, LocalDate.of(2023, 7, 28),
                LocalDate.of(2023, 7, 29), 1, 1f, 0, 0f, 1f);
        rentalAgreement.printAgreementValues();
        assertEquals(formattedAgreement(), outputStream.toString());
    }

    @Test
    public void oneAfterDecimalNumbers() {
        toolType = new ToolType(toolTypeString, 1.1f, false, false, false);
        tool.setToolType(toolType);
        RentalAgreement rentalAgreement = new RentalAgreement(tool, 1, LocalDate.of(2023, 7, 28),
                LocalDate.of(2023, 7, 29), 1, 1.1f, 0, 0f, 1.1f);
        rentalAgreement.printAgreementValues();
        dailyRentalChargeString = "1.10";
        preDiscountChargeString = "1.10";
        finalChargeString = "1.10";
        assertEquals(formattedAgreement(), outputStream.toString());
    }

    @Test
    public void twoAfterDecimalNumbers() {
        toolType = new ToolType(toolTypeString, 1.11f, false, false, false);
        tool.setToolType(toolType);
        RentalAgreement rentalAgreement = new RentalAgreement(tool, 1, LocalDate.of(2023, 7, 28),
                LocalDate.of(2023, 7, 29), 1, 1.11f, 0, 0f, 1.11f);
        rentalAgreement.printAgreementValues();
        dailyRentalChargeString = "1.11";
        preDiscountChargeString = "1.11";
        finalChargeString = "1.11";
        assertEquals(formattedAgreement(), outputStream.toString());
    }

    @Test
    public void dateCorrectFormat() {
        RentalAgreement rentalAgreement = new RentalAgreement(tool, 1, LocalDate.of(1999, 1, 1),
                LocalDate.of(2001, 12, 31), 1, 1f, 0, 0f, 1f);
        rentalAgreement.printAgreementValues();
        checkOutDateString = "01/01/99";
        dueDateString = "12/31/01";
        assertEquals(formattedAgreement(), outputStream.toString());
    }

    private String formattedAgreement() {
        return String.format(agreement + "%s", toolCodeString, toolTypeString, brandString, rentalDayCountString,
                checkOutDateString, dueDateString, dailyRentalChargeString, chargeableDaysString,
                preDiscountChargeString,
                discountPercentString, discountAmountString, finalChargeString, System.lineSeparator());
    }
}
