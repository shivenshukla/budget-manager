package persistence;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// This code is modeled on the JsonWriterTest class from https://github.com/stleary/JSON-java

// Tests for the JsonWriter class
public class JsonWriterTest extends JsonTest {
    private Budget testBudget;
    private JsonWriter writer;
    private JsonReader reader;

    @BeforeEach
    void runBefore() {
        testBudget = new Budget();
    }

    @Test
    void testWriterInvalidFile() {
        try {
            writer = new JsonWriter("./data/my\0invalid:fileName.json");
            writer.open();
            fail("FileNotFoundException was not thrown");
        } catch (FileNotFoundException e) {
            // expected
        }
    }

    @Test
    void testWriterEmptyBudget() {
        try {
            writer = new JsonWriter("./data/testWriterEmptyBudget.json");
            writer.open();
            writer.write(testBudget);
            writer.close();

            reader = new JsonReader("./data/testWriterEmptyBudget.json");
            testBudget = reader.read();
            assertTrue(testBudget.getExpenseReport().isEmpty());
            assertTrue(testBudget.getIncomeReport().isEmpty());
        } catch (FileNotFoundException e) {
            fail("FileNotFoundException should not have been thrown");
        } catch (IOException e) {
            fail("IOException should not have been thrown");
        }
    }

    @Test
    void testWriterExpenseEmptyIncomeMany() {
        try {
            addIncomes();

            writer = new JsonWriter("./data/testWriterExpenseEmptyIncomeMany.json");
            writer.open();
            writer.write(testBudget);
            writer.close();

            reader = new JsonReader("./data/testWriterExpenseEmptyIncomeMany.json");
            testBudget = reader.read();

            Report expenseReport = testBudget.getExpenseReport();
            Report incomeReport = testBudget.getIncomeReport();

            assertTrue(expenseReport.isEmpty());
            assertEquals(3, incomeReport.size());
            checkIncomes(incomeReport);
        } catch (FileNotFoundException e) {
            fail("FileNotFoundException should not have been thrown");
        } catch (IOException e) {
            fail("IOException should not have been thrown");
        }
    }

    @Test
    void testWriterExpenseManyIncomeEmpty() {
        writer = new JsonWriter("./data/testWriterExpenseManyIncomeEmpty.json");
        try {
            addExpenses();

            writer.open();
            writer.write(testBudget);
            writer.close();

            reader = new JsonReader("./data/testWriterExpenseManyIncomeEmpty.json");
            reader.read();

            Report expenseReport = testBudget.getExpenseReport();
            Report incomeReport = testBudget.getIncomeReport();

            assertTrue(incomeReport.isEmpty());
            assertEquals(3, expenseReport.size());
            checkExpenses(expenseReport);
        } catch (FileNotFoundException e) {
            fail("FileNotFoundException should not have been thrown");
        } catch (IOException e) {
            fail("IOException should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralBudget() {
        try {
            addExpenses();
            addIncomes();

            writer = new JsonWriter("./data/testWriterGeneralBudget.json");
            writer.open();
            writer.write(testBudget);
            writer.close();

            reader = new JsonReader("./data/testWriterGeneralBudget.json");
            testBudget = reader.read();

            Report expenseReport = testBudget.getExpenseReport();
            Report incomeReport = testBudget.getIncomeReport();

            assertEquals(3, expenseReport.size());
            assertEquals(3, incomeReport.size());

            checkExpenses(expenseReport);
            checkIncomes(incomeReport);
        } catch (FileNotFoundException e) {
            fail("FileNotFoundException should not have been thrown");
        } catch (IOException e) {
            fail("IOException should not have been thrown");
        }
    }

    // MODIFIES: this
    // EFFECTS: adds expenses to testBudget
    private void addExpenses() {
        Calendar date1 = new GregorianCalendar(2020, Calendar.NOVEMBER, 25);
        Calendar date2 = new GregorianCalendar(1995, Calendar.MARCH, 14);
        Calendar date3 = new GregorianCalendar(2008, Calendar.JUNE, 30);

        Expense expense1 = new Expense("test expense 1", 100, date1);
        Expense expense2 = new Expense("test expense 2", 1234.56, date2);
        Expense expense3 = new Expense("test expense 3", 1.50, date3);

        testBudget.addExpense(expense1);
        testBudget.addExpense(expense2);
        testBudget.addExpense(expense3);
    }

    // MODIFIES: this
    // EFFECTS: adds incomes to testBudget
    private void addIncomes() {
        Calendar date1 = new GregorianCalendar(2019, Calendar.SEPTEMBER, 29);
        Calendar date2 = new GregorianCalendar(2020, Calendar.MARCH, 1);
        Calendar date3 = new GregorianCalendar(2001, Calendar.NOVEMBER, 20);

        Income income1 = new Income("test income 1", 14100, date1);
        Income income2 = new Income("test income 2", 2000.00, date2);
        Income income3 = new Income("test income 3", 340.00, date3);

        testBudget.addIncome(income1);
        testBudget.addIncome(income2);
        testBudget.addIncome(income3);
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
