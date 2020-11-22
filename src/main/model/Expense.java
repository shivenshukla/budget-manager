package model;

import exception.EmptyStringException;
import exception.NegativeInputException;

import java.util.Calendar;

// Represents an expense having a description, amount (in dollars), date of entry
public class Expense extends Entry {

    // EFFECTS: constructs an entry with given description, amount (in dollars) and date of entry;
    //          throws NegativeInputException if amount is negative;
    //          throws EmptyStringException if description is an empty string
    public Expense(String description, double amount, Calendar date) throws NegativeInputException,
            EmptyStringException {
        super(description, amount, date);
    }
}
