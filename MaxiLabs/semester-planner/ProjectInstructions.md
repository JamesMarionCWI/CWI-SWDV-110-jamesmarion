# Week 3 Coding Exercise: Semester Planner

**Chapter 18 – Date and Time Usage**
**Estimated time:** 3–5 hours
**Builds on:** in-class practices *Birthday Explorer*, *Library Due Dates*, *Meeting Planner*

---

## The scenario

Instructors build a course calendar every semester: list every class meeting, skip holidays and spring break, number the weeks, and work out due dates that don't land on a weekend or a day off. Online students also need to know what time class starts **where they live**.

You'll write a **Semester Planner** that does all of this with the `java.time` API. It also answers questions like *"It's March 15th; when is my next class, and when would a lab assigned that day be due?"*

## What you'll practice

| `java.time` feature | Where you use it |
|---|---|
| `LocalDate`, `LocalTime`, `LocalDateTime`, `of()` | Building the semester |
| `plusDays`, `isBefore`, `isAfter`, `getDayOfWeek` | Walking through every date in the semester |
| `TemporalAdjusters` (`dayOfWeekInMonth`, `lastInMonth`, `previousOrSame`) | Finding holidays like "third Monday of January" |
| `DayOfWeek`, `Month` enums | Meeting days and holidays |
| `Period` vs `Duration` | Semester length vs total hours of class |
| `ChronoUnit.DAYS / WEEKS.between` | Week numbers, days remaining |
| `DateTimeFormatter.ofPattern` + `DateTimeParseException` | Formatting output and parsing user input in 3 formats |
| `ZonedDateTime`, `ZoneId`, `withZoneSameInstant` | Class start time for online students in other time zones |

## Suggested time plan

| Part | Task | Time |
|---|---|---|
| 0 | Setup | 20–30 min |
| 1 | Holidays & formatting (`DateTools`) | 45 min |
| 2 | Parsing, school days & late penalties (`DateTools`) | 60 min |
| 3 | The `Semester` class | 60–75 min |
| 4 | The planner program (`App`) | 45–60 min |
| 5 | README reflection | 15 min |

---

## Part 0 – Setup

> First time using Maven? Complete **`swdv110_Exercise_Setup.md`** first.

```
semester-planner/
├── pom.xml
└── src/
├── main/java/edu/cwi/swdv110/calendar/
│ ├── App.java
│ ├── DateTools.java
│ └── Semester.java
└── test/java/edu/cwi/swdv110/calendar/
├── DateToolsTest.java
└── SemesterTest.java
```

**File:** `pom.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
<modelVersion>4.0.0</modelVersion>

<groupId>edu.cwi.swdv110</groupId>
<artifactId>semester-planner</artifactId>
<version>1.0-SNAPSHOT</version>

<properties>
<maven.compiler.release>17</maven.compiler.release>
<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
<exec.mainClass>edu.cwi.swdv110.calendar.App</exec.mainClass>
</properties>

<dependencies>
<dependency>
<groupId>org.junit.jupiter</groupId>
<artifactId>junit-jupiter</artifactId>
<version>5.14.4</version>
<scope>test</scope>
</dependency>
</dependencies>

<build>
<plugins>
<plugin>
<groupId>org.apache.maven.plugins</groupId>
<artifactId>maven-compiler-plugin</artifactId>
<version>3.15.0</version>
</plugin>
<plugin>
<groupId>org.apache.maven.plugins</groupId>
<artifactId>maven-surefire-plugin</artifactId>
<version>3.6.0</version>
</plugin>
<plugin>
<groupId>org.codehaus.mojo</groupId>
<artifactId>exec-maven-plugin</artifactId>
<version>3.6.4</version>
</plugin>
</plugins>
</build>
</project>
```

### Starter code

**File:** `src/main/java/edu/cwi/swdv110/calendar/DateTools.java`

```java
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
import java.util.List;
import java.util.Locale;
import java.util.Set;

/** Static date helpers used by Semester and App. */
public final class DateTools {

// TODO Part 1: a formatter for "Mon, Jan 11" (pattern letters: EEE, MMM, d)
// TODO Part 2: a formatter for "Mon, Jan 11 at 6:00 PM MST"
// (hint: literal text goes in single quotes: 'at')
// TODO Part 2: a List<DateTimeFormatter> of the three accepted input formats:
// 2027-03-15 3/15/2027 Mar 15, 2027
// Always pass Locale.US to ofPattern so "Mon"/"PM" don't change on other computers.
// For the INPUT formatters use "uuuu" (not "yyyy") for the year and add
// .withResolverStyle(ResolverStyle.STRICT) – see the Part 2 notes for why.

private DateTools() { }

// ---------------- Part 1 ----------------

/** The nth given weekday of a month, e.g. (2027, JANUARY, 3, MONDAY) -> 2027-01-18 (MLK Day). */
public static LocalDate nthWeekday(int year, Month month, int n, DayOfWeek day) {
// TODO – TemporalAdjusters.dayOfWeekInMonth
throw new UnsupportedOperationException("TODO");
}

/** The last given weekday of a month, e.g. (2027, MAY, MONDAY) -> 2027-05-31 (Memorial Day). */
public static LocalDate lastWeekday(int year, Month month, DayOfWeek day) {
// TODO – TemporalAdjusters.lastInMonth
throw new UnsupportedOperationException("TODO");
}

/** "Mon, Jan 11" */
public static String formatShort(LocalDate date) {
// TODO
throw new UnsupportedOperationException("TODO");
}

/** True for Saturday and Sunday. */
public static boolean isWeekend(LocalDate date) {
// TODO
throw new UnsupportedOperationException("TODO");
}

// ---------------- Part 2 ----------------

/**
* Accepts "2027-03-15", "3/15/2027" or "Mar 15, 2027" (surrounding spaces allowed).
* Anything else -> IllegalArgumentException("Unrecognized date: " + text).
* Hint: try each formatter in turn and catch DateTimeParseException.
*/
public static LocalDate parseFlexible(String text) {
// TODO
throw new UnsupportedOperationException("TODO");
}

/**
* Counts forward `days` school days from `start` (start itself is not counted).
* Weekends and dates in `holidays` are skipped.
* Fri 2027-01-08 + 1 school day -> Mon 2027-01-11
*/
public static LocalDate addSchoolDays(LocalDate start, int days, Set<LocalDate> holidays) {
// TODO
throw new UnsupportedOperationException("TODO");
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
public static int latePenaltyPercent(LocalDateTime due, LocalDateTime submitted) {
// TODO
throw new UnsupportedOperationException("TODO");
}

/** Converts to another zone and formats: "Tue, Jan 12 at 10:00 AM JST" */
public static String inZone(ZonedDateTime when, ZoneId zone) {
// TODO – withZoneSameInstant
throw new UnsupportedOperationException("TODO");
}
}
```

**File:** `src/main/java/edu/cwi/swdv110/calendar/Semester.java`

```java
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
public Semester(String name, LocalDate start, LocalDate end, Set<DayOfWeek> meetingDays,
LocalTime classStart, LocalTime classEnd) {
// TODO validate, then assign the fields
this.name = name;
this.start = start;
this.end = end;
this.meetingDays = meetingDays;
this.classStart = classStart;
this.classEnd = classEnd;
}

public String getName() { return name; }
public LocalDate getStart() { return start; }
public LocalDate getEnd() { return end; }
public LocalTime getClassStart() { return classStart; }
public Set<LocalDate> getDaysOff() { return daysOff; }

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
```

**File:** `src/main/java/edu/cwi/swdv110/calendar/App.java`

```java
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
Semester sem = new Semester("SWDV-110 – Spring 2027",
LocalDate.of(2027, 1, 11), LocalDate.of(2027, 5, 7),
Set.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY),
LocalTime.of(18, 0), LocalTime.of(20, 30));
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
```

### Provided tests

**File:** `src/test/java/edu/cwi/swdv110/calendar/DateToolsTest.java`

```java
package edu.cwi.swdv110.calendar;

import static org.junit.jupiter.api.Assertions.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DateToolsTest {

// ---------- Part 1 ----------
@Test
void holidaysFromRules() {
assertEquals(LocalDate.of(2027, 1, 18),
DateTools.nthWeekday(2027, Month.JANUARY, 3, DayOfWeek.MONDAY));
assertEquals(LocalDate.of(2027, 11, 25),
DateTools.nthWeekday(2027, Month.NOVEMBER, 4, DayOfWeek.THURSDAY));
assertEquals(LocalDate.of(2027, 5, 31),
DateTools.lastWeekday(2027, Month.MAY, DayOfWeek.MONDAY));
}

@Test
void formatShort() {
assertEquals("Mon, Jan 11", DateTools.formatShort(LocalDate.of(2027, 1, 11)));
assertEquals("Wed, Mar 3", DateTools.formatShort(LocalDate.of(2027, 3, 3)));
}

@Test
void weekend() {
assertTrue(DateTools.isWeekend(LocalDate.of(2027, 1, 9)));
assertTrue(DateTools.isWeekend(LocalDate.of(2027, 1, 10)));
assertFalse(DateTools.isWeekend(LocalDate.of(2027, 1, 11)));
}

// ---------- Part 2 ----------
@Test
void parsesThreeFormats() {
LocalDate expected = LocalDate.of(2027, 3, 15);
assertEquals(expected, DateTools.parseFlexible("2027-03-15"));
assertEquals(expected, DateTools.parseFlexible("3/15/2027"));
assertEquals(expected, DateTools.parseFlexible(" Mar 15, 2027 "));
}

@Test
void rejectsBadDates() {
assertThrows(IllegalArgumentException.class, () -> DateTools.parseFlexible("next tuesday"));
assertThrows(IllegalArgumentException.class, () -> DateTools.parseFlexible("2/30/2027"));
assertThrows(IllegalArgumentException.class, () -> DateTools.parseFlexible("15/3/2027"));
}

@Test
void schoolDaysSkipWeekendsAndHolidays() {
Set<LocalDate> none = Set.of();
assertEquals(LocalDate.of(2027, 1, 11),
DateTools.addSchoolDays(LocalDate.of(2027, 1, 8), 1, none));
assertEquals(LocalDate.of(2027, 1, 18),
DateTools.addSchoolDays(LocalDate.of(2027, 1, 11), 5, none));
assertEquals(LocalDate.of(2027, 1, 19),
DateTools.addSchoolDays(LocalDate.of(2027, 1, 11), 5, Set.of(LocalDate.of(2027, 1, 18))));
}

@Test
void latePenalty() {
LocalDateTime due = LocalDateTime.of(2027, 2, 1, 23, 59);
assertEquals(0, DateTools.latePenaltyPercent(due, due));
assertEquals(0, DateTools.latePenaltyPercent(due, due.minusHours(3)));
assertEquals(10, DateTools.latePenaltyPercent(due, due.plusMinutes(1)));
assertEquals(10, DateTools.latePenaltyPercent(due, due.plusHours(24)));
assertEquals(20, DateTools.latePenaltyPercent(due, due.plusHours(24).plusMinutes(1)));
assertEquals(50, DateTools.latePenaltyPercent(due, due.plusDays(7)));
}

@Test
void timeZones() {
ZonedDateTime boise = ZonedDateTime.of(2027, 1, 11, 18, 0, 0, 0, ZoneId.of("America/Boise"));
assertEquals("Mon, Jan 11 at 6:00 PM MST", DateTools.inZone(boise, ZoneId.of("America/Boise")));
assertEquals("Mon, Jan 11 at 8:00 PM EST", DateTools.inZone(boise, ZoneId.of("America/New_York")));
assertEquals("Tue, Jan 12 at 10:00 AM JST", DateTools.inZone(boise, ZoneId.of("Asia/Tokyo")));
}

@Test
void daylightSavingTime() {
// Arizona never changes its clocks. In January it matches Boise;
// in April Boise is on daylight time (MDT) and Phoenix is an hour behind.
ZoneId phoenix = ZoneId.of("America/Phoenix");
ZonedDateTime jan = ZonedDateTime.of(2027, 1, 11, 18, 0, 0, 0, ZoneId.of("America/Boise"));
ZonedDateTime apr = ZonedDateTime.of(2027, 4, 5, 18, 0, 0, 0, ZoneId.of("America/Boise"));
assertEquals("Mon, Jan 11 at 6:00 PM MST", DateTools.inZone(jan, phoenix));
assertEquals("Mon, Apr 5 at 5:00 PM MST", DateTools.inZone(apr, phoenix));
}
}
```

**File:** `src/test/java/edu/cwi/swdv110/calendar/SemesterTest.java`

```java
package edu.cwi.swdv110.calendar;

import static org.junit.jupiter.api.Assertions.*;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SemesterTest {

private Semester sem;

@BeforeEach
void setUp() {
// A short 3-week summer session that meets Tuesday & Thursday, 9:00–11:15
sem = new Semester("Summer", LocalDate.of(2027, 6, 1), LocalDate.of(2027, 6, 18),
Set.of(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY),
LocalTime.of(9, 0), LocalTime.of(11, 15));
}

@Test
void constructorValidates() {
assertThrows(IllegalArgumentException.class, () -> new Semester("Bad",
LocalDate.of(2027, 6, 18), LocalDate.of(2027, 6, 1),
Set.of(DayOfWeek.MONDAY), LocalTime.of(9, 0), LocalTime.of(10, 0)));
assertThrows(IllegalArgumentException.class, () -> new Semester("Bad",
LocalDate.of(2027, 6, 1), LocalDate.of(2027, 6, 18),
Set.of(DayOfWeek.MONDAY), LocalTime.of(10, 0), LocalTime.of(9, 0)));
}

@Test
void meetingDatesWithoutDaysOff() {
assertEquals(List.of(
LocalDate.of(2027, 6, 1), LocalDate.of(2027, 6, 3),
LocalDate.of(2027, 6, 8), LocalDate.of(2027, 6, 10),
LocalDate.of(2027, 6, 15), LocalDate.of(2027, 6, 17)), sem.meetingDates());
}

@Test
void holidaysAndBreaksAreSkipped() {
sem.addHoliday(LocalDate.of(2027, 6, 3));
sem.addBreak(LocalDate.of(2027, 6, 7), LocalDate.of(2027, 6, 11));
assertEquals(6, sem.getDaysOff().size());
assertEquals(List.of(
LocalDate.of(2027, 6, 1),
LocalDate.of(2027, 6, 15), LocalDate.of(2027, 6, 17)), sem.meetingDates());
}

@Test
void weekNumbers() {
assertEquals(1, sem.weekNumber(LocalDate.of(2027, 6, 1))); // Tue
assertEquals(1, sem.weekNumber(LocalDate.of(2027, 6, 6))); // Sun – same week
assertEquals(2, sem.weekNumber(LocalDate.of(2027, 6, 7))); // Mon – new week
assertEquals(3, sem.weekNumber(LocalDate.of(2027, 6, 17)));
}

@Test
void durationsAndPeriods() {
assertEquals(Duration.ofMinutes(135), sem.classLength());
assertEquals(Duration.ofMinutes(135 * 6), sem.totalClassTime());
assertEquals(Period.of(0, 0, 17), sem.length());
}

@Test
void nextMeetingAndDaysLeft() {
assertEquals(LocalDate.of(2027, 6, 1), sem.nextMeeting(LocalDate.of(2027, 5, 20)));
assertEquals(LocalDate.of(2027, 6, 10), sem.nextMeeting(LocalDate.of(2027, 6, 10)));
assertEquals(LocalDate.of(2027, 6, 15), sem.nextMeeting(LocalDate.of(2027, 6, 11)));
assertNull(sem.nextMeeting(LocalDate.of(2027, 6, 18)));

assertEquals(8, sem.daysLeft(LocalDate.of(2027, 6, 10)));
assertEquals(0, sem.daysLeft(LocalDate.of(2027, 7, 4)));
}
}
```

---

## Part 1 – Holidays & formatting

Many U.S. holidays are defined by a rule, not a fixed date: *"the third Monday in January."* `TemporalAdjusters` turns that rule into one line:

```java
LocalDate.of(year, month, 1).with(TemporalAdjusters.dayOfWeekInMonth(n, day))
```

Then create your formatters as `private static final` constants. Build each one once, not on every call.

## Part 2 – Parsing, school days, late penalties

- **`parseFlexible`**: loop over your three formatters. `try` to parse; on `DateTimeParseException`, move on to the next one. If all three fail, throw an `IllegalArgumentException`.
- **Gotcha:** by default, `ofPattern` uses the *SMART* resolver, which quietly "fixes" `2/30/2027` to **Feb 28**. That's a bug for user input. Add `.withResolverStyle(ResolverStyle.STRICT)` so impossible dates are rejected. In STRICT mode, `yyyy` means "year of era" and needs an era, so use `uuuu` for the year: `DateTimeFormatter.ofPattern("M/d/uuuu", Locale.US).withResolverStyle(ResolverStyle.STRICT)`. (`ISO_LOCAL_DATE` is already strict.)
- **`addSchoolDays`**: a `while` loop that steps one day at a time and only counts the days that are not a weekend and not a holiday.
- **`latePenaltyPercent`**: "or part of 24 hours" means you round **up**. With integer minutes, `(minutes + 1439) / 1440` is a ceiling division.
- **`inZone`**: `withZoneSameInstant` keeps the *moment* and changes the wall-clock time. Compare it with `withZoneSameLocal`, which keeps the wall-clock time and changes the moment. Which would be a bug here?

✅ Checkpoint: `mvn -Dtest=DateToolsTest test` passes.

## Part 3 – `Semester`

- Loop over dates with `for (LocalDate d = start; !d.isAfter(end); d = d.plusDays(1))`.
- `Period` is for *calendar* amounts (months and days). `Duration` is for *clock* amounts (hours and minutes). The semester length is a `Period`; class time is a `Duration`.
- Look at the `weekNumbers` test: a Sunday belongs to the same week as the Tuesday before it.

✅ Checkpoint: `mvn test`. Everything passes.

## Part 4 – The planner (`App`)

Complete the four TODOs and run with `mvn -q compile exec:java`. Your output should match this (output after the first few weeks is trimmed):

```text
SWDV-110 – Spring 2027
======================
Week 1 Mon, Jan 11
Wed, Jan 13
Week 2 Wed, Jan 20
Week 3 Mon, Jan 25
Wed, Jan 27
...
Week 10 Mon, Mar 15
Wed, Mar 17
Week 12 Mon, Mar 29
Wed, Mar 31
...
Week 17 Mon, May 3
Wed, May 5

Days off: 7
Meetings: 30
Class time: 75h 0m
Length: 3 months, 26 days

First class, for online students:
America/Boise Mon, Jan 11 at 6:00 PM MST
America/Los_Angeles Mon, Jan 11 at 5:00 PM PST
America/New_York Mon, Jan 11 at 8:00 PM EST
Asia/Tokyo Tue, Jan 12 at 10:00 AM JST

Enter a date (or 'quit'): 3/15/2027
Next class: Mon, Mar 15 (week 10)
Days until the semester ends: 53
A lab assigned that day is due Mon, Mar 29

Enter a date (or 'quit'): Mar 22, 2027
Next class: Mon, Mar 29 (week 12)
Days until the semester ends: 46
A lab assigned that day is due Mon, Apr 5

Enter a date (or 'quit'): 5/8/2027
No more classes – enjoy your summer!

Enter a date (or 'quit'): nope
Unrecognized date: nope – try 2027-03-15, 3/15/2027 or Mar 15, 2027

Enter a date (or 'quit'): quit
```

Notice that Week 2 has only one class (MLK Day), and Week 11 is missing entirely (spring break). A lab assigned Mar 15 is due Mar 29, because the 5 school days skip the break.

---

## Part 5 – README reflection

1. Why did Java replace `java.util.Date` and `Calendar` with `java.time`? Give two concrete problems the old API had.
2. When do you use `Period`, and when `Duration`? Use an example from this project.
3. In the `daylightSavingTime` test, Boise and Phoenix show the *same* clock time in January but are an hour apart in April. Explain why.
4. Your program uses `LocalDate` for class dates but `ZonedDateTime` for the online-student times. Why not use `ZonedDateTime` everywhere?

---

## Stretch goals (pick any)

- **Your real schedule:** change `App` to build the semester for one of *your* current classes, using CWI's published academic calendar.
- **ICS export:** write each meeting as a `VEVENT` in an `.ics` file that Google Calendar or Outlook can import. Use the format `yyyyMMdd'T'HHmmss`.
- **Countdown:** using `LocalDateTime.now()`, print "Next class starts in 2 days, 3 hours, 15 minutes."
- **Late-work calculator:** ask for a due date-time and a submission date-time, then print the penalty.

---

## Submission checklist

- [ ] `mvn test`: all provided tests pass
- [ ] Program output matches Part 4 (calendar, summary, time zones, lookups)
- [ ] Bad input prints a friendly message; the program doesn't crash
- [ ] `README.md` answers the Part 5 questions
- [ ] Pushed to GitHub with a `.gitignore`

## Grading (100 points)

| Area | Points |
|---|---|
| Provided `DateToolsTest` passes | 25 |
| Provided `SemesterTest` passes | 25 |
| `App` output (Part 4) | 20 |
| Code quality: constants for formatters, no magic numbers, clear names | 10 |
| README reflection | 15 |
| Git: meaningful commits | 5 |
| Stretch goals | up to +5 bonus |