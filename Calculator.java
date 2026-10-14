import java.util.ArrayList;
import java.util.Scanner;

public class Calculator {
    public static String runOperation(String operation, double firstNum, double secondNum) throws IllegalArgumentException {
        switch(operation) {
            case "+" -> { return firstNum + " + " + secondNum + " = " + (firstNum + secondNum); }
            case "-" -> { return firstNum + " - " + secondNum + " = " + (firstNum - secondNum); }
            case "*" -> { return firstNum + " * " + secondNum + " = " + (firstNum * secondNum); }
            case "/" -> { return firstNum + " / " + secondNum + " = " + (firstNum / secondNum); }
        }
        return "";
    }

    public static double getDouble(Scanner scanner, String prompt) {
        // Prompt
        System.out.print(prompt);
        // Try to return a double
        return Double.parseDouble(scanner.nextLine());
    }

    public static void main(String[] args) {
        System.out.println("Welcome to the calculator!");
        Scanner scanner = new Scanner(System.in);
        double firstNum = getDouble(scanner, "Please enter your first number: ");
        double secondNum = getDouble(scanner, "Please enter your second number: ");
        
        ArrayList<String> results = new ArrayList<String>();
        results.add(runOperation("+", firstNum, secondNum));
        results.add(runOperation("-", firstNum, secondNum));
        results.add(runOperation("*", firstNum, secondNum));
        results.add(runOperation("/", firstNum, secondNum));
        
        System.out.println("\nResults:");
        for(String result : results) {
            System.out.printf("%s%n", result);
        }
        scanner.close();
    }
}
