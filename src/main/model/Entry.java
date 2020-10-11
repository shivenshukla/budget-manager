package model;

import java.util.Calendar;

// Represents an entry having a description, amount (in dollars), and date of entry
public abstract class Entry {
    protected String description;
    protected double amount;
    protected Calendar date;

    // REQUIRES: description has non-zero length and amount > 0
    // EFFECTS: constructs an entry with given description, amount, date of entry;
    public Entry(String description, double amount, Calendar date) {
        this.description = description;
        this.amount = amount;
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public Entry getEntry() {
        return this;
    }

    public String getDescription() {
        return this.description;
    }

    public double getAmount() {
        return this.amount;
    }

    public Calendar getDate() {
        return this.date;
    }
}
