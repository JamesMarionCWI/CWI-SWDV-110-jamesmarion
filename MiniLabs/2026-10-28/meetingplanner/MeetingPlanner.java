import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class MeetingPlanner {
    private static void flagOutsideBusinessHours(ZonedDateTime dt) {
        // 3. Flag any city where it falls outside 9 AM - 5 PM
        if(dt.getHour() < 9 || dt.getHour() >= 17) {
            System.out.print('*');
        }
    }
    public static void main(String[] args) {
        System.out.println("Welcome to the meeting planner application!");
        try(Scanner scanner = new Scanner(System.in)) {
            DateTimeFormatter format = DateTimeFormatter.ofPattern("M/d/yyyy h:mm a");
            // 5. Re-prompt on invalid input
            LocalDateTime meetingDateTime = null;
            while(meetingDateTime == null) {
                // 1. Ask for a meeting date and time in Boise (M/d/yyyy h:mm a)
                System.out.println("Please enter a meeting date and time (in the Boise timezone) (M/d/yyyy h:mm a) (e.g. 7/17/2007 7:27 PM)");
                String userInput = scanner.nextLine().trim();
                try {
                    meetingDateTime = LocalDateTime.parse(userInput, format);
                    if(meetingDateTime.isBefore(LocalDateTime.now())) {
                        meetingDateTime = null;
                        System.err.println("Error: Date cannot be in the past!");
                    }
                } catch (DateTimeParseException ex) {
                    System.err.println("Error: Incorrect format for your input!");
                }
            }

            ZoneId boise = ZoneId.of("America/Boise");
            ZonedDateTime boiseMeetingTime = ZonedDateTime.of(meetingDateTime, boise);
            ZonedDateTime nyMeetingTime = boiseMeetingTime.withZoneSameInstant(ZoneId.of("America/New_York"));
            ZonedDateTime londonMeetingTime = boiseMeetingTime.withZoneSameInstant(ZoneId.of("Europe/London"));
            ZonedDateTime tokyoMeetingTime = boiseMeetingTime.withZoneSameInstant(ZoneId.of("Asia/Tokyo"));

            // 2. Show the meeting time in New York, London and Tokyo
            // 3. Flag any city where it falls outside 9 AM - 5 PM
            System.out.println("Times in each city (* = outside of the 9am-5pm window):");
            flagOutsideBusinessHours(boiseMeetingTime);
            System.out.printf("Boise: %s%n", boiseMeetingTime);

            flagOutsideBusinessHours(nyMeetingTime);
            System.out.printf("New York: %s%n", nyMeetingTime);

            flagOutsideBusinessHours(londonMeetingTime);
            System.out.printf("London: %s%n", londonMeetingTime);

            flagOutsideBusinessHours(tokyoMeetingTime);
            System.out.printf("Tokyo: %s%n", tokyoMeetingTime);
            
            // 4. Print a countdown: days, hours and minutes from now
            Duration timeUntilMeeting = Duration.between(LocalDateTime.now(), meetingDateTime);
            System.out.printf("Days from now: %d%nHours from now: %d%nMinutes from now: %d%n", timeUntilMeeting.toDays(), timeUntilMeeting.toHoursPart(), timeUntilMeeting.toMinutesPart());

            // Stretch: suggest the next weekday hour that works for all four cities
            // lolno. That's cute.
        }
        System.out.println("Bye!");
    }
}
