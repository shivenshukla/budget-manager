package model;

import org.junit.jupiter.api.BeforeEach;

// Tests for the ExpenseReport class
public class ExpenseReportTest extends ReportTest{

    @BeforeEach
    void runBefore() {
        super.runBefore();
        testReport = new ExpenseReport();
        entry1 = new Expense(DESCRIPTION_1, AMOUNT_1, date1);
        entry2 = new Expense(DESCRIPTION_2, AMOUNT_2, date2);
        entry3 = new Expense(DESCRIPTION_3, AMOUNT_3, date3);
    }
}
