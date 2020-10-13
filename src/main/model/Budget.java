package model;

import java.util.List;

// Represents a budget having an expense report and income report
public class Budget {
    protected Report expenseReport;
    protected Report incomeReport;

    // EFFECTS: Constructs a budget with an empty expense report and an empty income report
    public Budget() {
        expenseReport = new ExpenseReport();
        incomeReport = new IncomeReport();
    }

    // EFFECTS: adds an expense to the expense report
    public void addExpense(Expense e) {
        expenseReport.addEntry(e);
    }

    // EFFECTS: adds an income the income report
    public void addIncome(Income i) {
        incomeReport.addEntry(i);
    }

    // EFFECTS: returns the difference in entry amounts between the income report and the expense report
    public double getDifference() {
        return incomeReport.sum() - expenseReport.sum();
    }

    // EFFECTS: returns true if total amount of budget is > 0; false otherwise
    public boolean isSurplus() {
        return (getDifference() > 0);
    }

    // EFFECTS: returns true if total amount of budget is < 0; false otherwise
    public boolean isDeficit() {
        return (getDifference() < 0);
    }

    // EFFECTS: returns the income report
    public Report getIncomeReport() {
        return this.incomeReport;
    }

    // EFFECTS: returns the expense report
    public Report getExpenseReport() {
        return this.expenseReport;
    }
}
