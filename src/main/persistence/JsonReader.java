package persistence;

import model.Budget;
import model.Expense;
import model.Income;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.stream.Stream;

// Represents a reader that reads budget from JSON data stored in file
// This code is modeled on the JsonReader class from https://github.com/stleary/JSON-java
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads budget from file and returns it;
    // throws IOException if an error occurs while reading data from file
    public Budget read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseBudget(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it;
    // throws IOException if an error occurs while reading data from source file
    public String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source),StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }
        return contentBuilder.toString();
    }

    // EFFECTS: parses budget from JSON object and returns it
    public Budget parseBudget(JSONObject jsonObject) {
        Budget budget = new Budget();
        addExpenseReport(budget, jsonObject);
        addIncomeReport(budget, jsonObject);
        return budget;
    }

    // MODIFIES: budget
    // EFFECTS: parses expenseReport from JSON object and adds it to budget
    public void addExpenseReport(Budget budget, JSONObject jsonObject) {
        JSONObject jsonReport = jsonObject.getJSONObject("expenseReport");
        JSONArray jsonArray = jsonReport.getJSONArray("entries");

        for (Object json: jsonArray) {
            JSONObject nextExpense = (JSONObject) json;
            addExpense(budget, nextExpense);
        }
    }

    // MODIFIES: budget
    // EFFECTS: parses incomeReport from JSON object and adds it to budget
    public void addIncomeReport(Budget budget, JSONObject jsonObject) {
        JSONObject jsonReport = jsonObject.getJSONObject("incomeReport");
        JSONArray jsonArray = jsonReport.getJSONArray("entries");

        for (Object json: jsonArray) {
            JSONObject nextIncome = (JSONObject) json;
            addIncome(budget, nextIncome);
        }
    }

    // MODIFIES: budget
    // EFFECTS: parses expense from JSON object and adds it to budget
    public void addExpense(Budget budget, JSONObject jsonObject) {
        String description = jsonObject.getString("description");
        double amount = jsonObject.getDouble("amount");
        Calendar date = getDate(jsonObject);

        Expense expense = new Expense(description, amount, date);
        budget.addExpense(expense);
    }

    // MODIFIES: budget
    // EFFECTS: parses income from JSON object and adds it to budget
    public void addIncome(Budget budget, JSONObject jsonObject) {
        String description = jsonObject.getString("description");
        double amount = jsonObject.getDouble("amount");
        Calendar date = getDate(jsonObject);

        Income income = new Income(description, amount, date);
        budget.addIncome(income);
    }

    // EFFECTS: parses date from JSON object and returns it
    private Calendar getDate(JSONObject jsonObject) {
        JSONObject jsonDate = jsonObject.getJSONObject("date");

        int year = jsonDate.getInt("year");
        int month = jsonDate.getInt("month");
        int day = jsonDate.getInt("day");

        Calendar date = new GregorianCalendar(year, month, day);

        return date;
    }
}
