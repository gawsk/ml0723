package Repositories;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import Objects.DynamicDateHoliday;
import Objects.Holiday;
import Objects.StaticDateHoliday;

public class HolidayRepository {
    private static HolidayRepository holidayRepository = new HolidayRepository();
    private Map<Month, Set<Holiday>> holidays;

    private HolidayRepository() {
        holidays = new HashMap<Month, Set<Holiday>>();

        LocalDate date = LocalDate.of(2023, Month.JULY, 4);
        Holiday holiday = new StaticDateHoliday(date, false, "Independence Day");
        Set<Holiday> julyHolidays = new HashSet<Holiday>();
        julyHolidays.add(holiday);
        holidays.put(Month.JULY, julyHolidays);


        TemporalAdjuster dayOfWeekInMonth = TemporalAdjusters.dayOfWeekInMonth(1, DayOfWeek.MONDAY);
        holiday = new DynamicDateHoliday(Month.SEPTEMBER, dayOfWeekInMonth, "Labor Day");
        Set<Holiday> septemberHolidays = new HashSet<Holiday>();
        septemberHolidays.add(holiday);
        holidays.put(Month.SEPTEMBER, septemberHolidays);
    }

    public static HolidayRepository getInstance() {
        return holidayRepository;
    }

    public Set<Holiday> getAllHolidaysInMonth(Month month) {
        return holidays.get(month);
    }
}
