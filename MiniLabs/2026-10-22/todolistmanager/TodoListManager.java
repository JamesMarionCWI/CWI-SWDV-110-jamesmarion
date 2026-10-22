import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class TodoListManager {
    private static boolean tryRemove(List<String> listToRemoveFrom, int indexToTryRemoving) {
        if(indexToTryRemoving >= 0 && indexToTryRemoving < listToRemoveFrom.size()) {
            listToRemoveFrom.remove(indexToTryRemoving);
            return true;
        } else {
            return false;
        }
    }

    private static boolean tryMove(List<String> listToMoveItems, int indexToMoveFrom, int indexToMoveTo) {
        if(indexToMoveFrom >= 0 && indexToMoveTo >= 0 && indexToMoveFrom < listToMoveItems.size() && indexToMoveTo < listToMoveItems.size()) {
            String temp = listToMoveItems.get(indexToMoveTo);
            listToMoveItems.set(indexToMoveTo, listToMoveItems.get(indexToMoveFrom));
            listToMoveItems.set(indexToMoveFrom, temp);
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
        // 1. Create a List<String> of tasks
        List<String> tasks = new ArrayList<>();
        System.out.println("Welcome to the Todo List manager!");

        try(Scanner scanner = new Scanner(System.in)) {
            String userInput;
            String[] inputs;
            // 2. Loop a menu: add, remove, list, done, quit
            do {
                System.out.println();
                System.out.println("Your options:");
                System.out.println("- add _");
                System.out.println("- remove _");
                System.out.println("- list");
                System.out.println("- done _");
                System.out.println("- sort");
                System.out.println("- edit _ _");
                System.out.println("- move _ _");
                System.out.println("- quit");

                userInput = scanner.nextLine();
                inputs = userInput.split(" ");
                
                System.out.println();
                if(inputs.length > 0) {
                    if(inputs[0].toLowerCase().equals("add")) {
                        if(inputs.length > 1) {
                            tasks.add(userInput.substring(userInput.indexOf(' ')).trim());
                            System.out.println("Task added successfully.");
                        } else {
                            System.out.println("You entered no text to add as a task...");
                        }
                    } else if(inputs[0].toLowerCase().equals("remove")) {
                        if(inputs.length == 1) {
                            System.out.println("No number was entered to be removed...");
                        } else if(inputs.length > 2) {
                            System.out.println("Too many values were entered to be removed, please enter the numbers one at a time...");
                        } else if(inputs.length == 2) {
                            try {
                                // Convert from human number to index and try removing it
                                if(tryRemove(tasks, Integer.parseInt(inputs[1]) - 1)) {
                                    System.out.println("Task removed successfully.");
                                } else {
                                    System.out.println("That number was not within the range of tasks to remove...");
                                }
                            } catch (NumberFormatException ex) {
                                // 5. Print a friendly message for an invalid number
                                System.out.println("That was not a valid number...");
                            }
                        }
                    } else if(inputs[0].toLowerCase().equals("list")) {
                        // 3. list prints tasks numbered from 1
                        System.out.println("Here is your todo list:");
                        for(int i = 0; i < tasks.size(); i++) {
                            // Add 1 to index to number it human-readably from 1
                            System.out.printf("%d - %s%n", i+1, tasks.get(i));
                        }
                    } else if(inputs[0].toLowerCase().equals("done")) {
                        // 4. done 2 removes task #2
                        if(inputs.length == 1) {
                            System.out.println("No number was entered to be completed...");
                        } else if(inputs.length > 2) {
                            System.out.println("Too many values were entered to be completed, please enter the numbers one at a time...");
                        } else if(inputs.length == 2) {
                            try {
                                // Convert from human number to index and try removing it
                                if(tryRemove(tasks, Integer.parseInt(inputs[1]) - 1)) {
                                    System.out.println("Task completed successfully.");
                                } else {
                                    System.out.println("That number was not within the range of tasks to complete...");
                                }
                            } catch (NumberFormatException ex) {
                                // 5. Print a friendly message for an invalid number
                                System.out.println("That was not a valid number...");
                            }
                        }
                    } else if(inputs[0].toLowerCase().equals("sort")) {
                        // Stretch: add a sort command and a move(indexToMoveFrom, indexToMoveTo) function
                        Collections.sort(tasks);
                        System.out.println("Tasks sorted alphabetically.");
                    } else if(inputs[0].toLowerCase().equals("edit")) {
                        if(inputs.length < 3) {
                            System.out.println("No value was entered to be edited OR no text was entered to change the task to...");
                        } else {
                            try {
                                // Convert from human number to index
                                int indexToEdit = Integer.parseInt(inputs[1]) - 1;
                                if(indexToEdit >= 0 && indexToEdit < tasks.size()) {
                                    String numberAndTextToSet = userInput.substring(userInput.indexOf(' ')).trim();
                                    String textToSet = numberAndTextToSet.substring(numberAndTextToSet.indexOf(' ')).trim();
                                    tasks.set(indexToEdit, textToSet);
                                    System.out.println("Task edited successfully.");
                                } else {
                                    System.out.println("That number was not within the range of tasks to edit...");
                                }
                            } catch (NumberFormatException ex) {
                                // 5. Print a friendly message for an invalid number
                                System.out.println("That was not a valid number...");
                            }
                        }
                    } else if(inputs[0].toLowerCase().equals("move")) {
                        // Stretch: add a sort command and a move(indexToMoveFrom, indexToMoveTo) function
                        if(inputs.length < 3) {
                            System.out.println("Not enough numbers were entered to move from and to...");
                        } else {
                            try {
                                // Convert from human number to index
                                int indexToMoveFrom = Integer.parseInt(inputs[1]) - 1;
                                int indexToMoveTo = Integer.parseInt(inputs[2]) - 1;
                                if(tryMove(tasks, indexToMoveFrom, indexToMoveTo)) {
                                    System.out.println("Task moved successfully.");
                                } else {
                                    System.out.println("One of those numbers was not within the range of tasks to edit...");
                                }
                            } catch (NumberFormatException ex) {
                                // 5. Print a friendly message for an invalid number
                                System.out.println("That was not a valid number...");
                            }
                        }
                    }
                } else {
                    System.out.println("Command was empty...");
                }
            } while(!inputs[0].toLowerCase().equals("quit"));
        }
    }
}
