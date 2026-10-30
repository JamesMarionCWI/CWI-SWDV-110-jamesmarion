package edu.cwi.swdv110.calendar;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Scanner;
import java.util.Set;

public class App {
    static final ZoneId CAMPUS = ZoneId.of("America/Boise");

    public static void main(String[] args) {
        // Example semester – these dates are for practice, not the official CWI calendar.
        Semester sem = new Semester("SWDV-110 - Spring 2027", LocalDate.of(2027, 1, 11), LocalDate.of(2027, 5, 7), Set.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY), LocalTime.of(18, 0), LocalTime.of(20, 30));
        sem.addHoliday(DateTools.nthWeekday(2027, Month.JANUARY, 3, DayOfWeek.MONDAY)); // MLK Day
        sem.addHoliday(DateTools.nthWeekday(2027, Month.FEBRUARY, 3, DayOfWeek.MONDAY)); // Presidents' Day
        sem.addBreak(LocalDate.of(2027, 3, 22), LocalDate.of(2027, 3, 26)); // Spring break

        // Part 4a: print the name, a line of '=' the same length, then every meeting:
        // "Week N" (only on the first meeting of each week) and the formatted date
        System.out.println(sem.getName());
        System.out.println("=".repeat(sem.getName().length()));
        int previousWeekNumber = 0;
        for(LocalDate meeting : sem.meetingDates()) {
            int weekNumber = sem.weekNumber(meeting);
            if(previousWeekNumber != weekNumber) {
                System.out.printf("Week %d ", weekNumber);
                previousWeekNumber = weekNumber;
            }
            System.out.println(DateTools.formatShort(meeting));
        }

        // Empty line for formatting
        System.out.println();

        // Part 4b: print days off, number of meetings, total class time as "75h 0m"
        // (toHours, toMinutesPart) and the semester length as "3 months, 26 days"
        System.out.printf("Days off: %d%n", sem.getDaysOff().size());
        System.out.printf("Meetings: %d%n", sem.meetingDates().size());
        Duration classTime = sem.totalClassTime();
        System.out.printf("Class time: %dh %dm%n", classTime.toHours(), classTime.toMinutesPart());
        Period length = sem.length();
        System.out.printf("Length: %d months, %d days%n", length.getMonths(), length.getDays());
        
        // Empty line for formatting
        System.out.println();

        // Part 4c: build a ZonedDateTime for the FIRST meeting at the class start time
        // in CAMPUS, and print it for America/Boise, America/Los_Angeles,
        // America/New_York and Asia/Tokyo using DateTools.inZone
        ZonedDateTime firstMeeting = ZonedDateTime.of(LocalDateTime.of(sem.nextMeeting(sem.getStart()), sem.getClassStart()), App.CAMPUS);
        ZoneId boise = ZoneId.of("America/Boise");
        System.out.printf("%s %s%n", boise, DateTools.inZone(firstMeeting, boise));
        ZoneId losAngeles = ZoneId.of("America/Los_Angeles");
        System.out.printf("%s %s%n", losAngeles, DateTools.inZone(firstMeeting, losAngeles));
        ZoneId newYork = ZoneId.of("America/New_York");
        System.out.printf("%s %s%n", newYork, DateTools.inZone(firstMeeting, newYork));
        ZoneId tokyo = ZoneId.of("Asia/Tokyo");
        System.out.printf("%s %s%n", tokyo, DateTools.inZone(firstMeeting, tokyo));
        // Empty line for formatting
        System.out.println();

        // Part 4d: loop – read a date (any of the 3 formats) or 'quit'. Print:
        // the next class and its week number, days until the semester ends, and the
        // due date of a lab assigned at that class (5 school days, skipping days off).
        // If there are no more classes say so. Catch IllegalArgumentException for bad input.
        try(Scanner sc = new Scanner(System.in)) {
            String userInput = "";
            while(!userInput.equals("quit")) {
                System.out.print("Enter a date (or 'quit'): ");
                userInput = sc.nextLine().trim();
                if(!userInput.equals("quit")) {
                    try {
                        LocalDate inputDate = DateTools.parseFlexible(userInput);
                        LocalDate nextMeeting = sem.nextMeeting(inputDate);
                        if(nextMeeting != null) {
                            System.out.printf("Next class: %s (week %d)%n", DateTools.formatShort(nextMeeting), sem.weekNumber(nextMeeting));
                            System.out.printf("Days until the semester ends: %d%n", sem.daysLeft(nextMeeting));
                            LocalDate dueDate = DateTools.addSchoolDays(nextMeeting, 5, sem.getDaysOff());
                            System.out.printf("A lab assigned that day is due %s%n", DateTools.formatShort(dueDate));
                        } else {
                            System.out.println("No more classes - enjoy your summer!");
                        }
                    } catch (IllegalArgumentException ex) {
                        System.out.printf("Unrecognized date: %s - try 2027-03-15, 3/15/2027 or Mar 15, 2027%n", userInput);
                    }
                    // Empty line for formatting
                    System.out.println();
                }
            }
        }
    }
}
