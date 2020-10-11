package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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

    Report testReport;
    Entry entry1;
    Entry entry2;
    Entry entry3;
    Calendar date1;
    Calendar date2;
    Calendar date3;

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
    void testAddEntryEmpty() {
        assertEquals(0, testReport.size());

        testReport.addEntry(DESCRIPTION_1, 100.00, date1);

        assertEquals(1, testReport.size());
        assertTrue(testReport.contains(entry1));
    }

    @Test
    void testAddEntryMany() {
        assertEquals(0, testReport.size());

        testReport.addEntry(DESCRIPTION_1, AMOUNT_1, date1);
        testReport.addEntry(DESCRIPTION_2, AMOUNT_2, date2);

        assertEquals(2, testReport.size());

        testReport.addEntry(DESCRIPTION_3, AMOUNT_3, date3);

        assertEquals(3, testReport.size());
        assertTrue(testReport.contains(entry1));
        assertTrue(testReport.contains(entry2));
        assertTrue(testReport.contains(entry3));
    }

    @Test
    void testDeleteEntryOne() {
        assertEquals(0, testReport.size());

        testReport.addEntry(DESCRIPTION_1, AMOUNT_1, date1);

        assertEquals(1, testReport.size());
        assertTrue(testReport.contains(entry1));

        testReport.deleteEntry(entry1);

        assertEquals(0, testReport.size());
        assertFalse(testReport.contains(entry1));
    }

    @Test
    void testDeleteEntryMany() {
        assertEquals(0, testReport.size());

        testReport.addEntry(DESCRIPTION_1, AMOUNT_1, date1);
        testReport.addEntry(DESCRIPTION_2, AMOUNT_2, date2);
        testReport.addEntry(DESCRIPTION_3, AMOUNT_3, date3);

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

        testReport.addEntry(DESCRIPTION_1, AMOUNT_1, date1);
        testReport.addEntry(DESCRIPTION_2, AMOUNT_2, date2);
        testReport.addEntry(DESCRIPTION_3, AMOUNT_3, date3);

        assertEquals(3, testReport.size());

        List<Entry> entries = testReport.getEntriesInRange(startDate, endDate);

        assertEquals(0, entries.size());
    }

    @Test
    void testGetEntriesInRangeOneInRange() {
        Calendar endDate = new GregorianCalendar();
        endDate.set(YEAR_2, Calendar.APRIL, DAY_2 - 1);

        testReport.addEntry(DESCRIPTION_1, AMOUNT_1, date1);
        testReport.addEntry(DESCRIPTION_2, AMOUNT_2, date2);
        testReport.addEntry(DESCRIPTION_3, AMOUNT_3, date3);

        assertEquals(3, testReport.size());

        List<Entry> entries = testReport.getEntriesInRange(date1, endDate);

        assertEquals(1, entries.size());
    }

    @Test
    void testGetEntriesInRangeAllInRange() {
        testReport.addEntry(DESCRIPTION_1, AMOUNT_1, date1);
        testReport.addEntry(DESCRIPTION_2, AMOUNT_2, date2);
        testReport.addEntry(DESCRIPTION_3, AMOUNT_3, date3);

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
        testReport.addEntry(DESCRIPTION_1, AMOUNT_1, date1);
        testReport.addEntry(DESCRIPTION_2, AMOUNT_2, date2);
        testReport.addEntry(DESCRIPTION_3, AMOUNT_3, date3);

        assertEquals(3, testReport.size());

        List<Entry> entries = testReport.getAllEntries();

        assertEquals(3, entries.size());
        assertTrue(entries.contains(entry1));
        assertTrue(entries.contains(entry2));
        assertTrue(entries.contains(entry3));
    }
}
