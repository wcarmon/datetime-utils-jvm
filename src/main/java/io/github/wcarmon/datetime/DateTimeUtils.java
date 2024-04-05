package io.github.wcarmon.datetime;

import static java.util.Objects.requireNonNull;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.function.Predicate;
import org.jetbrains.annotations.Nullable;

/** Simplify Core Java date/time usage */
public final class DateTimeUtils {

    private DateTimeUtils() {}

    /**
     * Derive the instant for the start of the date within the passed timezone.
     *
     * @param ts timestamp at or after the return value
     * @param zoneId timezone used to determine when the day starts
     * @return latest start-of-day (midnight) before ts, in the passed timezone
     */
    // TODO: add tests
    public static Instant getStartOfDay(Instant ts, ZoneId zoneId) {
        requireNonNull(ts, "ts is required and null.");
        requireNonNull(zoneId, "zoneId is required and null.");

        return ZonedDateTime.ofInstant(ts, zoneId).truncatedTo(ChronoUnit.DAYS).toInstant();
    }

    /**
     * Derive a date that is n business days in the past. (a weekday and not a holiday).
     *
     * @param startDate current or starting point for calculation
     * @param n how many business days to go back
     * @param holidayPredicate determines which dates are holidays
     * @return a business date (a weekday and not a holiday).
     */
    // TODO: add tests
    public static LocalDate nBusinessDaysAgo(
            LocalDate startDate, int n, @Nullable Predicate<LocalDate> holidayPredicate) {

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

    /**
     * Derive a date that is n weekdays in the past. Does not consider holidays.
     *
     * @param startDate current or starting point for calculation
     * @param n how many week days to go back
     * @return a weekday in the past
     */
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
     * Derive the previous business day (a weekday and not a holiday). Accepts configurable holidays
     * via predicate argument.
     *
     * @param startDate current or starting point for calculation
     * @param holidayPredicate determines which dates are holidays
     * @return a non-holiday, weekday in the past
     */
    public static LocalDate previousBusinessDay(
            LocalDate startDate, @Nullable Predicate<LocalDate> holidayPredicate) {

        requireNonNull(startDate, "startDate is required and null.");

        final Predicate<LocalDate> isHoliday =
                holidayPredicate == null ? dt -> false : holidayPredicate;

        LocalDate prev = previousWeekDay(startDate);
        // -- For US holidays, never iterates more than 4 times
        while (isHoliday.test(prev)) {
            prev = previousWeekDay(prev);
        }

        return prev;
    }

    /**
     * Derive the previous weekday. Does not consider holidays.
     *
     * @param startDate current or starting point for calculation
     * @return previous weekday
     */
    public static LocalDate previousWeekDay(LocalDate startDate) {
        requireNonNull(startDate, "startDate is required and null.");

        switch (startDate.getDayOfWeek()) {
            case SUNDAY:
                return startDate.minusDays(2);

            case MONDAY:
                return startDate.minusDays(3);

            case TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY:
                return startDate.minusDays(1);

            default:
                throw new IllegalStateException("Invalid day of week: " + startDate.getDayOfWeek());
        }
    }
}
