package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Tests for the Report class
public abstract class ReportTest {
    // constants used to construct entries
    protected static final String DESCRIPTION_1 = "Entry 1";
    protected static final String DESCRIPTION_2 = "Entry 2";
    protected static final String DESCRIPTION_3 = "Entry 3";
    protected static final double AMOUNT_1 = 100.00;
    protected static final double AMOUNT_2 = 65.00;
    protected static final double AMOUNT_3 = 134.00;
    //constants used to construct dates
    protected static final int YEAR_1 = 2004;
    protected static final int YEAR_2 = 2008;
    protected static final int YEAR_3 = 2018;
    protected static final int DAY_1 = 3;
    protected static final int DAY_2 = 20;
    protected static final int DAY_3 = 7;

    protected Report testReport;
    protected Entry entry1;
    protected Entry entry2;
    protected Entry entry3;
    protected Calendar date1;
    protected Calendar date2;
    protected Calendar date3;

    @BeforeEach
    void runBefore() {
        date1 = new GregorianCalendar();
        date1.set(YEAR_1, Calendar.APRIL, DAY_1);

        date2 = new GregorianCalendar();
        date2.set(YEAR_2, Calendar.NOVEMBER, DAY_2);

        date3 = new GregorianCalendar();
        date3.set(YEAR_3, Calendar.AUGUST, DAY_3);
    }

    @Test
    void testConstructor() {
        assertEquals(0, testReport.size());
    }

    @Test
    void testAddEntryEmpty() {
        assertEquals(0, testReport.size());

        testReport.addEntry(entry1);

        assertEquals(1, testReport.size());
        assertTrue(testReport.contains(entry1));
    }

    @Test
    void testAddEntryMany() {
        assertEquals(0, testReport.size());

        testReport.addEntry(entry1);
        testReport.addEntry(entry2);

        assertEquals(2, testReport.size());

        testReport.addEntry(entry3);

        assertEquals(3, testReport.size());
        assertTrue(testReport.contains(entry1));
        assertTrue(testReport.contains(entry2));
        assertTrue(testReport.contains(entry3));
    }

    @Test
    void testDeleteEntryOne() {
        assertEquals(0, testReport.size());

        testReport.addEntry(entry1);

        assertEquals(1, testReport.size());
        assertTrue(testReport.contains(entry1));

        testReport.deleteEntry(entry1);

        assertEquals(0, testReport.size());
        assertFalse(testReport.contains(entry1));
    }

    @Test
    void testDeleteEntryMany() {
        assertEquals(0, testReport.size());

        addAllEntries();

        assertEquals(3, testReport.size());
        assertTrue(testReport.contains(entry1));
        assertTrue(testReport.contains(entry2));
        assertTrue(testReport.contains(entry3));

        testReport.deleteEntry(entry2);

        assertEquals(2, testReport.size());
        assertTrue(testReport.contains(entry1));
        assertFalse(testReport.contains(entry2));
        assertTrue(testReport.contains(entry3));

        testReport.deleteEntry(entry1);

        assertEquals(1, testReport.size());
        assertFalse(testReport.contains(entry1));
        assertTrue(testReport.contains(entry3));
    }

    @Test
    void testGetEntriesInRangeNoneInRange() {
        Calendar startDate = new GregorianCalendar();
        startDate.set(YEAR_1 - 1, Calendar.DECEMBER, DAY_1);

        Calendar endDate = new GregorianCalendar();
        endDate.set(YEAR_1, Calendar.APRIL, DAY_1 - 1);

        addAllEntries();

        assertEquals(3, testReport.size());

        List<Entry> entries = testReport.getEntriesInRange(startDate, endDate);

        assertEquals(0, entries.size());
    }

    @Test
    void testGetEntriesInRangeOneInRange() {
        Calendar endDate = new GregorianCalendar();
        endDate.set(YEAR_3, Calendar.JANUARY, DAY_2);

        addAllEntries();

        assertEquals(3, testReport.size());

        List<Entry> entries = testReport.getEntriesInRange(date2, endDate);

        assertEquals(1, entries.size());
        assertTrue(entries.contains(entry2));
    }

    @Test
    void testGetEntriesInRangeAllInRange() {
        addAllEntries();

        assertEquals(3, testReport.size());

        List<Entry> entries = testReport.getEntriesInRange(date1, date3);

        assertEquals(3, entries.size());
        assertTrue(entries.contains(entry1));
        assertTrue(entries.contains(entry2));
        assertTrue(entries.contains(entry3));
    }

    @Test
    void testGetAllEntriesEmpty() {
        assertEquals(0, testReport.size());

        List<Entry> entries = testReport.getAllEntries();

        assertEquals(0, entries.size());
    }

    @Test
    void testGetAllEntriesMany() {
        addAllEntries();

        assertEquals(3, testReport.size());

        List<Entry> entries = testReport.getAllEntries();

        assertEquals(3, entries.size());
        assertTrue(entries.contains(entry1));
        assertTrue(entries.contains(entry2));
        assertTrue(entries.contains(entry3));
    }

    @Test
    void testSumEntriesEmpty() {
        assertEquals(0, testReport.size());
        assertEquals(0, testReport.sum());
    }

    @Test
    void testSumEntriesMany() {
        addAllEntries();

        assertEquals(3, testReport.size());

        double total = entry1.getAmount() + entry2.getAmount() + entry3.getAmount();
        assertEquals(total, testReport.sum());
    }

    @Test
    void testIsEmptyEmpty() {
        assertEquals(0, testReport.size());
        assertTrue(testReport.isEmpty());
    }

    @Test
    void testIsEmptyMany() {
        addAllEntries();

        assertEquals(3, testReport.size());
        assertFalse(testReport.isEmpty());
    }

    // MODIFIES: this
    // EFFECTS: adds all entries to the testReport
    private void addAllEntries() {
        testReport.addEntry(entry1);
        testReport.addEntry(entry2);
        testReport.addEntry(entry3);
    }
}
