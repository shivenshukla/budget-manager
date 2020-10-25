package persistence;

import model.Budget;
import model.Entry;
import model.Report;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// I have modeled this code on the JsonReaderTest class from https://github.com/stleary/JSON-java
public class JsonReaderTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            Budget testBudget = reader.read();
            fail("IOException was not thrown");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    void testReaderEmptyBudget() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyBudget.json");
        try {
            Budget testBudget = reader.read();
            assertTrue(testBudget.getExpenseReport().isEmpty());
            assertTrue(testBudget.getIncomeReport().isEmpty());
        } catch (IOException e) {
            fail("Could not read from file");
        }
    }

    @Test
    void testReaderGeneralBudget() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralBudget.json");
        try {
            Budget testBudget = reader.read();

            Report expenseReport = testBudget.getExpenseReport();
            Report incomeReport = testBudget.getIncomeReport();

            assertEquals(3, expenseReport.size());
            assertEquals(3, incomeReport.size());

            checkExpenses(expenseReport);
            checkIncomes(incomeReport);

        } catch (IOException e) {
            fail("Could not read from file");
        }
    }

    private void checkExpenses(Report expenseReport) {
        List<Entry> expenses = expenseReport.getAllEntries();

        assertEquals("test expense 1", expenses.get(0).getDescription());
        assertEquals(100, expenses.get(0).getAmount());
        assertEquals(2020, expenses.get(0).getDate().get(Calendar.YEAR));
        assertEquals(10, expenses.get(0).getDate().get(Calendar.MONTH));
        assertEquals(25, expenses.get(0).getDate().get(Calendar.DAY_OF_MONTH));

        assertEquals("test expense 2", expenses.get(1).getDescription());
        assertEquals(1234.56, expenses.get(1).getAmount());
        assertEquals(1995, expenses.get(1).getDate().get(Calendar.YEAR));
        assertEquals(2, expenses.get(1).getDate().get(Calendar.MONTH));
        assertEquals(14, expenses.get(1).getDate().get(Calendar.DAY_OF_MONTH));

        assertEquals("test expense 3", expenses.get(2).getDescription());
        assertEquals(1.50, expenses.get(2).getAmount());
        assertEquals(2008, expenses.get(2).getDate().get(Calendar.YEAR));
        assertEquals(5, expenses.get(2).getDate().get(Calendar.MONTH));
        assertEquals(30, expenses.get(2).getDate().get(Calendar.DAY_OF_MONTH));
    }

    private void checkIncomes(Report incomeReport) {
        List<Entry> incomes = incomeReport.getAllEntries();

        assertEquals("test income 1", incomes.get(0).getDescription());
        assertEquals(14100, incomes.get(0).getAmount());
        assertEquals(2019, incomes.get(0).getDate().get(Calendar.YEAR));
        assertEquals(8, incomes.get(0).getDate().get(Calendar.MONTH));
        assertEquals(29, incomes.get(0).getDate().get(Calendar.DAY_OF_MONTH));

        assertEquals("test income 2", incomes.get(1).getDescription());
        assertEquals(2000.00, incomes.get(1).getAmount());
        assertEquals(2020, incomes.get(1).getDate().get(Calendar.YEAR));
        assertEquals(2, incomes.get(1).getDate().get(Calendar.MONTH));
        assertEquals(1, incomes.get(1).getDate().get(Calendar.DAY_OF_MONTH));

        assertEquals("test income 3", incomes.get(2).getDescription());
        assertEquals(340.00, incomes.get(2).getAmount());
        assertEquals(2001, incomes.get(2).getDate().get(Calendar.YEAR));
        assertEquals(10, incomes.get(2).getDate().get(Calendar.MONTH));
        assertEquals(20, incomes.get(2).getDate().get(Calendar.DAY_OF_MONTH));
    }
}
