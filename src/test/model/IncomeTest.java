package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IncomeTest {
    Income testIncome;

    @BeforeEach
    void runBefore() {
        Calendar date = new GregorianCalendar();
        date.set(2019, 6, 21);
        testIncome = new Income("Paycheck from part-time job", 350.01, date);
    }

    @Test
    void testConstructor() {
        assertEquals("Paycheck from part-time job", testIncome.getDescription());
        assertEquals(350.01, testIncome.getAmount());
        assertEquals(2019, testIncome.getDate().getWeekYear());
    }

    @Test
    void testSetters() {
        testIncome.setDescription("Salary from full-time job");
        testIncome.setAmount(3500.00);
        testIncome.setDate(2019, 6, 21);

        assertEquals("Salary from full-time job", testIncome.getDescription());
        assertEquals(3500.00, testIncome.getAmount());
        assertEquals(2019, testIncome.getDate().get(Calendar.YEAR));
        assertEquals(6, testIncome.getDate().get(Calendar.MONTH));
        assertEquals(21, testIncome.getDate().get(Calendar.DAY_OF_MONTH));
    }

    @Test
    void testGetEntry() {
        Entry otherEntry = testIncome.getEntry();

        assertEquals(otherEntry, testIncome);
    }

    @Test
    void testToString() {
        assertEquals("2019-06-21\t\t\t$350.01         Paycheck from part-time job", testIncome.toString());
    }

}

