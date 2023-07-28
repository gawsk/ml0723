package Objects;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;

public class DynamicDateHoliday implements Holiday {
    private String name;
    private Month month;
    private TemporalAdjuster dayOfWeekInMonth;

    public DynamicDateHoliday(){}

    public DynamicDateHoliday(int weekNumber, Month month, DayOfWeek dayOfWeek, String name) {
        this.month = month;
        dayOfWeekInMonth = TemporalAdjusters.dayOfWeekInMonth(weekNumber, dayOfWeek);
        this.name = name;
    }

    public DynamicDateHoliday(Month month, TemporalAdjuster dayOfWeekInMonth, String name) {
        this.month = month;
        this.dayOfWeekInMonth = dayOfWeekInMonth;
        this.name = name;
    }

    public LocalDate getDateInYear(int year) {
        //Using a default day since it will be changed
        LocalDate dateInYear = LocalDate.of(year, month, 1);
        return dateInYear.with(dayOfWeekInMonth);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public TemporalAdjuster getDayOfWeekInMonth() {
        return dayOfWeekInMonth;
    }

    public void setDayOfWeekInMonth(TemporalAdjuster dayOfWeekInMonth) {
        this.dayOfWeekInMonth = dayOfWeekInMonth;
    }
}
