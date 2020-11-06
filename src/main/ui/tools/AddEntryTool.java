package ui.tools;

import javax.swing.*;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class AddEntryTool {

    private JSpinner daySpinner;
    private JSpinner monthSpinner;
    private JSpinner yearSpinner;

    private JFormattedTextField amountField;
    private JTextField descriptionField;

    public AddEntryTool() {
        initializeSpinners();
        initializeFields();
    }

    // MODIFIES: this
    // EFFECTS: initializes all jSpinners
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
    }

    // MODIFIES: this
    // EFFECTS: initializes amountField and descriptionField
    public void initializeFields() {
        NumberFormat amountFormat = NumberFormat.getNumberInstance();
        amountFormat.setMinimumFractionDigits(2);
        amountField = new JFormattedTextField(amountFormat);
        amountField.setValue(0);

        descriptionField = new JTextField();
        descriptionField.setText("Enter here");
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
