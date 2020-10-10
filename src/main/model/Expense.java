package model;

import java.util.Calendar;

// Represents an expense having a description, amount (in dollars), date of entry, and category
public class Expense extends Entry {
    protected ExpenseCategory category;

    // REQUIRES: description has non-zero length and amount > 0
    // EFFECTS: constructs an expense with given description, amount, date of entry; category is set to unspecified
    public Expense(String description, double amount, Calendar date) {
        super(description, amount, date);
        this.category = ExpenseCategory.UNSPECIFIED;
    }

    public void setCategory(ExpenseCategory category) {
        this.category = category;
    }

    public ExpenseCategory getCategory() {
        return this.category;
    }
}
