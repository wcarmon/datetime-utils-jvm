package io.github.wcarmon.datetime;

import static io.github.wcarmon.datetime.DateTimeUtils.previousBusinessDay;
import static io.github.wcarmon.datetime.DateTimeUtils.previousWeekDay;
import static java.time.DayOfWeek.FRIDAY;
import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.DayOfWeek.THURSDAY;
import static java.time.DayOfWeek.TUESDAY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.time.LocalDate;
import java.util.function.Predicate;
import org.junit.jupiter.api.Test;

class DateTimeUtilsTest {

    // TODO: test nextBusinessDay
    // TODO: test getStartOfDay

    @Test
    void testPreviousBusinessDay() {

        final Predicate<LocalDate> holidayPredicate =
                dt -> LocalDate.of(2024, 2, 19).equals(dt) || LocalDate.of(2024, 2, 16).equals(dt);

        final LocalDate start = LocalDate.of(2024, 2, 20);
        assumeTrue(TUESDAY == start.getDayOfWeek());

        LocalDate got;
        got = previousBusinessDay(start, holidayPredicate);
        assertEquals(LocalDate.of(2024, 2, 15), got);
        assertEquals(THURSDAY, got.getDayOfWeek());
    }

    @Test
    void testpreviousWeekDay() {

        LocalDate got, input;

        // -- Monday
        input = LocalDate.of(2024, 2, 26);
        assumeTrue(MONDAY == input.getDayOfWeek());

        got = previousWeekDay(input);
        assertEquals(LocalDate.of(2024, 2, 23), got);
        assertEquals(FRIDAY, got.getDayOfWeek());

        // -- Tuesday
        input = LocalDate.of(2024, 2, 27);
        assumeTrue(TUESDAY == input.getDayOfWeek());

        got = previousWeekDay(input);
        assertEquals(LocalDate.of(2024, 2, 26), got);
        assertEquals(MONDAY, got.getDayOfWeek());

        // -- Skipping Wednesday

        // -- Skipping Thursday

        // -- Friday
        input = LocalDate.of(2024, 3, 1);
        assumeTrue(FRIDAY == input.getDayOfWeek());

        got = previousWeekDay(input);
        assertEquals(LocalDate.of(2024, 2, 29), got);
        assertEquals(THURSDAY, got.getDayOfWeek());

        // -- Saturday
        input = LocalDate.of(2024, 3, 2);
        assumeTrue(SATURDAY == input.getDayOfWeek());

        got = previousWeekDay(input);
        assertEquals(LocalDate.of(2024, 3, 1), got);
        assertEquals(FRIDAY, got.getDayOfWeek());

        // -- Sunday
        input = LocalDate.of(2024, 3, 3);
        assumeTrue(SUNDAY == input.getDayOfWeek());

        got = previousWeekDay(input);
        assertEquals(LocalDate.of(2024, 3, 1), got);
        assertEquals(FRIDAY, got.getDayOfWeek());
    }
}
