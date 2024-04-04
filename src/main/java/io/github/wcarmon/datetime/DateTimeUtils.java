package io.github.wcarmon.datetime;

import static java.util.Objects.requireNonNull;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;

/**
 * Simplify Core Java date/time usage
 */
public final class DateTimeUtils {

    private DateTimeUtils() {
    }

    /**
     * @return latest start-of-day (midnight) before ts, in the passed timezone
     */
    //TODO: add tests
    public static Instant getStartOfDay(
            Instant ts,
            ZoneId zoneId) {

        return ZonedDateTime.ofInstant(
                        ts, zoneId)
                .truncatedTo(ChronoUnit.DAYS)
                .toInstant();
    }

    /**
     * @param startDate
     * @param n
     * @param holidayPredicate
     * @return
     */
    // TODO: add tests
    public static LocalDate nBusinessDaysAgo(
            LocalDate startDate,
            int n,
            @Nullable Predicate<LocalDate> holidayPredicate) {

        requireNonNull(startDate, "startDate is required and null.");
        if (n < 0) {
            throw new IllegalArgumentException("n must be >= 0");
        }

        if (n == 0) {
            return startDate;
        }

        LocalDate current = startDate;
        for (int i = 0; i < n; i++) {
            current = previousBusinessDay(current, holidayPredicate);
        }

        return current;
    }

    // TODO: add tests
    public static LocalDate nWeekDaysAgo(LocalDate startDate, int n) {
        requireNonNull(startDate, "startDate is required and null.");
        if (n < 0) {
            throw new IllegalArgumentException("n must be >= 0");
        }

        if (n == 0) {
            return startDate;
        }

        LocalDate current = startDate;
        for (int i = 0; i < n; i++) {
            current = previousWeekDay(current);
        }

        return current;
    }

    /**
     * @param startDate
     * @param holidayPredicate allows control over holidays
     * @return a non-holiday, weekday in the past
     */
    public static LocalDate previousBusinessDay(
            LocalDate startDate,
            @Nullable Predicate<LocalDate> holidayPredicate) {

        requireNonNull(startDate, "startDate is required and null.");

        final Predicate<LocalDate> isHoliday = holidayPredicate == null
                ? dt -> false
                : holidayPredicate;

        LocalDate prev = previousWeekDay(startDate);
        // -- For US holidays, never iterates more than 4 times
        while (isHoliday.test(prev)) {
            prev = previousWeekDay(prev);
        }

        return prev;
    }

    /**
     * @param startDate
     * @return previous weekday
     */
    public static LocalDate previousWeekDay(LocalDate startDate) {
        requireNonNull(startDate, "startDate is required and null.");

        switch (startDate.getDayOfWeek()) {
            case SUNDAY:
                return startDate.minusDays(2);

            case MONDAY:
                return startDate.minusDays(3);

            case TUESDAY,
                    WEDNESDAY,
                    THURSDAY,
                    FRIDAY,
                    SATURDAY:
                return startDate.minusDays(1);

            default:
                throw new IllegalStateException("Invalid day of week: " + startDate.getDayOfWeek());
        }
    }
}
