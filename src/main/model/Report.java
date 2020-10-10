package model;

import java.util.GregorianCalendar;
import java.util.List;

public abstract class Report {

    public void addEntry() {
        // stub
    }

    public void deleteEntry() {
        // stub
    }

    public void deleteExpense() {
        // stub
    }

    public List<Entry> getEntriesInRange(GregorianCalendar startDate, GregorianCalendar endDate) {
        return null;
    }

    public List<Income> getAllEntries() {
        return null;
    }
}
