package model;

import java.util.Calendar;

// Represents an expense having a description, amount (in dollars), date of entry, and category
public class Expense extends Entry {

    // REQUIRES: description has non-zero length and amount > 0
    // EFFECTS: constructs an expense with given description, amount, date of entry
    public Expense(String description, double amount, Calendar date) {
        super(description, amount, date);
    }
}
