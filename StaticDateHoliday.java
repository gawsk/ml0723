import java.util.Calendar;

public class StaticDateHoliday implements Holiday {
    private Calendar date;
    private boolean observedOnWeekend;

    public StaticDateHoliday(){}

    public StaticDateHoliday(Calendar date, boolean observedOnWeekend) {
        this.date = date;
        this.observedOnWeekend = observedOnWeekend;
    }

    public Calendar getDateInYear(int year) {
        Calendar dateInYear = Calendar.getInstance();
        dateInYear.clear();
        dateInYear.set(year, date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
        if(!observedOnWeekend) {
            if(dateInYear.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                dateInYear.add(Calendar.DAY_OF_MONTH, -1);
            } else if (dateInYear.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                dateInYear.add(Calendar.DAY_OF_MONTH, 1);
            }
        }
        return dateInYear;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public boolean isObservedOnWeekend() {
        return observedOnWeekend;
    }

    public void setObservedOnWeekend(boolean observedOnWeekend) {
        this.observedOnWeekend = observedOnWeekend;
    }

    
}
