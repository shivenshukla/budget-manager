package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.jupiter.api.Assertions.assertEquals;

// Tests for the Entry class
public abstract class EntryTest {
    protected static final String DESCRIPTION = "Entry";
    protected static final double AMOUNT = 123.45;

    protected Entry testEntry;
    protected Calendar date;

    @BeforeEach
    void runBefore() {
        date = new GregorianCalendar();
        date.set(2020, 1, 15);
    }

    @Test
    void testConstructor() {
        assertEquals(DESCRIPTION, testEntry.getDescription());
        assertEquals(AMOUNT, testEntry.getAmount());
        assertEquals(2020, testEntry.getDate().get(Calendar.YEAR));
        assertEquals(1, testEntry.getDate().get(Calendar.MONTH));
        assertEquals(15, testEntry.getDate().get(Calendar.DAY_OF_MONTH));
    }

    @Test
    void testSetters() {
        testEntry.setDescription("New entry");
        testEntry.setAmount(69.99);
        testEntry.setDate(2019, 6, 21);

        assertEquals("New entry", testEntry.getDescription());
        assertEquals(69.99, testEntry.getAmount());
        assertEquals(2019, testEntry.getDate().get(Calendar.YEAR));
        assertEquals(6, testEntry.getDate().get(Calendar.MONTH));
        assertEquals(21, testEntry.getDate().get(Calendar.DAY_OF_MONTH));
    }

    @Test
    void testGetEntry() {
        Entry otherEntry = testEntry.getEntry();
        assertEquals(otherEntry, testEntry);
    }

    @Test
    void testToString() {
        assertEquals("2020-01-15\t\t\t$123.45         Entry", testEntry.toString());
    }
}
