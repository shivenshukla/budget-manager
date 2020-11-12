package ui;

import model.*;
import persistence.JsonReader;
import persistence.JsonWriter;
import ui.tools.BarChart;
import ui.tools.EntryTool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.GregorianCalendar;

import static java.lang.Math.abs;

// Represents the GUI of the budget application
public class BudgetRocket extends JFrame {
    private static final String BUDGET_DATA = "./data/budget.json";
    private static final Color BACKGROUND_COLOR = Color.LIGHT_GRAY;

    private static final int X = 500;      // initial x pos of application window
    private static final int Y = 200;      // initial y pos of application window
    private static final int WIDTH = 1000; // initial width of application window
    private static final int HEIGHT = 700; // initial height of application window

    private Budget budget;
    private Report expenseReport;
    private Report incomeReport;

    private JsonReader jsonReader;
    private JsonWriter jsonWriter;

    private JFrame mainFrame;
    private JPanel mainPanel;
    private JPanel budgetPanel;
    private JPanel expensesPanel;
    private JPanel incomesPanel;
    private JPanel addExpensePanel;
    private JPanel addIncomePanel;
    private JPanel modifyExpensePanel;
    private JPanel modifyIncomePanel;

    private BarChart barChart;

    private DefaultListModel<Entry> expensesModel;
    private JList<Entry> expenses;
    private DefaultListModel<Entry> incomesModel;
    private JList<Entry> incomes;

    private EntryTool addExpenseTool;
    private EntryTool addIncomeTool;
    private EntryTool modifyExpenseTool;
    private EntryTool modifyIncomeTool;

    private JLabel info;
    private JLabel expenseInfo;
    private JLabel incomeInfo;

    // MODIFIES: this
    // EFFECTS: runs the application
    public BudgetRocket() {
        initializeFields();
        initializePanels();
        initializeMainFrame();
    }

    // MODIFIES: this
    // EFFECTS: constructs a new budget with an empty income and expense report.
    // Instantiates a new JsonReader and JsonWriter with the BUDGET_DATA file.
    public void initializeFields() {
        budget = new Budget();
        expenseReport = budget.getExpenseReport();
        incomeReport = budget.getIncomeReport();

        jsonReader = new JsonReader(BUDGET_DATA);
        jsonWriter = new JsonWriter(BUDGET_DATA);

        info =  new JLabel();
    }

    /** Main frame + Menu Bar **/

    // MODIFIES: this
    // EFFECTS: constructs the main JFrame of the application
    public void initializeMainFrame() {
        mainFrame = new JFrame("Budget Rocket");
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setLocation(X, Y);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        mainFrame.setResizable(false);

        mainFrame.getContentPane().add(mainPanel);
        initializeMenuBar();

        mainFrame.pack();
        mainFrame.setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: constructs the JMenuBar and adds it to the main JFrame
    public void initializeMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        menuBar.add(setFileMenu());
        menuBar.add(setViewMenu());

        mainFrame.setJMenuBar(menuBar);
    }

    private JMenu setFileMenu() {
        JMenu fileMenu = new JMenu("File");

        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem loadItem = new JMenuItem("Load");

        saveItem.addActionListener(new SaveAction());
        loadItem.addActionListener(new LoadAction());

        fileMenu.add(loadItem);
        fileMenu.add(saveItem);

        return fileMenu;
    }

    private JMenu setViewMenu() {
        JMenu viewMenu = new JMenu("View");

        JMenuItem homeItem =  new JMenuItem("Home");
        JMenuItem budgetItem = new JMenuItem("Budget");
        JMenuItem expensesItem = new JMenuItem("Expenses");
        JMenuItem incomesItem = new JMenuItem("Incomes");

        homeItem.setActionCommand("openHome");
        homeItem.addActionListener(new OpenAction());

        budgetItem.setActionCommand("openBudget");
        budgetItem.addActionListener(new OpenAction());

        expensesItem.setActionCommand("openExpenses");
        expensesItem.addActionListener(new OpenAction());

        incomesItem.setActionCommand("openIncomes");
        incomesItem.addActionListener(new OpenAction());

        viewMenu.add(homeItem);
        viewMenu.add(budgetItem);
        viewMenu.add(expensesItem);
        viewMenu.add(incomesItem);

        return viewMenu;
    }

    /** Panels **/

    // MODIFIES: this
    // EFFECTS: constructs all relevant JPanels
    public void initializePanels() {
        initializeMainPanel();
        initializeBudgetPanel();
        initializeExpensesPanel();
        initializeIncomesPanel();
        initializeAddExpensePanel();
        initializeAddIncomePanel();
        initializeModifyExpensePanel();
        initializeModifyIncomePanel();
    }

    // MODIFIES: this
    // EFFECTS: constructs a JPanel which will serve as the home page of the application
    public void initializeMainPanel() {
        mainPanel = new JPanel();
        mainPanel.setBackground(Color.RED);

        Icon mainPhoto = new ImageIcon("./data/budgetRocketHome.jpg");
        JLabel photo = new JLabel(mainPhoto);
        mainPanel.add(photo);
    }

    // MODIFIES: this
    // EFFECTS: constructs a JPanel for the budget report
    public void initializeBudgetPanel() {
        JLabel title = new JLabel("Budget Report");
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
        title.setForeground(Color.BLACK);

        budgetPanel = new JPanel(new BorderLayout());
        budgetPanel.setBackground(BACKGROUND_COLOR);
        budgetPanel.add(title, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel(new GridLayout(3, 0));
        infoPanel.setBackground(BACKGROUND_COLOR);
        infoPanel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
        infoPanel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
        infoPanel.setForeground(Color.BLACK);
        infoPanel.setPreferredSize(new Dimension(WIDTH, 100));

        expenseInfo = new JLabel(String.format("Total expenses: $%.2f", expenseReport.sum()));
        incomeInfo = new JLabel(String.format("Total income: $%.2f", incomeReport.sum()));

        infoPanel.add(expenseInfo);
        infoPanel.add(incomeInfo);
        infoPanel.add(info);

        budgetPanel.add(infoPanel, BorderLayout.PAGE_END);

        barChart = new BarChart(0, 0);
        budgetPanel.add(barChart);

        budgetPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    public void updateBudgetPanel() {
        double budgetTotal = budget.getDifference();
        String budgetString = String.format("%.2f", budgetTotal);

        if (budget.isSurplus()) {
            info.setText("You have a surplus of $" + budgetString);
        } else if (budget.isDeficit()) {
            budgetString = String.format("%.2f", abs(budgetTotal));
            info.setText("You have a deficit of $" + budgetString);
        } else {
            info.setText("You have a balance of $" + budgetString);
        }

        expenseInfo.setText(String.format("Total expenses: $%.2f", expenseReport.sum()));
        incomeInfo.setText(String.format("Total income: $%.2f", incomeReport.sum()));

        budgetPanel.remove(barChart);
        barChart = new BarChart(expenseReport.sum(), incomeReport.sum());
        budgetPanel.add(barChart);
    }

    // MODIFIES: this
    // EFFECTS: constructs a JPanel for the expense report
    public void initializeExpensesPanel() {
        BorderLayout expensesLayout = new BorderLayout(0, 5);

        expensesPanel = new JPanel(expensesLayout);
        expensesPanel.setBackground(BACKGROUND_COLOR);

        JPanel titlePanel = getTitlePanel("Expense Report");
        expensesPanel.add(titlePanel, BorderLayout.NORTH);

        expensesModel = new DefaultListModel<>();
        expenses = new JList<>();
        expenses.setModel(expensesModel);
        expenses.setFont(new Font(Font.MONOSPACED, Font.BOLD, 15));

        expenses.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        expenses.setLayoutOrientation(JList.VERTICAL);
        expenses.setVisibleRowCount(-1);

        JScrollPane listScrollPane = new JScrollPane(expenses);
        listScrollPane.setPreferredSize(new Dimension(100, 100));
        expensesPanel.add(listScrollPane, BorderLayout.CENTER);

        JPanel buttons = setReportButtons("expense");

        expensesPanel.add(buttons, BorderLayout.PAGE_END);
        expensesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    // MODIFIES: this
    // EFFECTS: constructs a JPanel for the income report
    public void initializeIncomesPanel() {
        BorderLayout incomesLayout = new BorderLayout(0, 5);

        incomesPanel = new JPanel(incomesLayout);
        incomesPanel.setBackground(BACKGROUND_COLOR);

        JPanel titlePanel = getTitlePanel("Income Report");
        incomesPanel.add(titlePanel, BorderLayout.NORTH);

        incomesModel = new DefaultListModel<>();
        incomes = new JList<>();
        incomes.setModel(incomesModel);
        incomes.setFont(new Font(Font.MONOSPACED, Font.BOLD, 15));

        incomes.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        incomes.setLayoutOrientation(JList.VERTICAL);
        incomes.setVisibleRowCount(-1);

        JScrollPane listScrollPane = new JScrollPane(incomes);
        listScrollPane.setPreferredSize(new Dimension(100, 100));
        incomesPanel.add(listScrollPane, BorderLayout.CENTER);

        JPanel buttons = setReportButtons("income");

        incomesPanel.add(buttons, BorderLayout.PAGE_END);
        incomesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    private JPanel getTitlePanel(String title) {
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        titleLabel.setForeground(Color.BLACK);

        JPanel titlePanel = new JPanel(new BorderLayout(0, 2));
        titlePanel.add(titleLabel, BorderLayout.PAGE_START);

        String dateHeader = String.format("%-30s", "Date");
        String amountHeader = String.format("%-31s", "Amount");

        JLabel header = new JLabel(dateHeader + amountHeader + "Description");
        header.setFont(new Font(Font.MONOSPACED, Font.BOLD, 15));

        titlePanel.setBackground(BACKGROUND_COLOR);
        titlePanel.add(header, BorderLayout.PAGE_END);
        return titlePanel;
    }

    private JPanel setReportButtons(String actionCommand) {
        JPanel buttons = new JPanel(new GridLayout(0, 3, 2, 0));
        buttons.setBackground(BACKGROUND_COLOR);

        JButton addButton = new JButton("Add");
        addButton.setActionCommand(actionCommand);
        addButton.addActionListener(new AddAction());

        JButton modifyButton = new JButton("Modify");
        modifyButton.setActionCommand(actionCommand);
        modifyButton.addActionListener(new ModifyAction());

        JButton deleteButton = new JButton("Delete");
        deleteButton.setActionCommand(actionCommand);
        deleteButton.addActionListener(new DeleteAction());

        addButton.setBackground(Color.WHITE);
        modifyButton.setBackground(Color.WHITE);
        deleteButton.setBackground(Color.WHITE);

        buttons.add(addButton);
        buttons.add(modifyButton);
        buttons.add(deleteButton);

        return buttons;
    }

    public void initializeAddExpensePanel() {
        addExpenseTool = new EntryTool();
        addExpensePanel = new JPanel(new BorderLayout());
        addExpensePanel.setBackground(BACKGROUND_COLOR);
        addExpensePanel.add(initializeEntryPanel(addExpenseTool), BorderLayout.CENTER);
        addExpensePanel.add(setAddExpenseButtons(), BorderLayout.PAGE_END);
        addExpensePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    public void initializeAddIncomePanel() {
        addIncomeTool = new EntryTool();
        addIncomePanel = new JPanel(new BorderLayout());
        addIncomePanel.setBackground(BACKGROUND_COLOR);
        addIncomePanel.add(initializeEntryPanel(addIncomeTool), BorderLayout.CENTER);
        addIncomePanel.add(setAddIncomeButtons(), BorderLayout.PAGE_END);
        addIncomePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    // MODIFIES: this
    // EFFECTS:
    public JPanel initializeEntryPanel(EntryTool addEntryTool) {
        JPanel addEntryPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        addEntryPanel.setBackground(BACKGROUND_COLOR);

        addEntryTool.getAmountField().setValue(0);
        addEntryTool.getDescriptionField().setText("Enter here");

        JLabel dayLabel = new JLabel("Day", JLabel.CENTER);
        JLabel monthLabel = new JLabel("Month", JLabel.CENTER);
        JLabel yearLabel = new JLabel("Year", JLabel.CENTER);
        JLabel amountLabel = new JLabel("Amount ($)", JLabel.CENTER);
        JLabel descriptionLabel = new JLabel("Description", JLabel.CENTER);

        addEntryPanel.add(dayLabel);
        addEntryPanel.add(addEntryTool.getDaySpinner());

        addEntryPanel.add(monthLabel);
        addEntryPanel.add(addEntryTool.getMonthSpinner());

        addEntryPanel.add(yearLabel);
        addEntryPanel.add(addEntryTool.getYearSpinner());

        addEntryPanel.add(amountLabel);
        addEntryPanel.add(addEntryTool.getAmountField());

        addEntryPanel.add(descriptionLabel);
        addEntryPanel.add(addEntryTool.getDescriptionField());

        addEntryPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        return addEntryPanel;
    }

    public JPanel setAddExpenseButtons() {
        JPanel buttons = new JPanel(new GridLayout(0, 2, 10, 0));
        buttons.setBackground(BACKGROUND_COLOR);

        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(new ConfirmAddExpense());

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand("openExpenses");
        cancelButton.addActionListener(new OpenAction());

        confirmButton.setBackground(Color.WHITE);
        cancelButton.setBackground(Color.WHITE);

        buttons.add(confirmButton);
        buttons.add(cancelButton);

        return buttons;
    }

    public JPanel setAddIncomeButtons() {
        JPanel buttons = new JPanel(new GridLayout(0, 2, 10, 0));
        buttons.setBackground(BACKGROUND_COLOR);

        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(new ConfirmAddIncome());

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand("openIncomes");
        cancelButton.addActionListener(new OpenAction());

        confirmButton.setBackground(Color.WHITE);
        cancelButton.setBackground(Color.WHITE);

        buttons.add(confirmButton);
        buttons.add(cancelButton);

        return buttons;
    }

    // MODIFIES: this
    // EFFECTS:
    public void initializeModifyExpensePanel() {
        modifyExpenseTool = new EntryTool();
        modifyExpensePanel = new JPanel(new BorderLayout());
        modifyExpensePanel.setBackground(BACKGROUND_COLOR);
        modifyExpensePanel.add(initializeEntryPanel(modifyExpenseTool), BorderLayout.CENTER);
        modifyExpensePanel.add(setModifyExpenseButtons(), BorderLayout.PAGE_END);
        modifyExpensePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    // MODIFIES: this
    // EFFECTS:
    public void initializeModifyIncomePanel() {
        modifyIncomeTool = new EntryTool();
        modifyIncomePanel = new JPanel(new BorderLayout());
        modifyIncomePanel.setBackground(BACKGROUND_COLOR);
        modifyIncomePanel.add(initializeEntryPanel(modifyIncomeTool), BorderLayout.CENTER);
        modifyIncomePanel.add(setModifyIncomeButtons(), BorderLayout.PAGE_END);
        modifyIncomePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    public JPanel setModifyExpenseButtons() {
        JPanel buttons = new JPanel(new GridLayout(0, 2, 10, 0));
        buttons.setBackground(BACKGROUND_COLOR);

        JButton confirmButton = new JButton("Confirm");
        confirmButton.setActionCommand("expense");
        confirmButton.addActionListener(new ConfirmModifyAction());

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand("openExpenses");
        cancelButton.addActionListener(new OpenAction());

        confirmButton.setBackground(Color.WHITE);
        cancelButton.setBackground(Color.WHITE);

        buttons.add(confirmButton);
        buttons.add(cancelButton);

        return buttons;
    }

    public JPanel setModifyIncomeButtons() {
        JPanel buttons = new JPanel(new GridLayout(0, 2, 10, 0));
        buttons.setBackground(BACKGROUND_COLOR);

        JButton confirmButton = new JButton("Confirm");
        confirmButton.setActionCommand("income");
        confirmButton.addActionListener(new ConfirmModifyAction());

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand("openIncomes");
        cancelButton.addActionListener(new OpenAction());

        confirmButton.setBackground(Color.WHITE);
        cancelButton.setBackground(Color.WHITE);

        buttons.add(confirmButton);
        buttons.add(cancelButton);

        return buttons;
    }

    // MODIFIES: this
    // EFFECTS: changes the current panel on the content pane to the given panel
    public void changePanel(JPanel panel) {
        mainFrame.getContentPane().removeAll();
        mainFrame.getContentPane().add(panel);
        mainFrame.getContentPane().validate();
        mainFrame.getContentPane().repaint();
    }

    // MODIFIES: this
    // EFFECTS: updates the budget
    public void updateBudget() {
        expensesModel.clear();
        incomesModel.clear();

        for (Entry e: expenseReport.getAllEntries()) {
            expensesModel.addElement(e);
        }

        for (Entry i: incomeReport.getAllEntries()) {
            incomesModel.addElement(i);
        }
    }

    /** ActionListeners for navigation **/

    public class OpenAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (e.getActionCommand()) {
                case "openHome":
                    changePanel(mainPanel);
                    break;
                case "openBudget":
                    updateBudgetPanel();
                    changePanel(budgetPanel);
                    break;
                case "openExpenses":
                    changePanel(expensesPanel);
                    break;
                case "openIncomes":
                    changePanel(incomesPanel);
                    break;
            }
        }
    }

    /** ActionListeners for entry operations **/

    public class AddAction implements ActionListener {

        // MODIFIES: this
        // EFFECTS: adds a new entry to the report
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("expense")) {
                addExpenseTool.resetAll();
                changePanel(addExpensePanel);
            } else {
                addIncomeTool.resetAll();
                changePanel(addIncomePanel);
            }
        }
    }

    public class ConfirmAddExpense implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String description = addExpenseTool.getDescriptionField().getText();
            double amount = ((Number) addExpenseTool.getAmountField().getValue()).doubleValue();
            int month = (int) addExpenseTool.getMonthSpinner().getValue() - 1;
            int day = (int) addExpenseTool.getDaySpinner().getValue();
            int year = (int) addExpenseTool.getYearSpinner().getValue();

            Expense expense = new Expense(description, amount, new GregorianCalendar(year, month, day));
            budget.addExpense(expense);
            expensesModel.addElement(expense);
            changePanel(expensesPanel);
        }
    }

    public class ConfirmAddIncome implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String description = addIncomeTool.getDescriptionField().getText();
            double amount = ((Number) addIncomeTool.getAmountField().getValue()).doubleValue();
            int month = (int) addIncomeTool.getMonthSpinner().getValue() - 1;
            int day = (int) addIncomeTool.getDaySpinner().getValue();
            int year = (int) addIncomeTool.getYearSpinner().getValue();

            Income income = new Income(description, amount, new GregorianCalendar(year, month, day));
            budget.addIncome(income);
            incomesModel.addElement(income);
            changePanel(incomesPanel);
        }
    }

    public class DeleteAction implements ActionListener {

        // MODIFIES: this
        // EFFECTS: deletes an existing entry from the report
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("expense")) {
                deleteEntry(expenses, expensesModel, expenseReport);
            } else {
                deleteEntry(incomes, incomesModel, incomeReport);
            }
        }
    }

    private void deleteEntry(JList<Entry> entries, DefaultListModel<Entry> entryModel, Report report) {
        int index = entries.getSelectedIndex();

        if (index != -1) {
            entryModel.remove(index);
            Entry entryToDelete = report.getAllEntries().get(index);
            report.deleteEntry(entryToDelete);
        } else {
            JOptionPane.showMessageDialog(mainFrame, "No entry is selected!", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public class ConfirmModifyAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("expense")) {
                modifyEntry(modifyExpenseTool, expenses, expenseReport);
                changePanel(expensesPanel);
            } else {
                modifyEntry(modifyIncomeTool, incomes, incomeReport);
                changePanel(incomesPanel);
            }
        }
    }

    private void modifyEntry(EntryTool modifyTool, JList<Entry> entries, Report report) {
        int index = entries.getSelectedIndex();

        String description = modifyTool.getDescriptionField().getText();
        double amount = ((Number) modifyTool.getAmountField().getValue()).doubleValue();
        int month = (int) modifyTool.getMonthSpinner().getValue() - 1;
        int day = (int) modifyTool.getDaySpinner().getValue();
        int year = (int) modifyTool.getYearSpinner().getValue();

        Entry entryToModify = report.getAllEntries().get(index);
        entryToModify.setDescription(description);
        entryToModify.setAmount(amount);
        entryToModify.setDate(year, month, day);
    }


    public class ModifyAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("expense")) {
                int index = expenses.getSelectedIndex();

                if (index != -1) {
                    modifyExpenseTool.setAll(expenseReport.getAllEntries().get(index));
                    changePanel(modifyExpensePanel);
                } else {
                    JOptionPane.showMessageDialog(mainFrame, "No entry is selected!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {
                int index = incomes.getSelectedIndex();

                if (index != -1) {
                    modifyIncomeTool.setAll(incomeReport.getAllEntries().get(index));
                    changePanel(modifyIncomePanel);
                } else {
                    JOptionPane.showMessageDialog(mainFrame, "No entry is selected!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /** ActionListeners for Data Persistence **/

    public class SaveAction implements ActionListener {

        // MODIFIES: this
        // EFFECTS: saves budget to file
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                jsonWriter.open();
                jsonWriter.write(budget);
                jsonWriter.close();
                JOptionPane.showMessageDialog(mainFrame, "Budget successfully saved to " + BUDGET_DATA);
            } catch (FileNotFoundException exception) {
                JOptionPane.showMessageDialog(mainFrame, "Unable to save budget to " + BUDGET_DATA,
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    public class LoadAction implements ActionListener {

        // MODIFIES: this
        // EFFECTS: loads budget from file
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                budget = jsonReader.read();
                expenseReport = budget.getExpenseReport();
                incomeReport = budget.getIncomeReport();
                updateBudget();
                JOptionPane.showMessageDialog(mainFrame, "Budget successfully loaded from " + BUDGET_DATA);
            } catch (IOException exception) {
                JOptionPane.showMessageDialog(mainFrame, "Unable to load budget from " + BUDGET_DATA,
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}