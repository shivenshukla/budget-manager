package model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

// Represents a report of entries
public abstract class Report {
    List<Entry> entries;

    // EFFECTS: constructs an empty report
    public Report() {
        entries = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: adds an entry to the report
    public void addEntry(Entry e) {
        entries.add(e);
    }

    // REQUIRES: entry exists in the report
    // MODIFIES: this
    // EFFECTS: removes entry from the report
    public void deleteEntry(Entry e) {
        entries.remove(e);
    }

    // EFFECTS: returns the number of entries in the report
    public int size() {
        return entries.size();
    }

    // EFFECTS: returns true if entry exists in the report; false otherwise
    public boolean contains(Entry entry) {
        return entries.contains(entry);
    }

    // EFFECTS: returns all entries in the report from startDate to endDate
    public List<Entry> getEntriesInRange(Calendar startDate, Calendar endDate) {
        List<Entry> entriesInRange = new ArrayList<>();

        for (Entry e : entries) {
            if (!(e.date.before(startDate) || e.date.after(endDate))) {
                entriesInRange.add(e);
            }
        }
        return entriesInRange;
    }

    // EFFECTS: returns all entries in the report
    public List<Entry> getAllEntries() {
        return entries;
    }

    // EFFECTS: returns the total amount of all entries
    public double sum() {
        double result = 0;

        for (Entry e: entries) {
            result += e.getAmount();
        }
        return result;
    }

    // EFFECTS: returns true if report has no entries
    public boolean isEmpty() {
        return (entries.size() == 0);
    }
}
