package model;

import java.util.Calendar;

// Represents an expense having a description, amount (in dollars), date of entry
public class Expense extends Entry {

    // REQUIRES: description has non-zero length and amount > 0
    // EFFECTS: constructs a new expense with given description, amount and date of entry
    public Expense(String description, double amount, Calendar date) {
        super(description, amount, date);
    }
}
