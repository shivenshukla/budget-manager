package model;

import org.junit.jupiter.api.BeforeEach;

// Tests for the Expense class
public class ExpenseTest extends EntryTest{

    @BeforeEach
    void runBefore() {
        super.runBefore();
        testEntry = new Expense(DESCRIPTION, AMOUNT, date);
    }
}