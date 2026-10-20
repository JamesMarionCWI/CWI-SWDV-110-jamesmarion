import java.util.Scanner;

public class NameEmailParser {
    public static void main(String[] args) {
        try(Scanner scanner = new Scanner(System.in)) {
            // 1. Prompt for a full name like Grace Brewster Hopper
            System.out.print("Enter your Full Name: ");
            String fullName = scanner.nextLine();
            // Stretch: handle extra spaces before, after and between names
            String[] names = fullName.split(" ");

            // 2. Print the first name and the last name
            if(names.length > 0) {
                if(names.length > 1) {
                    System.out.println("First name: " + names[0]);
                    int indexOfLastName = names.length - 1;
                    // If we have any potential middle names
                    if(names.length > 2) {
                        for(int i = 1; i < indexOfLastName; i++) {
                            System.out.printf("Middle name %d: %s%n", i, names[i]);
                        }
                    }
                    System.out.println("Last name: " + names[indexOfLastName]);
                } else {
                    System.out.println("Name: " + names[0]);
                }
            }

            // 3. Print the initials, e.g. G.B.H. (middle name optional)
            String initials = "";
            for(String name : names) {
                initials += name.toUpperCase().charAt(0) + ".";
            }
            System.out.println(initials);

            // 4. Prompt for an email; print the user and domain
            System.out.print("Enter your Email Address: ");
            String fullEmail = scanner.nextLine();

            // 5. Print invalid if there is no @ or no . after it
            int atSignIndex = fullEmail.indexOf("@");
            if(atSignIndex < 0) {
                System.out.println("Invalid email: no @ sign! Exiting...");
                return;
            }
            int dotIndex = fullEmail.indexOf(".");
            if(dotIndex < 0) {
                System.out.println("Invalid email: no dot! Exiting...");
                return;
            }

            // 4. Prompt for an email; print the user and domain
            String emailUsernamePart = fullEmail.substring(0, atSignIndex);
            String emailDomainPart = fullEmail.substring(atSignIndex + 1, dotIndex);
            String emailTLDPart = fullEmail.substring(dotIndex + 1);
            System.out.println("Email Username: " + emailUsernamePart);
            System.out.println("Email Domain: " + emailDomainPart);
            System.out.println("Email TLD: " + emailTLDPart);
        }
    }
}
