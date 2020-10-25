package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

// Represents a report which is a list of entries
public abstract class Report implements Writable {
    List<Entry> entries;

    // EFFECTS: constructs an empty Report
    public Report() {
        entries = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: adds an entry to the Report
    public void addEntry(Entry e) {
        entries.add(e);
    }

    // REQUIRES: entry exists in the Report
    // MODIFIES: this
    // EFFECTS: removes entry from the Report
    public void deleteEntry(Entry e) {
        entries.remove(e);
    }

    // EFFECTS: returns the number of entries in the Report
    public int size() {
        return entries.size();
    }

    // EFFECTS: returns true if entry exists in the Report; false otherwise
    public boolean contains(Entry entry) {
        return entries.contains(entry);
    }

    // EFFECTS: returns all entries in the report from startDate to endDate, inclusive
    public List<Entry> getEntriesInRange(Calendar startDate, Calendar endDate) {
        List<Entry> entriesInRange = new ArrayList<>();

        for (Entry e : entries) {
            if (!(e.date.before(startDate) || e.date.after(endDate))) {
                entriesInRange.add(e);
            }
        }
        return entriesInRange;
    }

    // EFFECTS: returns all entries in the Report
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

    // EFFECTS: returns true if the Report has no entries
    public boolean isEmpty() {
        return (entries.size() == 0);
    }

    // EFFECTS: returns incomeReport as a JSON object
    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("entries", entriesToJSon());
        return jsonObject;
    }

    // EFFECTS: returns entries in the Report as a JSON array
    protected JSONArray entriesToJSon() {
        JSONArray jsonArray = new JSONArray();

        for (Entry e: this.entries) {
            jsonArray.put(e.toJson());
        }
        return  jsonArray;
    }
}
