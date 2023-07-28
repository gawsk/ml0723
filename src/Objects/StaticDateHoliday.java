package Objects;
import java.time.DayOfWeek;
import java.time.LocalDate;

public class StaticDateHoliday implements Holiday {
    private String name;
    private LocalDate staticDate;
    private boolean observedOnWeekend;

    public StaticDateHoliday(){}

    public StaticDateHoliday(LocalDate staticDate, boolean observedOnWeekend, String name) {
        this.staticDate = staticDate;
        this.observedOnWeekend = observedOnWeekend;
        this.name = name;
    }

    public LocalDate getDateInYear(int year) {
        LocalDate dateInYear = staticDate.withYear(year);
        if(!observedOnWeekend) {
            if(dateInYear.getDayOfWeek() == DayOfWeek.SATURDAY) {
                dateInYear = dateInYear.minusDays(1);
            } else if (dateInYear.getDayOfWeek() == DayOfWeek.SUNDAY) {
                dateInYear = dateInYear.plusDays(1);
            }
        }
        return dateInYear;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStaticDate() {
        return staticDate;
    }

    public void setStaticDate(LocalDate staticDate) {
        this.staticDate = staticDate;
    }

    public boolean isObservedOnWeekend() {
        return observedOnWeekend;
    }

    public void setObservedOnWeekend(boolean observedOnWeekend) {
        this.observedOnWeekend = observedOnWeekend;
    }    
}
