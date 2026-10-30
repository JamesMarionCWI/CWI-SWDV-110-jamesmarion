package edu.cwi.swdv110.calendar;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/** Static date helpers used by Semester and App. */
public final class DateTools {
    // Part 1: a formatter for "Mon, Jan 11" (pattern letters: EEE, MMM, d)
    private static final DateTimeFormatter SHORT_FORMAT = DateTimeFormatter.ofPattern("EEE, MMM d", Locale.US);
    // Part 2: a formatter for "Mon, Jan 11 at 6:00 PM MST"
    // (hint: literal text goes in single quotes: 'at')
    private static final DateTimeFormatter LONG_FORMAT = DateTimeFormatter.ofPattern("EEE, MMM d 'at' h:mm a z", Locale.US);
    // Part 2: a List<DateTimeFormatter> of the three accepted input formats:
    // 2027-03-15 3/15/2027 Mar 15, 2027
    // Always pass Locale.US to ofPattern so "Mon"/"PM" don't change on other computers.
    // For the INPUT formatters use "uuuu" (not "yyyy") for the year and add
    // .withResolverStyle(ResolverStyle.STRICT) – see the Part 2 notes for why.
    private static final List<DateTimeFormatter> FORMATS_LIST = new ArrayList<>(List.of(
            DateTimeFormatter.ofPattern("uuuu-MM-d", Locale.US).withResolverStyle(ResolverStyle.STRICT),
            DateTimeFormatter.ofPattern("M/d/uuuu", Locale.US).withResolverStyle(ResolverStyle.STRICT),
            DateTimeFormatter.ofPattern("MMM d, uuuu", Locale.US).withResolverStyle(ResolverStyle.STRICT)
        ));

    private DateTools() { }

    // ---------------- Part 1 ----------------

    /** The nth given weekday of a month, e.g. (2027, JANUARY, 3, MONDAY) -> 2027-01-18 (MLK Day). */
    public static LocalDate nthWeekday(int year, Month month, int n, DayOfWeek day) {
        return LocalDate.of(year, month, 1).with(TemporalAdjusters.dayOfWeekInMonth(n, day));
    }

    /** The last given weekday of a month, e.g. (2027, MAY, MONDAY) -> 2027-05-31 (Memorial Day). */
    public static LocalDate lastWeekday(int year, Month month, DayOfWeek day) {
        return LocalDate.of(year, month, 1).with(TemporalAdjusters.lastInMonth(day));
    }

    /** "Mon, Jan 11" */
    public static String formatShort(LocalDate date) {
        return date.format(SHORT_FORMAT);
    }

    /** True for Saturday and Sunday. */
    public static boolean isWeekend(LocalDate date) {
        return date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY;
    }

    // ---------------- Part 2 ----------------

    /**
    * Accepts "2027-03-15", "3/15/2027" or "Mar 15, 2027" (surrounding spaces allowed).
    * Anything else -> IllegalArgumentException("Unrecognized date: " + text).
    * Hint: try each formatter in turn and catch DateTimeParseException.
    */
    public static LocalDate parseFlexible(String text) throws IllegalArgumentException {
        LocalDate result = null;
        for(int i = 0; result == null && i < FORMATS_LIST.size(); i++) {
            try {
                result = LocalDate.parse(text.trim(), FORMATS_LIST.get(i));
            } catch (DateTimeParseException ex) {
                // :')
            }
        }
        if (result == null) {
            throw new IllegalArgumentException("Unrecognized date: " + text);
        }
        return result;
    }

    /**
    * Counts forward `days` school days from `start` (start itself is not counted).
    * Weekends and dates in `holidays` are skipped.
    * Fri 2027-01-08 + 1 school day -> Mon 2027-01-11
    */
    public static LocalDate addSchoolDays(LocalDate start, int days, Set<LocalDate> holidays) {
        int validDayCount = 0;
        LocalDate endDate = start;
        while(validDayCount < days) {
            endDate = endDate.plusDays(1);
            if(!isWeekend(endDate) && !holidays.contains(endDate)) {
                validDayCount++;
            }
        }
        return endDate;
    }

    /**
    * Late policy: 10% off for each 24 hours (or part of 24 hours) late, maximum 50%.
    * on time or early -> 0
    * 1 minute late -> 10
    * exactly 24 hours late -> 10
    * 24 hours + 1 minute -> 20
    * a week late -> 50
    * Hint: Duration.between(...).toMinutes()
    */
    private static int ceilingMinutesToDay(int minutes) {
        int minutesInADay = 24 * 60; // 1440
        return (minutes + minutesInADay - 1)/minutesInADay;
    }
    public static int latePenaltyPercent(LocalDateTime due, LocalDateTime submitted) {
        final int MIN_GRADE = 0;
        final int MAX_GRADE = 50;
        final int PERCENT_GRADE_FACTOR = 10;

        return Math.min(MAX_GRADE, Math.max(MIN_GRADE, (ceilingMinutesToDay((int)Duration.between(due, submitted).toMinutes()))*PERCENT_GRADE_FACTOR));
    }

    /** Converts to another zone and formats: "Tue, Jan 12 at 10:00 AM JST" */
    public static String inZone(ZonedDateTime when, ZoneId zone) {
        // withZoneSameInstant
        return when.withZoneSameInstant(zone).format(LONG_FORMAT);
    }
}
