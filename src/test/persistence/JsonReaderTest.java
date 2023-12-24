package persistence;

import model.Budget;
import model.Entry;
import model.Report;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Tests for the JsonReader class
public class JsonReaderTest extends JsonTest{
    private Budget testBudget;
    private JsonReader reader;

    @Test
    void testReaderNonExistentFile() {
        reader = new JsonReader("./data/noSuchFile.json");
        try {
            testBudget = reader.read();
            fail("IOException was not thrown");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    void testReaderEmptyBudget() {
        reader = new JsonReader("./data/testReaderEmptyBudget.json");
        try {
            testBudget = reader.read();
            assertTrue(testBudget.getExpenseReport().isEmpty());
            assertTrue(testBudget.getIncomeReport().isEmpty());
        } catch (IOException e) {
            fail("IOException should not have been thrown");
        }
    }

    @Test
    void testReaderExpenseEmptyIncomeMany() {
        reader = new JsonReader("./data/testReaderExpenseEmptyIncomeMany.json");
        try {
            testBudget = reader.read();
            Report expenseReport = testBudget.getExpenseReport();
            Report incomeReport = testBudget.getIncomeReport();
            assertTrue(expenseReport.isEmpty());
            assertEquals(3, incomeReport.size());
            checkIncomes(incomeReport);
        } catch (IOException e) {
            fail("IOException should not have been thrown");
        }
    }

    @Test
    void testReaderExpenseManyIncomeEmpty() {
        reader = new JsonReader("./data/testReaderExpenseManyIncomeEmpty.json");
        try {
            testBudget = reader.read();
            Report expenseReport = testBudget.getExpenseReport();
            Report incomeReport = testBudget.getIncomeReport();
            assertTrue(incomeReport.isEmpty());
            assertEquals(3, expenseReport.size());
            checkExpenses(expenseReport);
        } catch (IOException e) {
            fail("IOException should not have been thrown");
        }
    }

    @Test
    void testReaderGeneralBudget() {
        reader = new JsonReader("./data/testReaderGeneralBudget.json");
        try {
            testBudget = reader.read();
            Report expenseReport = testBudget.getExpenseReport();
            Report incomeReport = testBudget.getIncomeReport();
            assertEquals(3, expenseReport.size());
            assertEquals(3, incomeReport.size());
            checkExpenses(expenseReport);
            checkIncomes(incomeReport);
        } catch (IOException e) {
            fail("IOException should not have been thrown");
        }
    }

    @Test
    void testReaderInvalidEntries() {
        reader = new JsonReader("./data/testReaderInvalidEntries.json");
        try {
            testBudget = reader.read();
            Report expenseReport = testBudget.getExpenseReport();
            Report incomeReport = testBudget.getIncomeReport();
            assertEquals(1, expenseReport.size());
            assertEquals(1, incomeReport.size());
            Entry validExpense = expenseReport.getAllEntries().get(0);
            Entry validIncome = incomeReport.getAllEntries().get(0);
            checkEntry(validExpense, "test expense 3", 1.50, 2008, 5, 30);
            checkEntry(validIncome, "test income 3", 340.00, 2001, 10, 20);
        } catch (IOException e) {
            fail("IOException should not have been thrown");
        }
    }

    // REQUIRES: expenseReport has at least 3 entries
    // EFFECTS: checks whether expenses were read correctly
    private void checkExpenses(Report expenseReport) {
        List<Entry> expenses = expenseReport.getAllEntries();
        checkEntry(expenses.get(0), "test expense 1", 100, 2020, 10, 25 );
        checkEntry(expenses.get(1), "test expense 2", 1234.56, 1995, 2,14);
        checkEntry(expenses.get(2), "test expense 3", 1.50, 2008, 5, 30);
    }

    // REQUIRES: incomeReport has at least 3 entries
    // EFFECTS: checks whether incomes were read correctly
    private void checkIncomes(Report incomeReport) {
        List<Entry> incomes = incomeReport.getAllEntries();
        checkEntry(incomes.get(0), "test income 1", 14100, 2019, 8, 29);
        checkEntry(incomes.get(1), "test income 2", 2000.00, 2020, 2, 1);
        checkEntry(incomes.get(2), "test income 3", 340.00, 2001, 10, 20);
    }
}
