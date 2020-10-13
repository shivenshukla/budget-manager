package ui;

import model.*;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Scanner;

// Represents the Budget application
// The following code was created while referencing ui/TellerApp
public class BudgetApp {
    private Budget budget;
    private Scanner input;
    private Report expenseReport;
    private Report incomeReport;

    // EFFECTS: runs the budget application
    public BudgetApp() {
        runBudget();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void runBudget() {
        System.out.println("Welcome to Budget Rocket!");

        boolean keepGoing = true;
        String command;

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
    // EFFECTS: processes user command for submenu
    private void processSubCommandExpenseReport(String command) {
        if (command.equals("a")) {
            addExpense();
        } else if (command.equals("d")) {
            deleteEntry(expenseReport);
        } else if (command.equals("m")) {
            modifyEntry(expenseReport);
        } else {
            System.out.println("Uh Oh! You have entered an invalid input...");
        }
    }

    // MODIFIES: this
    // EFFECTS: processes user command for submenu
    private void processSubCommandIncomeReport(String command) {
        if (command.equals("a")) {
            addIncome();
        } else if (command.equals("d")) {
            deleteEntry(incomeReport);
        } else if (command.equals("m")) {
            modifyEntry(incomeReport);
        } else {
            System.out.println("Uh Oh! You have entered an invalid input...");
        }
    }

    // MODIFIES: this
    // EFFECTS: processes user command for modify submenu
    private void processModifyCommand(String command, Entry entry) {
        if (command.equals("d")) {
            System.out.print("Enter the year: ");
            int year = input.nextInt();

            System.out.print("Enter the month: ");
            int month = input.nextInt();

            System.out.print("Enter the day: ");
            int day = input.nextInt();

            entry.setDate(year, month, day);
        } else if (command.equals("a")) {
            System.out.print("Enter the amount: ");
            double amount = input.nextDouble();

            entry.setAmount(amount);
        } else if (command.equals("z")) {
            System.out.print("Enter the description: ");
            String description = input.next();

            entry.setDescription(description);
        } else {
            System.out.println("Uh Oh! You have entered an invalid input...");
        }
    }

    // MODIFIES: this
    // EFFECTS: initializes budget
    private void initialize() {
        budget = new Budget();
        input = new Scanner(System.in);
        expenseReport = budget.getExpenseReport();
        incomeReport = budget.getIncomeReport();
    }

    // EFFECTS: displays budget report
    private void viewBudgetReport() {
        System.out.println("Budget Report:");

        double expenseTotal = expenseReport.sum();
        double incomeTotal = incomeReport.sum();
        double budgetTotal = budget.getDifference();

        System.out.printf("\nTotal expenses: $%.2f\n", expenseTotal);
        System.out.printf("Total income: $%.2f\n", incomeTotal);

        if (budget.isSurplus()) {
            System.out.printf("You have a surplus of $%.2f\n", budgetTotal);
        } else if (budget.isDeficit()) {
            System.out.printf("You have a deficit of $%.2f\n", budgetTotal);
        } else {
            System.out.printf("\nYou have a balance of $%.2f\n", budgetTotal);
        }
    }

    // MODIFIES: this
    // EFFECTS: displays expense report and processes user input
    private void viewExpenseReport() {
        boolean keepGoing = true;
        String command;

        while (keepGoing) {
            System.out.println("Expense Report:");

            if (expenseReport.isEmpty()) {
                System.out.println("There are no expenses to show");
            } else {
                displayEntries(expenseReport);
            }
            displayReportSubmenu();

            command = input.next();
            command = command.toLowerCase();

            if (command.equals("b")) {
                keepGoing = false;
            } else {
                processSubCommandExpenseReport(command);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: displays income report and processes user input
    private void viewIncomeReport() {
        boolean keepGoing = true;
        String command;

        while (keepGoing) {
            System.out.println("Income report:");

            if (incomeReport.isEmpty()) {
                System.out.println("There is no income to show");
            } else {
                displayEntries(incomeReport);
            }
            displayReportSubmenu();

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
    private void displayReportSubmenu() {
        System.out.println("\nSelect from:");
        System.out.println("\tm -> modify an existing entry");
        System.out.println("\ta -> add a new entry");
        System.out.println("\td -> delete an existing entry");
        System.out.println("\tb -> go back");
    }

    // EFFECTS: displays sub menu of of options to user
    private void displayModifySubmenu() {
        System.out.println("Which field would you like to modify?");
        System.out.println("\nSelect from:");
        System.out.println("\td -> date of entry");
        System.out.println("\ta -> amount");
        System.out.println("\tz -> description");
    }

    // MODIFIES: this
    // EFFECTS: adds a new income to the income report
    private void addIncome() {
        System.out.print("Enter the amount: ");
        double amount = input.nextDouble();

        System.out.print("Enter the description: ");
        String description = input.next();

        System.out.print("Enter the year: ");
        int year = input.nextInt();
        System.out.print("Enter the month: ");
        int month = input.nextInt();
        System.out.print("Enter the day: ");
        int day = input.nextInt();

        Calendar date = new GregorianCalendar();
        date.set(year, month, day);

        Income i = new Income(description, amount, date);
        budget.addIncome(i);
    }

    // MODIFIES: this
    // EFFECTS: adds a new expense to the expense report
    private void addExpense() {
        System.out.print("Enter the amount: ");
        double amount = input.nextDouble();

        System.out.print("Enter the description: ");
        String description = input.next();

        System.out.print("Enter the year: ");
        int year = input.nextInt();
        System.out.print("Enter the month: ");
        int month = input.nextInt();
        System.out.print("Enter the day: ");
        int day = input.nextInt();

        Calendar date = new GregorianCalendar();
        date.set(year, month, day);

        Expense e = new Expense(description, amount, date);
        budget.addExpense(e);
    }

    // MODIFIES: this
    // EFFECTS: deletes an entry from the given report
    private void deleteEntry(Report report) {
        if (report.isEmpty()) {
            System.out.println("There are no entries to delete");
        } else {
            System.out.println("Which entry would you like to delete? (-1 to cancel)");

            displayEntries(report);

            int entryNum = input.nextInt();

            if (entryNum >= report.size()) {
                System.out.println("There is no entry with the given number");
            } else if (entryNum < 0) {
                System.out.println("No entry was deleted");
            } else {
                Entry entryToDelete = report.getAllEntries().get(entryNum);
                report.deleteEntry(entryToDelete);

                System.out.println("The entry has been deleted");
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: modifies an entry from the given report
    private void modifyEntry(Report report) {
        if (report.isEmpty()) {
            System.out.println("There are no entries to modify");
        } else {
            System.out.println("Which entry would you like to modify? (enter -1 to cancel)");

            displayEntries(report);

            int entryNum = input.nextInt();

            if (entryNum >= report.size()) {
                System.out.println("There is no entry with that given number");
            } else if (entryNum < 0) {
                System.out.println("No entry was updated");
            } else {
                Entry entryToModify = report.getAllEntries().get(entryNum);

                displayModifySubmenu();

                String command = input.next();
                command.toLowerCase();

                processModifyCommand(command, entryToModify);

                System.out.println("The entry has been updated");
            }
        }
    }

    // EFFECTS: displays all entries in the given report
    private void displayEntries(Report report) {
        int i = 0;
        System.out.println("Entry Number\t\tDate of Entry\t\tAmount\t\t\tDescription");
        for (Entry e : report.getAllEntries()) {
            System.out.printf("%-12d\t\t" + e + "\n", i);
            i++;
        }
    }
}
