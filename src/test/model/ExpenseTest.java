package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseTest {
    Expense testExpense;

    @BeforeEach
    void runBefore() {
        Calendar date = new GregorianCalendar();
        date.set(2020, 1, 15);
        testExpense = new Expense("Groceries purchased at supermarket", 123.45, date);
    }

    @Test
    void testConstructor() {
        assertEquals("Groceries purchased at supermarket", testExpense.getDescription());
        assertEquals(123.45, testExpense.getAmount());
        assertEquals(2020, testExpense.getDate().getWeekYear());
    }

    @Test
    void testSetters() {
        testExpense.setDescription("Purchased a year-long gaming subscription");
        testExpense.setAmount(69.99);
        testExpense.setDate(2019, 6, 21);

        assertEquals("Purchased a year-long gaming subscription", testExpense.getDescription());
        assertEquals(69.99, testExpense.getAmount());
        assertEquals(2019, testExpense.getDate().get(Calendar.YEAR));
        assertEquals(6, testExpense.getDate().get(Calendar.MONTH));
        assertEquals(21, testExpense.getDate().get(Calendar.DAY_OF_MONTH));
    }

    @Test
    void testGetEntry() {
        Entry otherEntry = testExpense.getEntry();
        assertEquals(otherEntry, testExpense);
    }

    @Test
    void testToString(){
        assertEquals("2020-01-15\t\t\t$123.45         Groceries purchased at supermarket",
                testExpense.toString());
    }
}