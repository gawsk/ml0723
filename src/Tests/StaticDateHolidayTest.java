package Tests;

import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.Test;

import Objects.StaticDateHoliday;

public class StaticDateHolidayTest {
    
    @Test
    public void getDateInYear_NotObservedOnWeekend_Weekday() {
        StaticDateHoliday fridayJulyThird = new StaticDateHoliday(LocalDate.of(2020, 7, 3), false, "July 3");
        assertTrue(fridayJulyThird.getDateInYear(2020).isEqual(LocalDate.of(2020, 7, 3)));
    }

    @Test
    public void getDateInYear_NotObservedOnWeekend_Saturday() {
        StaticDateHoliday saturdayJulyFourth = new StaticDateHoliday(LocalDate.of(2020, 7, 4), false, "July 4");
        assertTrue(saturdayJulyFourth.getDateInYear(2020).isEqual(LocalDate.of(2020, 7, 3)));
    }

    @Test
    public void getDateInYear_NotObservedOnWeekend_Sunday() {
        StaticDateHoliday sundayJulyFifth = new StaticDateHoliday(LocalDate.of(2020, 7, 5), false, "July 5");
        assertTrue(sundayJulyFifth.getDateInYear(2020).isEqual(LocalDate.of(2020, 7, 6)));
    }

    @Test
    public void getDateInYear_ObservedOnWeekend_Weekend() {
        StaticDateHoliday saturdayJulyFourth = new StaticDateHoliday(LocalDate.of(2020, 7, 4), true, "July 4");
        assertTrue(saturdayJulyFourth.getDateInYear(2020).isEqual(LocalDate.of(2020, 7, 4)));
    }

}
