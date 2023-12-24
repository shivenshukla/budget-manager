package model;

import exception.EmptyStringException;
import exception.NegativeInputException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

// Tests for the Entry class
public abstract class EntryTest {
    protected static final String DESCRIPTION = "Entry";
    protected static final double AMOUNT = 123.45;

    protected Entry testEntry;
    protected Calendar date;

    @BeforeEach
    void runBefore() {
        date = new GregorianCalendar(2020, Calendar.FEBRUARY, 15);
    }

    @Test
    void testSetDescriptionNoException() {
        try {
            testEntry.setDescription("Morales Miles ManSpider");
            assertEquals("Morales Miles ManSpider", testEntry.getDescription());
        } catch (EmptyStringException e) {
            fail("EmptyStringException should have not been thrown");
        }
    }

    @Test
    void testSetDescriptionExpectEmptyStringException() {
        try {
            testEntry.setDescription("");
            fail("EmptyStringException was not thrown");
        } catch (EmptyStringException e) {
            // expected
        }
    }

    @Test
    void testSetAmountNoException() {
        try {
            testEntry.setAmount(0);
        } catch (NegativeInputException e) {
            fail("NegativeInputException should have not been thrown");
        }
    }

    @Test
    void testSetAmountExpectNegativeInputException() {
        try {
            testEntry.setAmount(-1);
            fail("NegativeInputException was not thrown");
        } catch (NegativeInputException e) {
            // expected
        }
    }

    @Test
    void testSetDateIntegerParameters() {
        testEntry.setDate(2019, 6, 21);
        assertEquals(2019, testEntry.getDate().get(Calendar.YEAR));
        assertEquals(6, testEntry.getDate().get(Calendar.MONTH));
        assertEquals(21, testEntry.getDate().get(Calendar.DAY_OF_MONTH));
    }

    @Test
    void testSetDateCalendarParameter() {
        testEntry.setDate(new GregorianCalendar(2013, Calendar.DECEMBER, 15));
        assertEquals(2013, testEntry.getDate().get(Calendar.YEAR));
        assertEquals(11, testEntry.getDate().get(Calendar.MONTH));
        assertEquals(15, testEntry.getDate().get(Calendar.DAY_OF_MONTH));
    }

    @Test
    void testGetEntry() {
        Entry otherEntry = testEntry.getEntry();
        assertEquals(otherEntry, testEntry);
    }

    @Test
    void testToString() {
        assertEquals("Feb 15 2020                   $123.45                        Entry",
                testEntry.toString());
    }
}
