package ui;

import model.Budget;
import model.Entry;
import model.Income;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Scanner;

// Represents the Budget application
// The following code was created while referencing ui/TellerApp
public class BudgetApp {
    private Budget budget;
    private Scanner input;

    // EFFECTS: runs the budget application
    public BudgetApp() {
        runBudget();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void runBudget() {
        System.out.println("Welcome to Budget Rocket!");

        boolean keepGoing = true;
        String command = null;

        initialize();

        while (keepGoing) {
            displayMainMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }

        System.out.println("\nGoodbye!");
    }

    // MODIFIES: this
    // EFFECTS: processes user command for main menu
    private void processCommand(String command) {
        if (command.equals("b")) {
            viewBudgetReport();
        } else if (command.equals("e")) {
            viewExpenseReport();
        } else if (command.equals("i")) {
            viewIncomeReport();
        } else {
            System.out.println("Uh Oh! You have entered an invalid input...");
        }
    }

    // MODIFIES: this
    // EFFECTS: processes user command for sub menu
    private void processSubCommandExpenseReport(String command) {
        if (command.equals("a")) {
            addExpense();
        } else if (command.equals("d")) {
            deleteEntryIncomeReport();
        } else if (command.equals("m")) {
            modifyEntryIncomeReport();
        } else {
            System.out.println("Uh Oh! You have entered an invalid input...");
        }
    }

    // MODIFIES: this
    // EFFECTS: processes user command for sub menu
    private void processSubCommandIncomeReport(String command) {
        if (command.equals("a")) {
            addIncome();
        } else if (command.equals("d")) {
            deleteEntryIncomeReport();
        } else if (command.equals("m")) {
            modifyEntryIncomeReport();
        } else {
            System.out.println("Uh Oh! You have entered an invalid input...");
        }
    }

    // MODIFIES: this
    // EFFECTS: initializes budget
    private void initialize() {
        budget = new Budget();
        input = new Scanner(System.in);
    }

    private void viewBudgetReport() {
        System.out.println("Budget Report:");

        double expenseTotal = budget.getExpenseReport().sum();
        double incomeTotal = budget.getIncomeReport().sum();

        System.out.printf("\nTotal expenses: $%.2f\n", expenseTotal);
        System.out.printf("Total income: $%.2f\n", incomeTotal);

        if (budget.isSurplus()) {
            System.out.printf("You have a surplus of $%.2f\n", budget.getDifference());
        } else if (budget.isDeficit()) {
            System.out.printf("You have a deficit of $%.2f\n", budget.getDifference());
        } else {
            System.out.printf("\nYou have a balance of $%.2f\n", budget.getDifference());
        }
    }

    private void viewExpenseReport() {
        System.out.println("Expense report:");

        if (budget.getExpenseReport().isEmpty()) {
            System.out.println("There are no expenses to show");
        } else {
            for (Entry e : budget.getExpenseReport().getAllEntries()) {
                System.out.println(e.getDescription());
                System.out.printf("%.2f", e.getAmount());
                System.out.println(e.getDate());
            }
        }

        boolean keepGoing = true;
        String command = null;

        initialize();

        while (keepGoing) {
            displaySubMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("b")) {
                keepGoing = false;
            } else {
                processSubCommandExpenseReport(command);
            }
        }
    }

    private void viewIncomeReport() {
        boolean keepGoing = true;
        String command = null;

        initialize();

        while (keepGoing) {
            System.out.println("Income report:");

            if (budget.getIncomeReport().isEmpty()) {
                System.out.println("There is no income to show");
            } else {
                int i = 0;
                System.out.println("Entry Number \t\t Date of Entry \t\t Amount \t\t Description");
                for (Entry e : budget.getIncomeReport().getAllEntries()) {
                    System.out.printf("%d\t\t", i);
                    System.out.println(e);
                }
            }
            displaySubMenu();

            command = input.next();
            command = command.toLowerCase();

            if (command.equals("b")) {
                keepGoing = false;
            } else {
                processSubCommandIncomeReport(command);
            }
        }
    }

    // EFFECTS: displays main menu of options to user
    private void displayMainMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\tb -> view budget report");
        System.out.println("\te -> view expense report");
        System.out.println("\ti -> view income report");
        System.out.println("\tq -> quit");
    }

    // EFFECTS: displays sub menu of options to user
    private void displaySubMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\tm -> modify an existing entry");
        System.out.println("\ta -> add a new entry");
        System.out.println("\td -> delete an existing entry");
        System.out.println("\tb -> go back");
    }

    // MODIFIES: this
    // EFFECTS: adds a new income to the income report
    private void addIncome() {
        System.out.println("Enter the amount:");
        double amount = input.nextDouble();
        System.out.println("Enter the description:");
        String description = input.next();
        System.out.println("Enter the year:");
        int year = input.nextInt();
        System.out.println("Enter the month:");
        int month = input.nextInt();
        System.out.println("Enter the day:");
        int day = input.nextInt();

        Calendar date = new GregorianCalendar();
        date.set(year, month, day);
        Income i = new Income(description, amount, date);
        budget.addIncome(i);
    }

    // MODIFIES: this
    // EFFECTS: adds a new expense to the expense report
    private void addExpense() {

    }

    // MODIFIES: this
    // EFFECTS: deletes an entry from the report
    private void deleteEntryIncomeReport() {
        if (budget.getIncomeReport().isEmpty()) {
            System.out.println("There are no entries to delete");
        } else {
            System.out.println("Which entry would you like to delete?");
            int i = 0;
            System.out.println("Entry Number \t\t Date of Entry \t\t Amount \t\t Description");
            for (Entry e : budget.getIncomeReport().getAllEntries()) {
                System.out.printf("%d\t\t", i);
                System.out.println(e);
            }
            int entryNum = input.nextInt();
            Entry entryToDelete = budget.getIncomeReport().getAllEntries().get(entryNum);
            budget.getIncomeReport().deleteEntry(entryToDelete);
        }
    }

    // MODIFIES: this
    // EFFECTS: modifies an entry from the report
    private void modifyEntryIncomeReport() {
        if (budget.getIncomeReport().isEmpty()) {
            System.out.println("There are no entries to modify");
        } else {
            System.out.println("Which entry would you like to modify?");
            int i = 0;
            System.out.println("Entry Number \t\t Date of Entry \t\t Amount \t\t Description");
            for (Entry e : budget.getIncomeReport().getAllEntries()) {
                System.out.printf("%d\t\t", i);
                System.out.println(e);
            }
            int entryNum = input.nextInt();
            Entry entryToModify = budget.getIncomeReport().getAllEntries().get(entryNum);

            System.out.println("Which field would you like to modify?");
            System.out.println("\nSelect from:");
            System.out.println("\td -> date of entry");
            System.out.println("\ta -> amount");
            System.out.println("\tz -> description");

            String command = input.next();
            command.toLowerCase();

            if (command.equals("d")) {
                System.out.println("Enter the year:");
                int year = input.nextInt();
                System.out.println("Enter the month:");
                int month = input.nextInt();
                System.out.println("Enter the day:");
                int day = input.nextInt();
                Calendar date = new GregorianCalendar();
                date.set(year, month, day);
                entryToModify.setDate(date);
            } else if (command.equals("a")) {
                System.out.println("Enter the amount");
                double amount = input.nextDouble();
                entryToModify.setAmount(amount);
            } else if (command.equals("z")) {
                System.out.println("Enter the description");
                String description = input.next();
                entryToModify.setDescription(description);
            }

            System.out.println("The entry has been updated");
        }
    }
}
