package model;

import java.util.Calendar;
import java.util.List;

// Represents a report of entries
public abstract class Report {

    // EFFECTS: constructs an empty report
    public Report() {
        // stub
    }

    // MODIFIES: this
    // EFFECTS: adds an entry to the report
    public void addEntry(String description, double amount, Calendar date) {
        // stub
    }

    // REQUIRES: entry exists in the report
    // MODIFIES: this
    // EFFECTS: removes entry from the report
    public void deleteEntry(Entry entry) {
        // stub
    }

    // EFFECTS: returns the number of entries in the report
    public int size() {
        return 0; // stub
    }

    // EFFECTS: returns true if entry exists in the report; false otherwise
    public boolean contains(Entry entry) {
        return false;
    }

    // EFFECTS: returns all entries in the report from startDate to endDate
    public List<Entry> getEntriesInRange(Calendar startDate, Calendar endDate) {
        return null;
    }

    // EFFECTS: returns all entries in the report
    public List<Entry> getAllEntries() {
        return null;
    }
}
