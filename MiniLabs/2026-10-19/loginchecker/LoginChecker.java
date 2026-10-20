import java.util.ArrayList;
import java.util.Scanner;

// 1. Create LoginChecker.java with a main method
public class LoginChecker {
    public static void main(String[] args) {
        // 2. Store a username "admin" and password "Secret123"
        String expectedUsername = "admin";
        String expectedPassword = "Secret123";

        // 3. Prompt for both using a Scanner
        try(Scanner scanner = new Scanner(System.in)) {
            boolean loginConditionMet = false;
            ArrayList<String> loginAttemptNames = new ArrayList<>();
            // Stretch: give the user 3 attempts, then lock them out
            int numberOfAttempts = 0;
            while(!loginConditionMet || numberOfAttempts < 3) {
                numberOfAttempts++;
                
                System.out.print("Enter username: ");
                String inputUsername = scanner.nextLine();
                loginAttemptNames.add(inputUsername);
                System.out.print("Enter password: ");
                String inputPassword = scanner.nextLine();


                // 4. Username match ignores case; password must match exactly
                loginConditionMet = expectedUsername.equalsIgnoreCase(inputUsername) && expectedPassword.equals(inputPassword);
                // 5. Print Welcome or Access Denied
                if(loginConditionMet) {
                    System.out.println("Welcome");
                } else {
                    System.out.println("Access Denied");
                }

                // 6. Then print which of two ames comes first alphabetically using compareTo()
                String lowestAlphabeticalName = loginAttemptNames.get(0);
                for(String loginAttemptName : loginAttemptNames) {
                    if(loginAttemptName.compareTo(lowestAlphabeticalName) < 0) {
                        lowestAlphabeticalName = loginAttemptName;
                    }
                }
                System.out.println("The first name sorted alphabetically in your login attempts: " + lowestAlphabeticalName);
            } 
        }
    }
}
