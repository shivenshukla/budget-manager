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
}
