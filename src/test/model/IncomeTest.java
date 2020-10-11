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
        date.set(2019, Calendar.JUNE, 21);
        testIncome = new Income("Paycheck from part-time job", 350.01, date);
    }

    @Test
    void testConstructor() {
        assertEquals("Paycheck from part-time job", testIncome.getDescription());
        assertEquals(350.01, testIncome.getAmount());
        assertEquals(2019, testIncome.getDate().getWeekYear());
        assertEquals(IncomeCategory.UNSPECIFIED, testIncome.getCategory());
    }

    @Test
    void testSetters() {
        Calendar otherDate = new GregorianCalendar();
        otherDate.set(2019, Calendar.JUNE, 21);

        testIncome.setDescription("Salary from full-time job");
        testIncome.setAmount(3500.00);
        testIncome.setDate(otherDate);
        testIncome.setCategory(IncomeCategory.ALLOWANCE);

        assertEquals("Salary from full-time job", testIncome.getDescription());
        assertEquals(3500.00, testIncome.getAmount());
        assertEquals(otherDate, testIncome.getDate());
        assertEquals(IncomeCategory.ALLOWANCE, testIncome.getCategory());
    }

    @Test
    void testGetEntry() {
        Entry otherEntry = testIncome.getEntry();

        assertEquals(otherEntry, testIncome);
    }

}

