package model;

import org.junit.jupiter.api.BeforeEach;

public class IncomeTest extends EntryTest{

    @BeforeEach
    void runBefore() {
        super.runBefore();
        testEntry = new Income(DESCRIPTION, AMOUNT, date);
    }
}

