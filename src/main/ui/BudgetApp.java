package ui;

import model.Budget;

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
        System.out.println("Welcome to Budget Rocket!\n");

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
    // EFFECTS: processes user command
    private void processCommand(String command) {
        if (command.equals("b")) {
            viewBudgetReport();
        } else if (command.equals("e")) {
            viewExpenseReport();
        } else if (command.equals("e")) {
            viewIncomeReport();
        } else {
            System.out.println("Uh Oh! You have entered an invalid input...");
        }
    }

    // MODIFIES: this
    // EFFECTS: initializes budget
    private void initialize() {
    }

    private void viewBudgetReport() {
    }

    private void viewIncomeReport() {
    }

    private void viewExpenseReport() {
    }

    // EFFECTS: displays menu of options to user
    private void displayMainMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\tb -> view budget report");
        System.out.println("\te -> view expense report");
        System.out.println("\ti -> view income report");
    }

    private void displaySubMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\tm -> modify an existing entry");
        System.out.println("\ta -> add a new entry");
        System.out.println("\td -> delete an existing entry");
    }

    private void addExpense() {

    }

    private void addIncome() {

    }

    private void modifyExpense() {

    }

    private void modifyIncome() {

    }
}
