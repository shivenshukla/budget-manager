package model;

import org.junit.jupiter.api.BeforeEach;

// Tests for the Income class
public class IncomeTest extends EntryTest{

    @BeforeEach
    void runBefore() {
        super.runBefore();
        testEntry = new Income(DESCRIPTION, AMOUNT, date);
    }
}

