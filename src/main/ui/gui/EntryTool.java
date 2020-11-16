package ui.gui;

import model.Entry;

import javax.swing.*;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

// I learned to create this code from reading the following pages on
// https://docs.oracle.com/javase/tutorial/uiswing/components/componentlist.html
//  - How to Use Spinners
//  - How to Use Text Fields

// Represents the JComponents used for user input when adding or modifying an entry
public class EntryTool {

    private JSpinner daySpinner;
    private JSpinner monthSpinner;
    private JSpinner yearSpinner;

    private JFormattedTextField amountField;
    private JTextField descriptionField;

    public EntryTool() {
        initializeSpinners();
        initializeFields();
    }

    // MODIFIES: this
    // EFFECTS: initializes all JSpinners
    public void initializeSpinners() {
        Calendar currentDate = new GregorianCalendar();
        int currentDay = currentDate.get(Calendar.DAY_OF_MONTH);
        int currentMonth = currentDate.get(Calendar.MONTH) + 1;
        int currentYear = currentDate.get(Calendar.YEAR);

        SpinnerNumberModel dayModel = new SpinnerNumberModel(currentDay, 1, 31, 1);
        daySpinner = new JSpinner(dayModel);

        SpinnerNumberModel monthModel = new SpinnerNumberModel(currentMonth, 1, 12, 1);
        monthSpinner = new JSpinner(monthModel);

        SpinnerNumberModel yearModel = new SpinnerNumberModel(currentYear, 1900, 2100, 1);
        yearSpinner = new JSpinner(yearModel);
        yearSpinner.setEditor(new JSpinner.NumberEditor(yearSpinner, "#"));
    }

    // MODIFIES: this
    // EFFECTS: initializes amountField and descriptionField
    public void initializeFields() {
        DecimalFormat amountFormat = new DecimalFormat("###,##0.00");
        amountField = new JFormattedTextField(amountFormat);
        amountField.setHorizontalAlignment(JTextField.CENTER);
        amountField.setValue(0);

        descriptionField = new JTextField();
        descriptionField.setHorizontalAlignment(JTextField.CENTER);
        descriptionField.setText("Enter here");
    }

    // MODIFIES: this
    // EFFECTS: sets spinner values to current date; sets description field to "Enter here";
    // sets amountField to 0.00
    public void resetAll() {
        Calendar currentDate = new GregorianCalendar();
        int currentDay = currentDate.get(Calendar.DAY_OF_MONTH);
        int currentMonth = currentDate.get(Calendar.MONTH) + 1;
        int currentYear = currentDate.get(Calendar.YEAR);

        daySpinner.setValue(currentDay);
        monthSpinner.setValue(currentMonth);
        yearSpinner.setValue(currentYear);
        amountField.setValue(0);
        descriptionField.setText("Enter here");
    }

    // MODIFIES: this
    // EFFECTS: sets all spinner and field values to corresponding value of field in entry
    public void setAll(Entry entry) {
        daySpinner.setValue(entry.getDate().get(Calendar.DAY_OF_MONTH));
        monthSpinner.setValue(entry.getDate().get(Calendar.MONTH) + 1);
        yearSpinner.setValue(entry.getDate().get(Calendar.YEAR));
        amountField.setValue(entry.getAmount());
        descriptionField.setText(entry.getDescription());
    }

    // Getters
    public JSpinner getDaySpinner() {
        return daySpinner;
    }

    public JSpinner getMonthSpinner() {
        return monthSpinner;
    }

    public JSpinner getYearSpinner() {
        return yearSpinner;
    }

    public JFormattedTextField getAmountField() {
        return amountField;
    }

    public JTextField getDescriptionField() {
        return descriptionField;
    }
}
