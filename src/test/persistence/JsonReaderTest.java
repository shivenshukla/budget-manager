package persistence;

import model.Budget;
import model.Entry;
import model.Report;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// I modeled this code on the JsonReaderTest class from https://github.com/stleary/JSON-java
public class JsonReaderTest extends JsonTest{
    private JsonReader reader;

    @Test
    void testReaderNonExistentFile() {
        reader = new JsonReader("./data/noSuchFile.json");
        try {
            Budget testBudget = reader.read();
            fail("IOException was not thrown");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    void testReaderEmptyBudget() {
        reader = new JsonReader("./data/testReaderEmptyBudget.json");
        try {
            Budget testBudget = reader.read();
            assertTrue(testBudget.getExpenseReport().isEmpty());
            assertTrue(testBudget.getIncomeReport().isEmpty());
        } catch (IOException e) {
            fail("IOException should not have been thrown");
        }
    }

    @Test
    void testReaderGeneralBudget() {
        reader = new JsonReader("./data/testReaderGeneralBudget.json");
        try {
            Budget testBudget = reader.read();

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

    private void checkExpenses(Report expenseReport) {
        List<Entry> expenses = expenseReport.getAllEntries();

        checkEntry(expenses.get(0), "test expense 1", 100, 2020, 10, 25 );
        checkEntry(expenses.get(1), "test expense 2", 1234.56, 1995, 2,14);
        checkEntry(expenses.get(2), "test expense 3", 1.50, 2008, 5, 30);
    }

    private void checkIncomes(Report incomeReport) {
        List<Entry> incomes = incomeReport.getAllEntries();

        checkEntry(incomes.get(0), "test income 1", 14100, 2019, 8, 29);
        checkEntry(incomes.get(1), "test income 2", 2000.00, 2020, 2, 1);
        checkEntry(incomes.get(2), "test income 3", 340.00, 2001, 10, 20);
    }
}
