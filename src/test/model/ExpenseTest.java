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
        date.set(2020, Calendar.JANUARY, 15);
        testExpense = new Expense("Groceries purchased at supermarket", 123.45, date);
    }

    @Test
    void testConstructor() {
        assertEquals("Groceries purchased at supermarket", testExpense.getDescription());
        assertEquals(123.45, testExpense.getAmount());
        assertEquals(2020, testExpense.getDate().getWeekYear());
        assertEquals(ExpenseCategory.UNSPECIFIED, testExpense.getCategory());
    }

    @Test
    void testSetters() {
        Calendar otherDate = new GregorianCalendar();
        otherDate.set(2019, Calendar.JUNE, 21);

        testExpense.setDescription("Purchased a year-long gaming subscription");
        testExpense.setAmount(69.99);
        testExpense.setDate(otherDate);
        testExpense.setCategory(ExpenseCategory.ENTERTAINMENT);

        assertEquals("Purchased a year-long gaming subscription", testExpense.getDescription());
        assertEquals(69.99, testExpense.getAmount());
        assertEquals(otherDate, testExpense.getDate());
        assertEquals(ExpenseCategory.ENTERTAINMENT, testExpense.getCategory());
    }

    @Test
    void testGetEntry() {
        Entry otherEntry = testExpense.getEntry();
        assertEquals(otherEntry, testExpense);
    }
}