package edu.cwi.swdv110.calendar;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class App {
    static final ZoneId CAMPUS = ZoneId.of("America/Boise");

    public static void main(String[] args) {
        // Example semester – these dates are for practice, not the official CWI calendar.
        Semester sem = new Semester("SWDV-110 – Spring 2027", LocalDate.of(2027, 1, 11), LocalDate.of(2027, 5, 7), Set.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY), LocalTime.of(18, 0), LocalTime.of(20, 30));
        sem.addHoliday(DateTools.nthWeekday(2027, Month.JANUARY, 3, DayOfWeek.MONDAY)); // MLK Day
        sem.addHoliday(DateTools.nthWeekday(2027, Month.FEBRUARY, 3, DayOfWeek.MONDAY)); // Presidents' Day
        sem.addBreak(LocalDate.of(2027, 3, 22), LocalDate.of(2027, 3, 26)); // Spring break

        // TODO Part 4a: print the name, a line of '=' the same length, then every meeting:
        // "Week N" (only on the first meeting of each week) and the formatted date

        // TODO Part 4b: print days off, number of meetings, total class time as "75h 0m"
        // (toHours, toMinutesPart) and the semester length as "3 months, 26 days"

        // TODO Part 4c: build a ZonedDateTime for the FIRST meeting at the class start time
        // in CAMPUS, and print it for America/Boise, America/Los_Angeles,
        // America/New_York and Asia/Tokyo using DateTools.inZone

        // TODO Part 4d: loop – read a date (any of the 3 formats) or 'quit'. Print:
        // the next class and its week number, days until the semester ends, and the
        // due date of a lab assigned at that class (5 school days, skipping days off).
        // If there are no more classes say so. Catch IllegalArgumentException for bad input.
        Scanner sc = new Scanner(System.in);
    }
}
