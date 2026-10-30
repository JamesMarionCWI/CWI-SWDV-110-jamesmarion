# Reflection

### Why did Java replace `java.util.Date` and `Calendar` with `java.time`? Give two concrete problems the old API had.
The old API was made like any other programmed system at the time - Numbers started from 0 (e.g. January's index was 0), values were highly mutable and therefore not very multithreading friendly, and there wasn't much support for globalized values. These all caused many major issues with program quality.

### When do you use `Period`, and when `Duration`? Use an example from this project.
Period is used for the period of time between Dates, whereas Durations are used for the duration of time between Times. This project used a Period for printing the span of days that the semester takes place during. This project used a Duration for printing the total number of hours spent in class during the semester.

### In the `daylightSavingTime` test, Boise and Phoenix show the *same* clock time in January but are an hour apart in April. Explain why.
Arizona has special Daylight Savings rules - namely, the majority of the state does not observe daylight savings. The only other state in the United States that does not observe daylight savings is Hawaii.

### Your program uses `LocalDate` for class dates but `ZonedDateTime` for the online-student times. Why not use `ZonedDateTime` everywhere?
ZonedDateTime requires the Time component where there is none needed for the operations being performed. LocalDate is sufficient enough for providing clarity of what information is actually being operated on throughout the majority of the program. ZonedDateTime is specifically useful in this program in user-facing cases for printing about data for a specific timezone.
