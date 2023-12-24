package model;

import exception.EmptyStringException;
import exception.NegativeInputException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.jupiter.api.Assertions.*;

// Tests for the budget class
public class BudgetTest {
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

    protected Budget testBudget;
    protected Expense testExpense;
    protected Income testIncome;
    protected Calendar date1;
    protected Calendar date2;

    @BeforeEach
    void runBefore() {
        testBudget = new Budget();
        date1 = new GregorianCalendar(YEAR_1, Calendar.APRIL, DAY_1);
        date2 = new GregorianCalendar(YEAR_2, Calendar.APRIL, DAY_2);
    }

    @Test
    void testConstructor() {
        Report expenseReport = testBudget.getExpenseReport();
        Report incomeReport = testBudget.getIncomeReport();
        assertEquals(0, expenseReport.size());
        assertEquals(0, incomeReport.size());
    }

    @Test
    void testAddExpense() {
        try {
            testExpense = new Expense(DESCRIPTION_2, AMOUNT_2, date2);
            Report otherExpenseReport = testBudget.getExpenseReport();
            Report otherIncomeReport = testBudget.getIncomeReport();
            assertEquals(0, otherExpenseReport.size());
            assertEquals(0, otherIncomeReport.size());
            testBudget.addExpense(testExpense);
            assertEquals(1, otherExpenseReport.size());
            assertEquals(0, otherIncomeReport.size());
            assertTrue(otherExpenseReport.contains(testExpense));
            assertFalse(otherIncomeReport.contains(testExpense));
        } catch (NegativeInputException e) {
            fail("NegativeInputException should have not been thrown");
        } catch (EmptyStringException e) {
            fail("EmptyStringException should have not been thrown");
        }
    }

    @Test
    void testAddIncome() {
        try {
            testIncome = new Income(DESCRIPTION_1, AMOUNT_1, date1);
            Report otherExpenseReport = testBudget.getExpenseReport();
            Report otherIncomeReport = testBudget.getIncomeReport();
            assertEquals(0, otherExpenseReport.size());
            assertEquals(0, otherIncomeReport.size());
            testBudget.addIncome(testIncome);
            assertEquals(0, otherExpenseReport.size());
            assertEquals(1, otherIncomeReport.size());
            assertFalse(otherExpenseReport.contains(testIncome));
            assertTrue(otherIncomeReport.contains(testIncome));
        } catch (NegativeInputException e) {
            fail("NegativeInputException should not have been thrown");
        } catch (EmptyStringException e) {
            fail("EmptyStringException should not have been thrown");
        }
    }

    @Test
    void testIsSurplus() {
        try {
            testExpense = new Expense(DESCRIPTION_2, AMOUNT_2, date2);
            testIncome = new Income(DESCRIPTION_1, AMOUNT_1, date1);
            addEntries(testExpense, testIncome);
            assertTrue(testBudget.isSurplus());
        } catch (NegativeInputException e) {
            fail("NegativeInputException should not have been thrown");
        } catch (EmptyStringException e) {
            fail("EmptyStringException should not have been thrown");
        }
    }

    @Test
    void testIsSurplusZeroCase() {
        try {
            testExpense = new Expense(DESCRIPTION_1, AMOUNT_1, date2);
            testIncome = new Income(DESCRIPTION_2, AMOUNT_1, date1);
            addEntries(testExpense, testIncome);
            assertFalse(testBudget.isSurplus());
        } catch (NegativeInputException e) {
            fail("NegativeInputException should not have been thrown");
        } catch (EmptyStringException e) {
            fail("EmptyStringException should not have been thrown");
        }
    }

    @Test
    void testIsDeficit() {
        try {
            testExpense = new Expense(DESCRIPTION_1, AMOUNT_1, date1);
            testIncome = new Income(DESCRIPTION_2, AMOUNT_2, date2);
            addEntries(testExpense, testIncome);
            assertTrue(testBudget.isDeficit());
        } catch (NegativeInputException e) {
            fail("NegativeInputException should not have been thrown");
        } catch (EmptyStringException e) {
            fail("EmptyStringException should not have been thrown");
        }
    }

    @Test
    void testIsDeficitZeroCase() {
        try {
            testExpense = new Expense(DESCRIPTION_1, AMOUNT_1, date2);
            testIncome = new Income(DESCRIPTION_2, AMOUNT_1, date1);
            addEntries(testExpense, testIncome);
            assertFalse(testBudget.isDeficit());
        } catch (NegativeInputException e) {
            fail("NegativeInputException should not have been thrown");
        } catch (EmptyStringException e) {
            fail("EmptyStringException should not have been thrown");
        }
    }

    @Test
    void testGetters() {
        try {
            testExpense = new Expense(DESCRIPTION_2, AMOUNT_2, date2);
            testIncome = new Income(DESCRIPTION_1, AMOUNT_1, date1);
            addEntries(testExpense, testIncome);
            Report otherExpenseReport = testBudget.getExpenseReport();
            Report otherIncomeReport = testBudget.getIncomeReport();
            assertEquals(1, otherExpenseReport.size());
            assertEquals(1, otherIncomeReport.size());
            assertTrue(otherExpenseReport.contains(testExpense));
            assertTrue(otherIncomeReport.contains(testIncome));
        } catch (NegativeInputException e) {
            fail("NegativeInputException should not have been thrown");
        } catch (EmptyStringException e) {
            fail("EmptyStringException should not have been thrown");
        }
    }

    // MODIFIES: this
    // EFFECTS: adds expense e and income i to the testBudget
    void addEntries(Expense e, Income i) {
        testBudget.addExpense(e);
        testBudget.addIncome(i);
    }
}
