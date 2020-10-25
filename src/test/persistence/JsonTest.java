package persistence;

import model.Entry;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {

    // REQUIRES: description has non-zero length and amount >= 0
    // EFFECTS: checks if entry was read correctly
    protected void checkEntry(Entry entry, String description, double amount, int year, int month, int day) {
        assertEquals(description, entry.getDescription());
        assertEquals(amount, entry.getAmount());
        assertEquals(year, entry.getDate().get(Calendar.YEAR));
        assertEquals(month, entry.getDate().get(Calendar.MONTH));
        assertEquals(day, entry.getDate().get(Calendar.DAY_OF_MONTH));
    }
}
