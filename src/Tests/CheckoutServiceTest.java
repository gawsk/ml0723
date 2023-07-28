package Tests;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import Objects.DynamicDateHoliday;
import Objects.Holiday;
import Objects.RentalAgreement;
import Objects.StaticDateHoliday;
import Repositories.HolidayRepository;
import Repositories.ToolRepository;
import Services.CheckoutServiceImpl;

public class CheckoutServiceTest {

        private CheckoutServiceImpl serviceImpl;
        private ToolRepository toolRepository;

        @Before
        public void init() {
                serviceImpl = new CheckoutServiceImpl();
                toolRepository = ToolRepository.getInstance();
        }

        @Test
        public void tooLargeDiscount() throws Exception {
                int discount = 101;

                assertThrows(String.format("Invald Discount percent {%d}: Discount percent must be in the range 0-100",
                                discount), Exception.class, () -> {
                                        serviceImpl.checkout("JAKR", 5, discount, LocalDate.now());
                                });
        }

        @Test
        public void tooSmallDiscount() throws Exception {
                int discount = -1;

                assertThrows(String.format("Invald Discount percent {%d}: Discount percent must be in the range 0-100",
                                discount), Exception.class, () -> {
                                        serviceImpl.checkout("JAKR", 5, discount, LocalDate.now());
                                });
        }

        @Test
        public void rentalDayCountTooSmall() throws Exception {
                int rentalDayCount = 0;

                assertThrows(String.format("Invalid Rental day count {%d}: Rental day count must be 1 or greater.",
                                rentalDayCount), Exception.class, () -> {
                                        serviceImpl.checkout("JAKR", rentalDayCount, 0, LocalDate.now());
                                });
        }

        @Test
        public void noHolidayCharge_SingleWeekendHolidayInRange() throws Exception {
                String toolCode = "LADW";
                int rentalDayCount = 3;
                int discount = 10;
                LocalDate checkoutDate = LocalDate.of(2020, 7, 2);

                RentalAgreement rentalAgreementCheckout = serviceImpl.checkout(toolCode, rentalDayCount, discount,
                                checkoutDate);

                RentalAgreement rentalAgreementAssert = new RentalAgreement(toolRepository.getTool(toolCode),
                                rentalDayCount,
                                checkoutDate, checkoutDate.plusDays(rentalDayCount), 2, 3.98f, discount, 0.40f, 3.58f);

                assertThat(rentalAgreementCheckout).usingRecursiveComparison().isEqualTo(rentalAgreementAssert);
        }

        @Test
        public void noHolidayCharge_MultiHolidayInRange() throws Exception {
                String toolCode = "LADW";
                int rentalDayCount = 70;
                int discount = 10;
                LocalDate checkoutDate = LocalDate.of(2020, 7, 2);

                RentalAgreement rentalAgreementCheckout = serviceImpl.checkout(toolCode, rentalDayCount, discount,
                                checkoutDate);

                RentalAgreement rentalAgreementAssert = new RentalAgreement(toolRepository.getTool(toolCode),
                                rentalDayCount,
                                checkoutDate, checkoutDate.plusDays(rentalDayCount), 68, 135.32f, discount, 13.53f,
                                121.79f);

                assertThat(rentalAgreementCheckout).usingRecursiveComparison().isEqualTo(rentalAgreementAssert);
        }

        @Test
        public void noHolidayCharge_MultipleHolidaysOnSameDate() throws Exception {
                String toolCode = "LADW";
                int rentalDayCount = 3;
                int discount = 10;
                LocalDate checkoutDate = LocalDate.of(2020, 7, 2);
                HolidayRepository mockHolidayRepository = mock(HolidayRepository.class);

                Set<Holiday> holidaySet = new HashSet<Holiday>();
                holidaySet.add(new StaticDateHoliday(LocalDate.of(2020, Month.JULY, 4), false, "Fourth of July"));
                holidaySet.add(new DynamicDateHoliday(1, Month.JULY, DayOfWeek.FRIDAY, "First Friday of July"));

                when(mockHolidayRepository.getAllHolidaysInMonth(Month.JULY)).thenReturn(holidaySet);

                serviceImpl = new CheckoutServiceImpl(toolRepository, mockHolidayRepository);

                RentalAgreement rentalAgreementCheckout = serviceImpl.checkout(toolCode, rentalDayCount, discount,
                                checkoutDate);

                RentalAgreement rentalAgreementAssert = new RentalAgreement(toolRepository.getTool(toolCode),
                                rentalDayCount,
                                checkoutDate, checkoutDate.plusDays(rentalDayCount), 2, 3.98f, discount, 0.40f, 3.58f);

                assertThat(rentalAgreementCheckout).usingRecursiveComparison().isEqualTo(rentalAgreementAssert);
        }

        @Test
        public void noWeekendCharge_SingleWeekendHolidayInRange() throws Exception {
                String toolCode = "CHNS";
                int rentalDayCount = 5;
                int discount = 25;
                LocalDate checkoutDate = LocalDate.of(2015, 7, 2);

                RentalAgreement rentalAgreementCheckout = serviceImpl.checkout(toolCode, rentalDayCount, discount,
                                checkoutDate);

                RentalAgreement rentalAgreementAssert = new RentalAgreement(toolRepository.getTool(toolCode),
                                rentalDayCount,
                                checkoutDate, checkoutDate.plusDays(rentalDayCount), 3, 4.47f, discount, 1.12f, 3.35f);

                assertThat(rentalAgreementCheckout).usingRecursiveComparison().isEqualTo(rentalAgreementAssert);
        }

        @Test
        public void noWeekendCharge_MultiWeekendInRange() throws Exception {
                String toolCode = "CHNS";
                int rentalDayCount = 8;
                int discount = 25;
                LocalDate checkoutDate = LocalDate.of(2015, 7, 3);

                RentalAgreement rentalAgreementCheckout = serviceImpl.checkout(toolCode, rentalDayCount, discount,
                                checkoutDate);

                RentalAgreement rentalAgreementAssert = new RentalAgreement(toolRepository.getTool(toolCode),
                                rentalDayCount,
                                checkoutDate, checkoutDate.plusDays(rentalDayCount), 5, 7.45f, discount, 1.86f, 5.59f);

                assertThat(rentalAgreementCheckout).usingRecursiveComparison().isEqualTo(rentalAgreementAssert);
        }

        @Test
        public void noHolidayChargeAndNoWeekendCharge_SingleWeekendAndSingleHolidayInRange() throws Exception {
                String toolCode = "JAKD";
                int rentalDayCount = 6;
                int discount = 0;
                LocalDate checkoutDate = LocalDate.of(2015, 9, 3);

                RentalAgreement rentalAgreementCheckout = serviceImpl.checkout(toolCode, rentalDayCount, discount,
                                checkoutDate);

                RentalAgreement rentalAgreementAssert = new RentalAgreement(toolRepository.getTool(toolCode),
                                rentalDayCount,
                                checkoutDate, checkoutDate.plusDays(rentalDayCount), 3, 8.97f, discount, 0f, 8.97f);

                assertThat(rentalAgreementCheckout).usingRecursiveComparison().isEqualTo(rentalAgreementAssert);
        }

        @Test
        public void noHolidayChargeAndNoWeekendCharge_MultiWeekendAndSingleWeekendHolidayInRange_NotObservedOnWeekend() throws Exception {
                String toolCode = "JAKR";
                int rentalDayCount = 9;
                int discount = 0;
                LocalDate checkoutDate = LocalDate.of(2015, 7, 2);

                RentalAgreement rentalAgreementCheckout = serviceImpl.checkout(toolCode, rentalDayCount, discount,
                                checkoutDate);

                RentalAgreement rentalAgreementAssert = new RentalAgreement(toolRepository.getTool(toolCode),
                                rentalDayCount,
                                checkoutDate, checkoutDate.plusDays(rentalDayCount), 5, 14.95f, discount, 0f, 14.95f);

                assertThat(rentalAgreementCheckout).usingRecursiveComparison().isEqualTo(rentalAgreementAssert);
        }

        @Test
        public void noHolidayChargeAndNoWeekendCharge_SingleWeekendAndSingleWeekendHolidayInRange_NotObservedOnWeekend() throws Exception {
                String toolCode = "JAKR";
                int rentalDayCount = 4;
                int discount = 50;
                LocalDate checkoutDate = LocalDate.of(2020, 7, 2);

                RentalAgreement rentalAgreementCheckout = serviceImpl.checkout(toolCode, rentalDayCount, discount,
                                checkoutDate);

                RentalAgreement rentalAgreementAssert = new RentalAgreement(toolRepository.getTool(toolCode),
                                rentalDayCount,
                                checkoutDate, checkoutDate.plusDays(rentalDayCount), 1, 2.99f, discount, 1.50f, 1.49f);

                assertThat(rentalAgreementCheckout).usingRecursiveComparison().isEqualTo(rentalAgreementAssert);
        }

        @Test
        public void noHolidayChargeAndNoWeekendCharge_SingleWeekendAndSingleWeekendHolidayInRange_ObservedOnWeekend() throws Exception {
                String toolCode = "JAKR";
                int rentalDayCount = 4;
                int discount = 50;
                LocalDate checkoutDate = LocalDate.of(2020, 7, 2);
                HolidayRepository mockHolidayRepository = mock(HolidayRepository.class);

                Set<Holiday> holidaySet = new HashSet<Holiday>();
                holidaySet.add(new StaticDateHoliday(LocalDate.of(2020, Month.JULY, 4), true, "Fourth of July"));

                when(mockHolidayRepository.getAllHolidaysInMonth(Month.JULY)).thenReturn(holidaySet);

                serviceImpl = new CheckoutServiceImpl(toolRepository, mockHolidayRepository);

                RentalAgreement rentalAgreementCheckout = serviceImpl.checkout(toolCode, rentalDayCount, discount,
                                checkoutDate);

                RentalAgreement rentalAgreementAssert = new RentalAgreement(toolRepository.getTool(toolCode),
                                rentalDayCount,
                                checkoutDate, checkoutDate.plusDays(rentalDayCount), 2, 5.98f, discount, 2.99f, 2.99f);

                assertThat(rentalAgreementCheckout).usingRecursiveComparison().isEqualTo(rentalAgreementAssert);
        }

         @Test
        public void noHolidayChargeAndNoWeekendCharge_SingleWeekendAndMultiWeekendHolidayInRange_ObservedOnWeekend() throws Exception {
                String toolCode = "JAKR";
                int rentalDayCount = 4;
                int discount = 50;
                LocalDate checkoutDate = LocalDate.of(2020, 7, 2);
                HolidayRepository mockHolidayRepository = mock(HolidayRepository.class);

                Set<Holiday> holidaySet = new HashSet<Holiday>();
                holidaySet.add(new StaticDateHoliday(LocalDate.of(2020, Month.JULY, 4), true, "Fourth of July"));
                holidaySet.add(new DynamicDateHoliday(1, Month.JULY, DayOfWeek.SUNDAY, "First Sunday of July"));

                when(mockHolidayRepository.getAllHolidaysInMonth(Month.JULY)).thenReturn(holidaySet);

                serviceImpl = new CheckoutServiceImpl(toolRepository, mockHolidayRepository);

                RentalAgreement rentalAgreementCheckout = serviceImpl.checkout(toolCode, rentalDayCount, discount,
                                checkoutDate);

                RentalAgreement rentalAgreementAssert = new RentalAgreement(toolRepository.getTool(toolCode),
                                rentalDayCount,
                                checkoutDate, checkoutDate.plusDays(rentalDayCount), 2, 5.98f, discount, 2.99f, 2.99f);

                assertThat(rentalAgreementCheckout).usingRecursiveComparison().isEqualTo(rentalAgreementAssert);
        }

        @Test
        public void noHolidayChargeAndNoWeekendCharge_LeapYearRental() throws Exception {
                String toolCode = "JAKR";
                int rentalDayCount = 365;
                int discount = 0;
                LocalDate checkoutDate = LocalDate.of(2015, 7, 4);
                // Weekends: 52 + 1 day
                // Holidays: 1 (Misses both July 4th holidays)

                RentalAgreement rentalAgreementCheckout = serviceImpl.checkout(toolCode, rentalDayCount, discount,
                                checkoutDate);

                RentalAgreement rentalAgreementAssert = new RentalAgreement(toolRepository.getTool(toolCode),
                                rentalDayCount,
                                checkoutDate, checkoutDate.plusDays(rentalDayCount), 259, 774.41f, discount, 0f,
                                774.41f);

                assertThat(rentalAgreementCheckout).usingRecursiveComparison().isEqualTo(rentalAgreementAssert);
        }

        @Test
        public void noHolidayChargeAndNoWeekendCharge_MultiYearRental() throws Exception {
                String toolCode = "JAKR";
                int rentalDayCount = 730;
                int discount = 0;
                LocalDate checkoutDate = LocalDate.of(2015, 7, 4);
                // Weekends: 208 + 1 day
                // Holidays: 3 (Both Labor Days, July 4th 2016)

                RentalAgreement rentalAgreementCheckout = serviceImpl.checkout(toolCode, rentalDayCount, discount,
                                checkoutDate);

                RentalAgreement rentalAgreementAssert = new RentalAgreement(toolRepository.getTool(toolCode),
                                rentalDayCount,
                                checkoutDate, checkoutDate.plusDays(rentalDayCount), 518, 1548.82f, discount, 0f,
                                1548.82f);

                assertThat(rentalAgreementCheckout).usingRecursiveComparison().isEqualTo(rentalAgreementAssert);
        }
}