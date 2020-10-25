package model;

import org.json.JSONObject;
import persistence.Writable;

// Represents a budget having an expense report and income report
public class Budget implements Writable {
    protected Report expenseReport;
    protected Report incomeReport;

    // EFFECTS: Constructs a budget with an empty expenseReport and an empty incomeReport
    public Budget() {
        expenseReport = new ExpenseReport();
        incomeReport = new IncomeReport();
    }

    // EFFECTS: adds an expense to the expenseReport
    public void addExpense(Expense e) {
        expenseReport.addEntry(e);
    }

    // EFFECTS: adds an income to the incomeReport
    public void addIncome(Income i) {
        incomeReport.addEntry(i);
    }

    // EFFECTS: returns the difference in the total dollar amount between incomeReport and expenseReport
    public double getDifference() {
        return incomeReport.sum() - expenseReport.sum();
    }

    // EFFECTS: returns true if total dollar amount of budget is > 0; false otherwise
    public boolean isSurplus() {
        return (getDifference() > 0);
    }

    // EFFECTS: returns true if total dollar amount of budget is < 0; false otherwise
    public boolean isDeficit() {
        return (getDifference() < 0);
    }

    // Getters
    public Report getIncomeReport() {
        return this.incomeReport;
    }

    public Report getExpenseReport() {
        return this.expenseReport;
    }

    // EFFECTS: returns budget as a JSON object
    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("expenseReport", this.expenseReport.toJson());
        jsonObject.put("incomeReport", this.incomeReport.toJson());
        return  jsonObject;
    }
}
