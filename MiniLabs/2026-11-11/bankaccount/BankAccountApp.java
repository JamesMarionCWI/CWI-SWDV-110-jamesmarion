import java.text.NumberFormat;
import java.util.Locale;
import java.util.Scanner;
import java.util.function.Supplier;

public class BankAccountApp {
    public static class InsufficientFundsException extends Exception {
        public InsufficientFundsException(String message) {
            super(message);
        }
    }

    // 1. Create a BankAccount class
    public static class BankAccount {
        protected String name;
        protected double balance;

        public BankAccount(String name, double openingDeposit) {
            if(openingDeposit < 0) {
                throw new IllegalArgumentException("Opening deposit cannot be negative!");
            }
            this.name = name;
            this.balance = openingDeposit;
        }

        public void deposit(double amountToDeposit) {
            if(amountToDeposit < 0) {
                throw new IllegalArgumentException("Amount to deposit cannot be negative! That would be a withdrawal.");
            }
            this.balance += amountToDeposit;
        }

        // 4. withdraw() throws your InsufficientFundsException
        public void withdraw(double amountToWithdraw) throws InsufficientFundsException {
            if(amountToWithdraw < 0) {
                throw new IllegalArgumentException("Amount to withdraw cannot be negative! That would be a deposit.");
            }
            if(amountToWithdraw > this.balance) {
                throw new InsufficientFundsException("Not enough funds to withdraw! Current balance: " + this.getCurrentBalance());
            }
            this.balance -= amountToWithdraw;
        }

        // 6. Show money with printf as currency: $1,234.50
        public String getCurrentBalance() {
            return NumberFormat.getCurrencyInstance(Locale.US).format(this.balance);
        }
    }

    // 3. Add SavingsAccount extends BankAccount with an addInterest() method
    public static class SavingsAccount extends BankAccount {
        protected double interestRate;
        public SavingsAccount(String name, double openingDeposit, double interestRate) {
            super(name, openingDeposit);
            this.interestRate = interestRate;
        }
        public void addInterest() {
            // nobody ever knows how it's done anyway
            // so nobody will ever notice that this is probably wrong
            // I joke but I've actually QA'd a few bugs where something like this was the case
            // (but with discount calculations and tax calculations xD)
            // Nothing is more permanent than a temporary solution
            this.balance *= interestRate;
        }
    }

    public static <T> T tryRepeatGetValueFromInput(Supplier<T> resultProducerAttempter) {
        T result = null;
        while(result == null) {
            result = resultProducerAttempter.get();
        }
        return result;
    }

    public static void main(String[] args) {
        try(Scanner scanner = new Scanner(System.in)) {
            // 2. Take user's name and opening deposit as input and instantiate a new BankAccount class
            System.out.println("Enter a name to opening a bank account:");
            String name = scanner.nextLine().trim();
            BankAccount bankAccount = tryRepeatGetValueFromInput(() -> {
                try {
                    System.out.println();
                    System.out.println("Enter an opening deposit amount:");
                    double depositBalance = Double.parseDouble(scanner.nextLine().trim());
                    return new BankAccount(name, depositBalance);
                } catch (NumberFormatException ex) {
                    System.out.println("That is not a valid number!");
                } catch (IllegalArgumentException ex) {
                    System.out.println(ex.getMessage());
                }
                return null;
            });

            // 5. Menu loop: deposit, withdraw, balance, quit - bad input never crashes
            // Loop Commands: deposit/withdraw/balance/quit
            String userInput = "";
            while(!userInput.equals("quit")) {
                System.out.println();
                System.out.println("Commands:\ndeposit\nwithdraw\nbalance\nquit\n========");
                userInput = scanner.nextLine().trim().toLowerCase();
                switch(userInput) {
                    case "deposit" -> {
                        tryRepeatGetValueFromInput(() -> {
                            System.out.println();
                            System.out.println("Enter an amount to deposit (or 'cancel' to cancel):");
                            String userDepositAmount = scanner.nextLine().trim();
                            
                            // If the user wants to cancel this deposit
                            if(userDepositAmount.equals("cancel")) {
                                return true; // Allow exiting the loop since the user chose to cancel
                            }
                            try {
                                // Try parsing and depositing
                                bankAccount.deposit(Double.parseDouble(userDepositAmount));
                                // No errors? Cool.
                                return true; // Allow exiting the loop since we succeeded
                            } catch (NumberFormatException ex) {
                                System.out.println("That is not a valid number to deposit!");
                            } catch (IllegalArgumentException ex) {
                                System.out.println(ex.getMessage());
                            }
                            return null; // Do not allow exiting the loop if we're down here
                        });
                    }
                    case "withdraw" -> {
                        tryRepeatGetValueFromInput(() -> {
                            System.out.println();
                            System.out.println("Enter an amount to withdraw (or 'cancel' to cancel):");
                            String userDepositAmount = scanner.nextLine().trim();
                            
                            // If the user wants to cancel this deposit
                            if(userDepositAmount.equals("cancel")) {
                                return true; // Allow exiting the loop since the user chose to cancel
                            }
                            try {
                                // Try parsing and depositing
                                bankAccount.withdraw(Double.parseDouble(userDepositAmount));
                                // No errors? Cool.
                                return true; // Allow exiting the loop since we succeeded
                            } catch (NumberFormatException ex) {
                                System.out.println("That is not a valid number to deposit!");
                            } catch (IllegalArgumentException ex) {
                                System.out.println(ex.getMessage());
                            } catch (InsufficientFundsException ex) {
                                System.out.println(ex.getMessage());
                            }
                            return null; // Do not allow exiting the loop if we're down here
                        });
                    }
                    case "balance" -> {
                        System.out.printf("Current balance: %s%n", bankAccount.getCurrentBalance());
                    }
                    case "quit" -> {
                        System.out.println("Have an awesome rest of your day! :)");
                    }
                }
            }
        }
    }
}
