package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.jupiter.api.Assertions.*;

public class BudgetTest {
    Budget testBudget;

    // constants used to construct entries
    protected static final String DESCRIPTION_1 = "Entry 1";
    protected static final String DESCRIPTION_2 = "Entry 2";
    protected static final double AMOUNT_1 = 100.00;
    protected static final double AMOUNT_2 = 65.58;

    //constants used to construct dates
    protected static final int YEAR_1 = 2008;
    protected static final int YEAR_2 = 2008;
    protected static final int DAY_1 = 3;
    protected static final int DAY_2 = 19;

    Expense expense1;
    Income income1;
    Calendar date1;
    Calendar date2;

    @BeforeEach
    void runBefore() {
        testBudget = new Budget();

        date1 = new GregorianCalendar();
        date1.set(YEAR_1, Calendar.APRIL, DAY_1);

        date2 = new GregorianCalendar();
        date2.set(YEAR_2, Calendar.APRIL, DAY_2);
    }

    @Test
    void testIsSurplus() {
        expense1 = new Expense(DESCRIPTION_2, AMOUNT_2, date2);
        income1 = new Income(DESCRIPTION_1, AMOUNT_1, date1);

        testBudget.addExpense(expense1);
        testBudget.addIncome(income1);

        assertTrue(testBudget.isSurplus());
    }

    @Test
    void testIsSurplusZeroCase() {
        expense1 = new Expense(DESCRIPTION_1, AMOUNT_1, date2);
        income1 = new Income(DESCRIPTION_2, AMOUNT_1, date1);

        testBudget.addExpense(expense1);
        testBudget.addIncome(income1);

        assertFalse(testBudget.isSurplus());
    }

    @Test
    void testIsDeficit() {
        expense1 = new Expense(DESCRIPTION_1, AMOUNT_1, date1);
        income1 = new Income(DESCRIPTION_2, AMOUNT_2, date2);

        testBudget.addExpense(expense1);
        testBudget.addIncome(income1);

        assertTrue(testBudget.isDeficit());
    }

    @Test
    void testIsDeficitZeroCase() {
        expense1 = new Expense(DESCRIPTION_1, AMOUNT_1, date2);
        income1 = new Income(DESCRIPTION_2, AMOUNT_1, date1);

        testBudget.addExpense(expense1);
        testBudget.addIncome(income1);

        assertFalse(testBudget.isDeficit());
    }

    @Test
    void testGetters() {
        expense1 = new Expense(DESCRIPTION_2, AMOUNT_2, date2);
        income1 = new Income(DESCRIPTION_1, AMOUNT_1, date1);

        testBudget.addExpense(expense1);
        testBudget.addIncome(income1);

        Report otherExpenseReport = testBudget.getExpenseReport();
        Report otherIncomeReport = testBudget.getIncomeReport();

        assertEquals(1, otherExpenseReport.size());
        assertEquals(1, otherIncomeReport.size());

        assertTrue(otherExpenseReport.contains(expense1));
        assertTrue(otherIncomeReport.contains(income1));
    }
}
