import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Scanner;

public class TicketQueue {
    public static void main(String[] args) {
        // 1. Store tickets in a Deque<String> used as a queue
        Deque<String> openTickets = new ArrayDeque<>();
        // 3. Keep a second Deque as a stack of resolved tickets
        Deque<String> resolvedTickets = new ArrayDeque<>();
        int autoIncrementNum = 1;
        System.out.println("Welcome to the Help Desk Ticket Queue!");

        try(Scanner scanner = new Scanner(System.in)) {
            String userInput;
            String[] inputs;
            // 2. Menu: new ticket, next (serve), peek, count
            do {
                System.out.println();
                System.out.println("Your options:");
                System.out.println("- new _");
                System.out.println("- next");
                System.out.println("- peek");
                System.out.println("- count");
                System.out.println("- undo");
                System.out.println("- quit");

                userInput = scanner.nextLine();
                inputs = userInput.split(" ");

                System.out.println();
                if(inputs.length > 0) {
                    if(inputs[0].toLowerCase().equals("new")) {
                        if(inputs.length > 1) {
                            // Stretch: give each ticket an auto-incrementing number
                            openTickets.offer(String.format("%d. %s", autoIncrementNum, userInput.substring(userInput.indexOf(' ')).trim()));
                            autoIncrementNum++;
                            System.out.println("Ticket added successfully.");
                        } else {
                            System.out.println("You entered no text to add as a ticket...");
                        }
                    } else if(inputs[0].toLowerCase().equals("next")) {
                        String nextTicket = openTickets.poll();
                        if(nextTicket != null) {
                            System.out.println("Here is your next ticket:");
                            System.out.println(nextTicket);
                            System.out.println("Marking as resolved since you're awesome like that...");
                            resolvedTickets.push(nextTicket);
                        } else {
                            // 5. Print a message when there is nothing to serve
                            System.out.println("There are no more unresolved tickets in the queue!");
                        }
                    } else if(inputs[0].toLowerCase().equals("peek")) {
                        String nextTicket = openTickets.peek();
                        if(nextTicket != null) {
                            System.out.println("Here are the details on the next ticket that is currently unassigned:");
                            System.out.println(nextTicket);
                        } else {
                            // 5. Print a message when there is nothing to serve
                            System.out.println("There are no more tickets in the queue!");
                        }
                    } else if(inputs[0].toLowerCase().equals("count")) {
                        System.out.println("Here are the number of open tickets in the queue:");
                        System.out.println(openTickets.size());
                        System.out.println("Here are the number of resolved tickets in the queue:");
                        System.out.println(resolvedTickets.size());
                    } else if(inputs[0].toLowerCase().equals("undo")) {
                        // 4. Add an undo command that reopens the last resolved ticket
                        String lastTicket = resolvedTickets.pop();
                        if(lastTicket != null) {
                            System.out.println("Here was your last ticket:");
                            System.out.println(lastTicket);
                            System.out.println("Reopening the ticket and sending it to the first in line so the customer gets priority...");
                            openTickets.push(lastTicket);
                        } else {
                            // 5. Print a message when there is nothing to serve
                            System.out.println("There are no tickets currently marked as resolved to undo!");
                        }
                    }
                } else {
                    System.out.println("Command was empty...");
                }
            } while(!inputs[0].toLowerCase().equals("quit"));
        }
    }
}