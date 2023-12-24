package model;

import exception.EmptyStringException;
import exception.NegativeInputException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

// Tests for the Expense class
public class ExpenseTest extends EntryTest{

    @BeforeEach
    void runBefore() {
        super.runBefore();
        try {
            testEntry = new Expense(DESCRIPTION, AMOUNT, date);
        } catch (Exception e) {
            fail("Cannot run tests: failed to construct test entries");
        }
    }

    @Test
    void testConstructorNoException() {
        try {
            testEntry = new Expense(DESCRIPTION, 0, date);
            assertEquals(DESCRIPTION, testEntry.getDescription());
            assertEquals(0, testEntry.getAmount());
            assertEquals(2020, testEntry.getDate().get(Calendar.YEAR));
            assertEquals(1, testEntry.getDate().get(Calendar.MONTH));
            assertEquals(15, testEntry.getDate().get(Calendar.DAY_OF_MONTH));
        } catch (NegativeInputException e) {
            fail("NegativeInputException should have not been thrown");
        } catch (EmptyStringException e) {
            fail("EmptyStringException should have not been thrown");
        }
    }

    @Test
    void testConstructorExpectNegativeInputException() {
        try {
            testEntry = new Expense(DESCRIPTION, -1, date);
            fail("NegativeInputException was not thrown");
        } catch (NegativeInputException e) {
            // expected
        } catch (EmptyStringException e) {
            fail("EmptyStringException thrown, expected NegativeInputException");
        }
    }

    @Test
    void testConstructorExpectEmptyStringException() {
        try {
            testEntry = new Expense("", 100, date);
            fail("EmptyStringException was not thrown");
        } catch (NegativeInputException e) {
            fail("NegativeInputException thrown, expected EmptyStringException");
        } catch (EmptyStringException e) {
            // expected
        }
    }
}