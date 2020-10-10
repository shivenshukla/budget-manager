package model;

import java.util.Calendar;

// Represents an income having a description, amount (in dollars), date of entry, and category
public class Income extends Entry {
    protected IncomeCategory category;

    // REQUIRES: description has non-zero length and amount > 0
    // EFFECTS: constructs an income with given description, amount, date of entry; category is set to unspecified
    public Income(String description, double amount, Calendar date) {
        super(description, amount, date);
        this.category = IncomeCategory.UNSPECIFIED;
    }

    public void setCategory(IncomeCategory category) {
        this.category = category;
    }

    public IncomeCategory getCategory() {
        return this.category;
    }
}
