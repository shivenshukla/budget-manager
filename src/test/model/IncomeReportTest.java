package model;

import org.junit.jupiter.api.BeforeEach;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class IncomeReportTest extends ReportTest {

    @BeforeEach
    void runBefore() {
        super.runBefore();
        testReport = new IncomeReport();
        entry1 = new Income(DESCRIPTION_1, AMOUNT_1, date1);
        entry2 = new Income(DESCRIPTION_2, AMOUNT_2, date2);
        entry3 = new Income(DESCRIPTION_3, AMOUNT_3, date3);
    }
}
