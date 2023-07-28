package Objects;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RentalAgreement {
    private Tool tool;
    private int rentalDayCount;
    private LocalDate checkoutDate;
    private LocalDate dueDate;
    private int chargeableDays;
    private float preDiscountCharge;
    private int discountPercent;
    private float discountAmount;
    private float finalCharge;

    // ******************************* Constructer *******************************

    public RentalAgreement() {
    }

    public RentalAgreement(Tool tool, int rentalDayCount, LocalDate checkoutDate, LocalDate dueDate, int chargeableDays,
            float preDiscountCharge, int discountPercent,
            float discountAmount, float finalCharge) {
        this.tool = tool;
        this.rentalDayCount = rentalDayCount;
        this.checkoutDate = checkoutDate;
        this.dueDate = dueDate;
        this.chargeableDays = chargeableDays;
        this.preDiscountCharge = preDiscountCharge;
        this.discountPercent = discountPercent;
        this.discountAmount = discountAmount;
        this.finalCharge = finalCharge;
    }

    // ******************************* Print Method *******************************

    public void printAgreementValues() {
        StringBuilder rentalAgreement = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");
        String newLine = System.lineSeparator();

        rentalAgreement.append(String.format("Tool code: %s%s", tool.getToolCode(), newLine));
        rentalAgreement.append(String.format("Tool type: %s%s", tool.getToolType().getName(), newLine));
        rentalAgreement.append(String.format("Tool brand: %s%s", tool.getBrand(), newLine));
        rentalAgreement.append(String.format("Rental Days: %d%s", rentalDayCount, newLine));
        rentalAgreement.append(String.format("Check out date: %s%s", formatter.format(checkoutDate), newLine));
        rentalAgreement.append(String.format("Due date: %s%s", formatter.format(dueDate), newLine));
        rentalAgreement.append(String.format("Daily rental charge: $%,.2f%s", tool.getToolType().getDailyCharge(), newLine));
        rentalAgreement.append(String.format("Charge days: %d%s", chargeableDays, newLine));
        rentalAgreement.append(String.format("Pre-discount charge: $%,.2f%s", preDiscountCharge, newLine));
        rentalAgreement.append(String.format("Discount percent: %d%%%s", discountPercent, newLine));
        rentalAgreement.append(String.format("Discount Amount: $%,.2f%s", discountAmount, newLine));
        rentalAgreement.append(String.format("Final charge: $%,.2f%s", finalCharge, newLine));
        System.out.print(rentalAgreement.toString());
    }

    // ******************************* Getters *******************************

    public Tool getTool() {
        return tool;
    }

    public String getToolCode() {
        return tool.getToolCode();
    }

    public ToolType getToolType() {
        return tool.getToolType();
    }

    public String getBrand() {
        return tool.getBrand();
    }

    public int getRentalDayCount() {
        return rentalDayCount;
    }

    public LocalDate getCheckoutDate() {
        return checkoutDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public float getDailyCharge() {
        return tool.getToolType().getDailyCharge();
    }

    public int getChargeableDays() {
        return chargeableDays;
    }

    public float getPreDiscountCharge() {
        return preDiscountCharge;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public float getDiscountAmount() {
        return discountAmount;
    }

    public float getFinalCharge() {
        return finalCharge;
    }
}
