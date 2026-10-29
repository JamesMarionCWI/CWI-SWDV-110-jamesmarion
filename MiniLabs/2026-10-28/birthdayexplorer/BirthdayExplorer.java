import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class BirthdayExplorer {
    public static void main(String[] args) {
        // 1. Ask for a birthday as yyyy-mm-dd and parse() it
        System.out.print("Enter a birthday (yyyy-mm-dd): ");
        try(Scanner scanner = new Scanner(System.in)) {
            String userBday = scanner.nextLine();
            try {
                // 1. Ask for a birthday as yyyy-mm-dd and parse() it
                LocalDate bday = LocalDate.parse(userBday.trim());
                // 2. Print the day of the week you were born
                System.out.printf("This birthday was on a %s%n", bday.getDayOfWeek());
                // 3. Print which day of the year it was
                System.out.printf("This birthday was on day %d of the year%n", bday.getDayOfYear());
                // 4. Print whether that year was a leap year
                if(Year.isLeap(bday.getYear())) {
                    System.out.println("This birthday's year was a leap year!");
                } else {
                    System.out.println("This birthday's year was not a leap year.");
                }
                // 5. Build this year's birthday with withYear() and print whether it has already passed
                LocalDate currentYearBday = bday.withYear(2026);
                LocalDate today = LocalDate.now();
                System.out.printf("This year, the birthday is celebrated on %s%n", currentYearBday);
                if(currentYearBday.isAfter(today)) {
                    System.out.println("The birthday is still upcoming this year.");
                } else if (currentYearBday.isBefore(today)) {
                    System.out.println("The birthday has already passed this year.");
                } else {
                    System.out.println("The birthday is today! Happy Birthday!");
                }

                // Stretch: print the day of the week your birthday falls on for the next 5 years
                StringBuilder nextSeveralYearsDaysOfWeek = new StringBuilder();
                for(int i = 1; i <= 5; i++) {
                    nextSeveralYearsDaysOfWeek.append(String.format("- %d: %s%n", 2026+i, bday.withYear(2026+i).getDayOfWeek()));
                }
                System.out.printf("Over the next 5 years, the birthday will be falling on:%n%s", nextSeveralYearsDaysOfWeek.toString());
            } catch (DateTimeParseException dtpex) {
                System.err.println("That was an invalid format for the birthday... ");
            }
        }
        System.out.println("Bye!");
    }
}
