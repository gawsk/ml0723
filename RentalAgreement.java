import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RentalAgreement {
    private String toolCode;
    private ToolType toolType;
    private String brand;
    private int rentalDayCount;
    private Calendar checkoutDate;
    private Calendar dueDate;
    private float dailyCharge;
    private int chargeableDays;
    private float preDiscountCharge;
    private int discountPercent;
    private float discountAmount;
    private float finalCharge;
    private final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");

    // ******************************* Constructers *******************************

    public RentalAgreement(){}

    public RentalAgreement(Tool tool, int rentalDayCount, Calendar checkoutDate, Calendar dueDate, int chargeableDays, float preDiscountCharge, int discountPercent,
                            float discountAmount, float finalCharge) {
        this.toolCode = tool.getToolCode();
        this.toolType = tool.getToolType();
        this.brand = tool.getBrand();
        this.rentalDayCount = rentalDayCount;
        this.checkoutDate = checkoutDate;
        this.dueDate = dueDate;
        this.dailyCharge = tool.getToolType().getDailyCharge();
        this.chargeableDays = chargeableDays;
        this.preDiscountCharge = preDiscountCharge;
        this.discountPercent = discountPercent;
        this.discountAmount = discountAmount;
        this.finalCharge = finalCharge;
    }

    // ******************************* Print Method *******************************

    public void printAgreementValues(){

        //TODO: do I want to do rounding in here and store the larger decimal, or have checkout save the 2 floating point decimal?
        System.out.println(String.format("Tool code: %s", toolCode));
        System.out.println(String.format("Tool type: %s", toolType.getName()));
        System.out.println(String.format("Tool brand: %s", brand));
        System.out.println(String.format("Rental Days: %d", rentalDayCount));
        System.out.println(String.format("Check out date: %s", sdf.format(checkoutDate.getTime())));
        System.out.println(String.format("Due date: %s", sdf.format(dueDate.getTime())));
        System.out.println(String.format("Daily rental charge: $%.2f", toolType.getDailyCharge()));
        System.out.println(String.format("Charge days: %d", chargeableDays));
        System.out.println(String.format("Pre-discount charge: $%.2f", preDiscountCharge));
        System.out.println(String.format("Discount percent: %d%%", discountPercent));
        System.out.println(String.format("Discount Amount: $%.2f", discountAmount));
        System.out.println(String.format("Final charge: $%.2f", finalCharge));
    }

    // ******************************* Getters and Setters *******************************

    public String getToolCode() {
        return toolCode;
    }

    public void setToolCode(String toolCode) {
        this.toolCode = toolCode;
    }

    public ToolType getToolType() {
        return toolType;
    }

    public void setToolType(ToolType toolType) {
        this.toolType = toolType;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getRentalDayCount() {
        return rentalDayCount;
    }

    public void setRentalDayCount(int rentalDayCount) {
        this.rentalDayCount = rentalDayCount;
    }

    public Calendar getCheckoutDate() {
        return checkoutDate;
    }

    public void setCheckoutDate(Calendar checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    public Calendar getDueDate() {
        return dueDate;
    }

    public void setDueDate(Calendar dueDate) {
        this.dueDate = dueDate;
    }

    public float getDailyCharge() {
        return dailyCharge;
    }

    public void setDailyCharge(float dailyCharge) {
        this.dailyCharge = dailyCharge;
    }

    public int getChargeableDays() {
        return chargeableDays;
    }

    public void setChargeableDays(int chargeableDays) {
        this.chargeableDays = chargeableDays;
    }

    public float getPreDiscountCharge() {
        return preDiscountCharge;
    }

    public void setPreDiscountCharge(float preDiscountCharge) {
        this.preDiscountCharge = preDiscountCharge;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(int discountPercent) {
        this.discountPercent = discountPercent;
    }

    public float getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(float discountAmount) {
        this.discountAmount = discountAmount;
    }

    public float getFinalCharge() {
        return finalCharge;
    }

    public void setFinalCharge(float finalCharge) {
        this.finalCharge = finalCharge;
    }

    
}
