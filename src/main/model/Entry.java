package model;

import java.util.Calendar;

// Represents an entry for a report having a description, amount (in dollars), and date of entry
// Calendar object referenced from https://docs.oracle.com/javase/7/docs/api/java/util/Calendar.html
public abstract class Entry {
    protected String description;
    protected double amount;
    protected Calendar date;

    // REQUIRES: description has non-zero length and amount >= 0
    // EFFECTS: constructs an entry with given description, amount (in dollars) and date of entry
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

    public void setDate(int year, int month, int day) {
        this.date.set(year, month, day);
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

    // Returns a string representation of entry
    @Override
    public String toString() {
        String amountStr = String.format("%-15.2f", this.amount);
        String yearStr = String.format("%d", this.date.get(Calendar.YEAR));
        String monthStr = String.format("%02d", this.date.get(Calendar.MONTH));
        String dayString = String.format("%02d", this.date.get(Calendar.DAY_OF_MONTH));
        return yearStr + "-" + monthStr + "-" + dayString + "\t\t\t$" + amountStr + description;
    }
}
