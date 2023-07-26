package Objects;
import java.time.DayOfWeek;
import java.time.LocalDate;

public class StaticDateHoliday implements Holiday {
    private LocalDate date;
    private boolean observedOnWeekend;

    public StaticDateHoliday(){}

    public StaticDateHoliday(LocalDate date, boolean observedOnWeekend) {
        this.date = date;
        this.observedOnWeekend = observedOnWeekend;
    }

    public LocalDate getDateInYear(int year) {
        LocalDate dateInYear = date.withYear(year);
        if(!observedOnWeekend) {
            if(dateInYear.getDayOfWeek() == DayOfWeek.SATURDAY) {
                dateInYear.minusDays(1);
            } else if (dateInYear.getDayOfWeek() == DayOfWeek.SUNDAY) {
                dateInYear.plusDays(1);
            }
        }
        return dateInYear;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean isObservedOnWeekend() {
        return observedOnWeekend;
    }

    public void setObservedOnWeekend(boolean observedOnWeekend) {
        this.observedOnWeekend = observedOnWeekend;
    }

    
}
