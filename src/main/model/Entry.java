package model;

import exception.EmptyStringException;
import exception.NegativeInputException;
import org.json.JSONObject;
import persistence.Writable;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

// Documentation for Calendar: https://docs.oracle.com/javase/7/docs/api/java/util/Calendar.html
// Documentation for DecimalFormat: https://docs.oracle.com/javase/7/docs/api/java/text/DecimalFormat.html
// Documentation for SimpleDateFormat: https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html

// Represents an entry for a report having a description, amount (in dollars), and date of entry
public abstract class Entry implements Writable {
    protected String description;
    protected double amount;
    protected Calendar date;

    // EFFECTS: constructs an entry with given description, amount (in dollars) and date of entry;
    //          throws NegativeInputException if amount is negative;
    //          throws EmptyStringException if description is an empty string
    public Entry(String description, double amount, Calendar date) throws EmptyStringException, NegativeInputException {
        if (description.isEmpty()) {
            throw  new EmptyStringException();
        } else if (amount < 0) {
            throw  new NegativeInputException();
        }
        this.description = description;
        this.amount = amount;
        this.date = date;
    }

    // Setters

    public void setDescription(String description) throws EmptyStringException {
        if (description.isEmpty()) {
            throw new EmptyStringException();
        }
        this.description = description;
    }

    public void setAmount(double amount) throws NegativeInputException {
        if (amount < 0) {
            throw new NegativeInputException();
        }
        this.amount = amount;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public void setDate(int year, int month, int day) {
        this.date.set(year, month, day);
    }


    // Getters
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

    // EFFECTS: Returns a string representation of entry
    @Override
    public String toString() {
        DecimalFormat decimalFormat = new DecimalFormat("###,##0.00");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd yyyy");

        String amountStr = decimalFormat.format(this.amount);
        amountStr = String.format("%-30s", amountStr);

        String dateStr = simpleDateFormat.format(this.date.getTime());
        dateStr = String.format("%-30s", dateStr);

        return  dateStr + "$" + amountStr + this.description;
    }

    // EFFECTS: Returns entry as a JSON object
    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("description", this.description);
        jsonObject.put("amount", this.amount);
        jsonObject.put("date", dateToJson());
        return jsonObject;
    }

    // EFFECTS: Returns date as a JSON object
    private JSONObject dateToJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("year", this.date.get(Calendar.YEAR));
        jsonObject.put("month", this.date.get(Calendar.MONTH));
        jsonObject.put("day", this.date.get(Calendar.DAY_OF_MONTH));
        return jsonObject;
    }
}
