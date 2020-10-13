package model;

import java.util.Calendar;

// Represents an income having a description, amount (in dollars), date of entry, and category
public class Income extends Entry {

    // REQUIRES: description has non-zero length and amount > 0
    // EFFECTS: constructs an income with given description, amount, date of entry; category is set to unspecified
    public Income(String description, double amount, Calendar date) {
        super(description, amount, date);
    }
}
