package Tests;

import static org.junit.Assert.assertTrue;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;

import org.junit.Test;

import Objects.DynamicDateHoliday;

public class DynamicDateHolidayTest {
    
    @Test
    public void firstDayInMonth() {
        DynamicDateHoliday firstMondayOfApril = new DynamicDateHoliday(1, Month.APRIL, DayOfWeek.MONDAY, "First Monday of April");
        LocalDate firstMondayOfApril2020 = firstMondayOfApril.getDateInYear(2020);
        assertTrue(firstMondayOfApril2020.isEqual(LocalDate.of(2020, 4, 6)));
    }

    @Test
    public void lastDayInMonth() {
        DynamicDateHoliday lastMondayOfApril = new DynamicDateHoliday(-1, Month.APRIL, DayOfWeek.MONDAY, "Last Monday of April");
        LocalDate lastMondayOfApril2020 = lastMondayOfApril.getDateInYear(2020);
        assertTrue(lastMondayOfApril2020.isEqual(LocalDate.of(2020, 4, 27)));
    }
}
