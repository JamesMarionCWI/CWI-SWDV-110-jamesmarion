package edu.cwi.swdv110.calendar;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/** One course's semester: the date range, meeting days, class time and days off. */
public class Semester {
    private final String name;
    private final LocalDate start;
    private final LocalDate end;
    private final Set<DayOfWeek> meetingDays;
    private final LocalTime classStart;
    private final LocalTime classEnd;
    private final Set<LocalDate> daysOff = new TreeSet<>();

    /**
    * Throws IllegalArgumentException if end is before start,
    * or if classEnd is not after classStart.
    */
    public Semester(String name, LocalDate start, LocalDate end, Set<DayOfWeek> meetingDays, LocalTime classStart, LocalTime classEnd) {
        // TODO validate, then assign the fields
        this.name = name;
        this.start = start;
        this.end = end;
        this.meetingDays = meetingDays;
        this.classStart = classStart;
        this.classEnd = classEnd;
    }

    public String getName() {
        return name;
    }

    public LocalDate getStart() {
        return start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public LocalTime getClassStart() {
        return classStart;
    }

    public Set<LocalDate> getDaysOff() {
        return daysOff;
    }

    public void addHoliday(LocalDate date) {
        daysOff.add(date);
    }

    /** Adds every date from first to last, INCLUDING both ends. */
    public void addBreak(LocalDate first, LocalDate last) {
        // TODO
    }

    /** Every date from start to end (inclusive) that is a meeting day and not a day off. */
    public List<LocalDate> meetingDates() {
        // TODO
        return new ArrayList<>();
    }

    /**
    * Week 1 is the Monday-to-Sunday week that contains the start date.
    * Hint: move both dates back to their Monday with previousOrSame(MONDAY),
    * then ChronoUnit.WEEKS.between(...) + 1.
    */
    public int weekNumber(LocalDate date) {
        // TODO
        return 0;
    }

    /** Length of one class meeting, e.g. 6:00 PM–8:30 PM -> PT2H30M */
    public Duration classLength() {
        // TODO
        return Duration.ZERO;
    }

    /** classLength() times the number of meetings. Hint: multipliedBy */
    public Duration totalClassTime() {
        // TODO
        return Duration.ZERO;
    }

    /** First meeting ON or AFTER `from`; null if there are no more meetings. */
    public LocalDate nextMeeting(LocalDate from) {
        // TODO
        return null;
    }

    /** Period from start to end, e.g. P3M26D */
    public Period length() {
        // TODO
        return Period.ZERO;
    }

    /** Days from `from` until the end date; 0 if the semester is already over. */
    public long daysLeft(LocalDate from) {
        // TODO
        return 0;
    }
}
