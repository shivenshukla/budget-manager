package model;

import java.util.Calendar;

// Represents an income having a description, amount (in dollars), date of entry
public class Income extends Entry {

    // REQUIRES: description has non-zero length and amount >= 0
    // EFFECTS: constructs a new income with given description, amount, and date of entry
    public Income(String description, double amount, Calendar date) {
        super(description, amount, date);
    }
}
