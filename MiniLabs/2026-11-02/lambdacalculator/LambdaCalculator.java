import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class LambdaCalculator {
    // 1. Create the MathOperation interface
    @FunctionalInterface 
    interface MathOperation {
        int apply(int a, int b);
    }

    public static void main(String[] args) {
        // 2. Store lambdas in a Map<String, MathOperation> for + - * / %
        // Using a LinkedHashMap to ensure that the order we insert is the order the operations print out when we loop over them
        Map<String, MathOperation> mathOps = new LinkedHashMap<>();
        mathOps.put("+", (a, b) -> a+b);
        mathOps.put("-", (a, b) -> a-b);
        mathOps.put("*", (a, b) -> a*b);
        mathOps.put("/", (a, b) -> a/b);
        mathOps.put("%", (a, b) -> a%b);
        // Stretch: add ^ (power) and max without touching your input-handling code
        mathOps.put("^", (a, b) -> Math.powExact(a, b));
        mathOps.put("max", (a, b) -> Math.max(a, b));

        System.out.println("Welcome to the Lambda Calculator!");

        try(Scanner scanner = new Scanner(System.in)) {
            // Empty line for formatting
            System.out.println();

            System.out.print("Please enter a simple math problem you want solved including the use of one of the following operators: ");
            // Print out the supported operators
            for(String operation : mathOps.keySet()) {
                System.out.print(operation + " ");
            }
            System.out.println();
            System.out.println("It must be in this exact format as an example: 12 * 4");
            System.out.print(": ");
            // 3. Read input like 12 * 4 and split() it on spaces
            String[] userInput = scanner.nextLine().trim().split(" ");

            // Empty line for formatting
            System.out.println();

            if(userInput.length != 3) {
                System.err.println("Error: Your input was not in the valid format of 'num op num'.");
                System.err.println("That's ok though. Just run again and get it right next time :)");
                return;
            }

            int num1;
            int num2;
            try {
                num1 = Integer.parseInt(userInput[0]);
                num2 = Integer.parseInt(userInput[2]);
            } catch (NumberFormatException ex) {
                System.err.println("Error: Your numbers in the format of 'num op num' were not readable as numbers.");
                System.err.println("That's ok though. Just run again and get it right next time :)");
                return;
            }

            // 4. Look up the operator in the map and call apply()
            MathOperation mathOp = mathOps.get(userInput[1]);
            if(mathOp == null) {
                System.err.println("Error: Your operation in the format of 'num op num' was not one of the valid operators supported by this program.");
                System.err.println("That's ok though. Just run again and get it right next time :)");
                return;
            }
            
            // 4. Look up the operator in the map and call apply()
            System.out.printf("Here is the result of %d %s %d: %d%n", num1, userInput[1], num2, mathOp.apply(num1, num2));
            System.out.println("Thank you for using our program! :)");
        }
    }
}
