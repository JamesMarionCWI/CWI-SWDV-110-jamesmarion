import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import java.util.Date;

public class Calculator {
    public static String runOperation(String operation, double firstNum, double secondNum) throws IllegalArgumentException {
        switch(operation) {
            case "+" -> { return firstNum + " + " + secondNum + " = " + (firstNum + secondNum); }
            case "-" -> { return firstNum + " - " + secondNum + " = " + (firstNum - secondNum); }
            case "*" -> { return firstNum + " * " + secondNum + " = " + (firstNum * secondNum); }
            case "/" -> { return firstNum + " / " + secondNum + " = " + (firstNum / secondNum); }
        }
        // Failfast instead of handling the error because "operation" is (currently) set by the programmer and not the user
        // In the future, this parameter should be refactored to something like an Enum instead of a String
        throw new IllegalArgumentException("Error: Unrecognized Operation: " + operation);
    }

    public static double getValidDouble(Scanner scanner, String prompt) {
        // Repeat while invalid
        while(true) {
            // Prompt in each loop
            System.out.print(prompt);
            try {
                // Try to return a valid double
                return Double.parseDouble(scanner.nextLine());
                // Invalid doubles get caught instead of returning
            } catch (NumberFormatException e) {
                // Alert the user that they entered an invalid number
                System.err.println("Invalid number...");
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("Welcome to the calculator!");
        try (Scanner scanner = new Scanner(System.in)) {
            double firstNum = getValidDouble(scanner, "Please enter your first number: ");
            double secondNum = getValidDouble(scanner, "Please enter your second number: ");
            
            ArrayList<String> results = new ArrayList<String>();
            results.add(runOperation("+", firstNum, secondNum));
            results.add(runOperation("-", firstNum, secondNum));
            results.add(runOperation("*", firstNum, secondNum));
            results.add(runOperation("/", firstNum, secondNum));
            
            System.out.println("\nResults:");
            for(String result : results) {
                System.out.printf("%s%n", result);
            }
        }
    }
}
