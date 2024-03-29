package ui;

import exception.EmptyStringException;
import exception.NegativeInputException;
import model.*;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Scanner;

import static java.lang.Math.abs;

// Data persistence methods were modeled from the ui package of https://github.com/stleary/JSON-java

// Represents the Budget application
public class BudgetManagerUI {
    private static final String BUDGET_DATA = "./data/budget.json";

    private Budget budget;
    private Scanner input;          // used for general input
    private Scanner inputSentence;  // used for input of description
    private Report expenseReport;
    private Report incomeReport;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECTS: runs the budget application
    public BudgetManagerUI() {
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
    // EFFECTS: initializes budget
    private void initialize() {
        budget = new Budget();
        input = new Scanner(System.in);
        inputSentence = new Scanner(System.in).useDelimiter("\n");
        expenseReport = budget.getExpenseReport();
        incomeReport = budget.getIncomeReport();
        jsonWriter = new JsonWriter(BUDGET_DATA);
        jsonReader = new JsonReader(BUDGET_DATA);
    }

    /** methods for processing user command **/

    // MODIFIES: this
    // EFFECTS: processes user command for main menu
    private void processCommand(String command) {
        if (command.equals("b")) {
            viewBudgetReport();
        } else if (command.equals("e")) {
            viewExpenseReport();
        } else if (command.equals("i")) {
            viewIncomeReport();
        } else if (command.equals("s")) {
            saveBudget();
        } else if (command.equals("l")) {
            loadBudget();
        } else {
            System.out.println("Uh Oh! You have entered an invalid input...");
        }
    }

    // MODIFIES: this
    // EFFECTS: processes user command for the expenseReport submenu
    private void processSubCommandExpenseReport(String command) {
        if (command.equals("a")) {
            addExpense();
        } else if (command.equals("d")) {
            deleteEntry(expenseReport);
        } else if (command.equals("m")) {
            modifyEntry(expenseReport);
        } else {
            System.out.println("Uh Oh! You have entered an invalid input...\n");
        }
    }

    // MODIFIES: this
    // EFFECTS: processes user command for the incomeReport submenu
    private void processSubCommandIncomeReport(String command) {
        if (command.equals("a")) {
            addIncome();
        } else if (command.equals("d")) {
            deleteEntry(incomeReport);
        } else if (command.equals("m")) {
            modifyEntry(incomeReport);
        } else {
            System.out.println("Uh Oh! You have entered an invalid input...\n");
        }
    }

    /** methods for viewing reports in budget **/

    // EFFECTS: displays the budget report
    private void viewBudgetReport() {
        System.out.println("Budget Report:");

        double expenseTotal = expenseReport.sum();
        double incomeTotal = incomeReport.sum();
        double budgetTotal = budget.getDifference();

        System.out.printf("Total expenses: $%.2f\n", expenseTotal);
        System.out.printf("Total income: $%.2f\n", incomeTotal);

        if (budget.isSurplus()) {
            System.out.printf("\nYou have a surplus of $%.2f\n", budgetTotal);
        } else if (budget.isDeficit()) {
            System.out.printf("\nYou have a deficit of $%.2f\n", abs(budgetTotal));
        } else {
            System.out.printf("\nYou have a balance of $%.2f\n", budgetTotal);
        }
    }

    // MODIFIES: this
    // EFFECTS: displays the expenseReport and processes user input
    private void viewExpenseReport() {
        boolean keepGoing = true;
        String command;

        while (keepGoing) {
            System.out.println("Expense Report:");

            if (expenseReport.isEmpty()) {
                System.out.println("There are no expenses to show.");
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
    // EFFECTS: displays the incomeReport and processes user input
    private void viewIncomeReport() {
        boolean keepGoing = true;
        String command;

        while (keepGoing) {
            System.out.println("Income report:");

            if (incomeReport.isEmpty()) {
                System.out.println("There is no income to show.");
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

    // EFFECTS: displays all entries in the given report
    private void displayEntries(Report report) {
        int i = 0;

        String dateHeader = String.format("%-30s", "Date of Entry");
        String amountHeader = String.format("%-31s", "Amount");

        System.out.println("Entry Number\t\t" + dateHeader + amountHeader + "Description");
        for (Entry e : report.getAllEntries()) {
            System.out.printf("%-12d\t\t" + e + "\n", i);
            i++;
        }
    }

    /** methods for displaying menus to user **/

    // EFFECTS: displays the main menu of options to the user
    private void displayMainMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\tb -> view budget report");
        System.out.println("\te -> view expense report");
        System.out.println("\ti -> view income report");
        System.out.println("\ts -> save budget to file");
        System.out.println("\tl -> load budget from file");
        System.out.println("\tq -> quit");
    }

    // EFFECTS: displays the report submenu of options to the user
    private void displayReportSubmenu() {
        System.out.println("\nSelect from:");
        System.out.println("\ta -> add a new entry");
        System.out.println("\tm -> modify an existing entry");
        System.out.println("\td -> delete an existing entry");
        System.out.println("\tb -> go back");
    }

    // EFFECTS: displays the modify submenu of of options to the user
    private void displayModifySubmenu() {
        System.out.println("Which field would you like to modify?");
        System.out.println("\nSelect from:");
        System.out.println("\te -> date of entry");
        System.out.println("\ta -> amount");
        System.out.println("\td -> description");
    }

    /** methods for doing operations **/

    // MODIFIES: this
    // EFFECTS: adds a new income to the incomeReport, if amount >= 0
    private void addIncome() {
        try {
            System.out.print("Enter the amount: ");
            double amount = input.nextDouble();

            System.out.print("Enter the description: ");
            String description = inputSentence.next();
            Calendar date = makeDate();

            Income i = new Income(description, amount, date);
            budget.addIncome(i);
        } catch (NegativeInputException e) {
            System.out.println("\nThe amount cannot be negative. No new entry was added.");
        } catch (EmptyStringException e) {
            System.out.println("\nThe description cannot be empty. No new entry was added.");
        } finally {
            System.out.println();
        }
    }

    // MODIFIES: this
    // EFFECTS: adds a new expense to the expenseReport, if amount >= 0
    private void addExpense() {
        try {
            System.out.print("Enter the amount: ");
            double amount = input.nextDouble();

            System.out.print("Enter the description: ");
            String description = inputSentence.next();
            Calendar date = makeDate();

            Expense e = new Expense(description, amount, date);
            budget.addExpense(e);
        } catch (NegativeInputException e) {
            System.out.println("\nThe amount cannot be negative. No new entry was added.");
        } catch (EmptyStringException e) {
            System.out.println("\nThe description cannot be empty. No new entry was added.");
        } finally {
            System.out.println();
        }
    }

    // MODIFIES: this
    // EFFECTS: prompts user for entry to delete; deletes it, if entryNum corresponds to an entry in report
    private void deleteEntry(Report report) {
        if (report.isEmpty()) {
            System.out.println("There are no entries to delete.");
        } else {
            System.out.println("Enter the number of the entry to delete; -1 to cancel.");

            displayEntries(report);

            int entryNum = input.nextInt();

            if (entryNum >= report.size()) {
                System.out.println("There is no entry with the given number.");
            } else if (entryNum < 0) {
                System.out.println("No entry was deleted.");
            } else {
                Entry entryToDelete = report.getAllEntries().get(entryNum);
                report.deleteEntry(entryToDelete);

                System.out.println("The entry has been deleted.");
            }
        }
        System.out.println();
    }

    // MODIFIES: this
    // EFFECTS: prompts user for entry to modify; modifies it, if entryNum corresponds to an entry in report
    private void modifyEntry(Report report) {
        if (report.isEmpty()) {
            System.out.println("There are no entries to modify.");
        } else {
            System.out.println("Enter the number of the entry to modify; -1 to cancel.");

            displayEntries(report);

            int entryNum = input.nextInt();

            if (entryNum >= report.size()) {
                System.out.println("There is no entry with the given number.");
            } else if (entryNum < 0) {
                System.out.println("No entry was modified.");
            } else {
                Entry entryToModify = report.getAllEntries().get(entryNum);

                displayModifySubmenu();

                String command = input.next();
                command.toLowerCase();

                doModification(command, entryToModify);
            }
        }
        System.out.println();
    }

    // MODIFIES: this
    // EFFECTS: processes user command and modifies the given entry
    private void doModification(String command, Entry entry) {
        if (command.equals("e")) {
            modifyDate(entry);
        } else if (command.equals("a")) {
            modifyAmount(entry);
        } else if (command.equals("d")) {
            modifyDescription(entry);
        } else {
            System.out.println("Uh Oh! You have entered an invalid input...\n");
        }
    }

    // MODIFIES: this, entry
    // EFFECTS: modifies the date of the given entry
    private void modifyDate(Entry entry) {
        Calendar date = makeDate();
        entry.setDate(date);
        System.out.println("The entry was modified");
    }

    // EFFECTS: processes user input and returns a new date
    private Calendar makeDate() {
        System.out.print("Enter the year: ");
        int year = input.nextInt();
        System.out.print("Enter the month (number): ");
        int month = input.nextInt() - 1;
        System.out.print("Enter the day (number): ");
        int day = input.nextInt();

        return new GregorianCalendar(year, month, day);
    }

    // MODIFIES: this, entry
    // EFFECTS: modifies the amount of the given entry if the inputted amount >= 0;
    //          otherwise prints an error message
    private void modifyAmount(Entry entry) {
        try {
            System.out.print("Enter the amount: ");
            double amount = input.nextDouble();
            entry.setAmount(amount);
            System.out.println("The entry was modified");
        } catch (NegativeInputException e) {
            System.out.println("\nThe amount cannot be negative. The entry was not modified\n");
        }
    }

    // MODIFIES: this, entry
    // EFFECTS: modifies the description of the given entry if the inputted description is not an empty string;
    //          otherwise prints an error message
    private void modifyDescription(Entry entry) {
        try {
            System.out.print("Enter the description: ");
            String description = inputSentence.next();
            entry.setDescription(description);
            System.out.println("The entry was modified");
        } catch (EmptyStringException e) {
            System.out.println("\nThe description cannot be empty. The entry was not modified\n");
        }
    }

    /** methods for data persistence **/

    // MODIFIES: this
    // EFFECTS: saves budget to file
    private void saveBudget() {
        try {
            jsonWriter.open();
            jsonWriter.write(budget);
            jsonWriter.close();
            System.out.println("Budget successfully saved to " + BUDGET_DATA);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to save budget to " + BUDGET_DATA);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads budget from file
    private void loadBudget() {
        try {
            budget = jsonReader.read();
            expenseReport = budget.getExpenseReport();
            incomeReport = budget.getIncomeReport();
            System.out.println("Budget successfully loaded from " + BUDGET_DATA);
        } catch (IOException e) {
            System.out.println("Unable to load budget from " + BUDGET_DATA);
        }

    }
}
