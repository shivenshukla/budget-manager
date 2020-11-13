package ui;

import model.*;
import persistence.JsonReader;
import persistence.JsonWriter;
import ui.gui.BarChart;
import ui.gui.EntryTool;

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

    private static final int X = 500;       // initial x pos of application window
    private static final int Y = 200;       // initial y pos of application window
    private static final int WIDTH = 1000;  // width of application window
    private static final int HEIGHT = 700;  // height of application window

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

    private DefaultListModel<Entry> expensesModel;
    private JList<Entry> expenses;
    private DefaultListModel<Entry> incomesModel;
    private JList<Entry> incomes;

    private EntryTool addExpenseTool;
    private EntryTool addIncomeTool;
    private EntryTool modifyExpenseTool;
    private EntryTool modifyIncomeTool;

    private BarChart barChart;
    private JLabel info;
    private JLabel expenseInfo;
    private JLabel incomeInfo;

    // MODIFIES: this
    // EFFECTS: runs the application
    public BudgetRocket() {
        initializeBudgetFields();
        initializePanels();
        initializeMainFrame();
    }

    // MODIFIES: this
    // EFFECTS: constructs a new budget with an empty income and expense report;
    // instantiates a new JsonReader and JsonWriter with the BUDGET_DATA file.
    public void initializeBudgetFields() {
        budget = new Budget();

        expenseReport = budget.getExpenseReport();
        incomeReport = budget.getIncomeReport();

        jsonReader = new JsonReader(BUDGET_DATA);
        jsonWriter = new JsonWriter(BUDGET_DATA);
    }

    /** Main frame **/

    // MODIFIES: this
    // EFFECTS: constructs the main frame
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

    /** Menu Bar **/

    // MODIFIES: this
    // EFFECTS: constructs a menu bar and adds it to the main frame
    public void initializeMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        menuBar.add(getFileMenu());
        menuBar.add(getViewMenu());

        mainFrame.setJMenuBar(menuBar);
    }

    // EFFECTS: constructs the file menu
    private JMenu getFileMenu() {
        JMenu fileMenu = new JMenu("File");

        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem loadItem = new JMenuItem("Load");

        saveItem.addActionListener(new SaveAction());
        loadItem.addActionListener(new LoadAction());

        fileMenu.add(loadItem);
        fileMenu.add(saveItem);

        return fileMenu;
    }

    // EFFECTS: constructs the view menu
    private JMenu getViewMenu() {
        JMenu viewMenu = new JMenu("View");

        JMenuItem homeItem =  new JMenuItem("Home");
        JMenuItem budgetItem = new JMenuItem("Budget");
        JMenuItem expensesItem = new JMenuItem("Expenses");
        JMenuItem incomesItem = new JMenuItem("Income");

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
        initializeExpenseReportPanel();
        initializeIncomeReportPanel();
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
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        title.setForeground(Color.BLACK);

        budgetPanel = new JPanel(new BorderLayout());
        budgetPanel.setBackground(BACKGROUND_COLOR);
        budgetPanel.add(title, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel(new GridLayout(3, 0));
        infoPanel.setBackground(BACKGROUND_COLOR);
        infoPanel.setPreferredSize(new Dimension(WIDTH, 100));

        info = new JLabel();
        info.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
        expenseInfo = new JLabel();
        expenseInfo.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
        incomeInfo = new JLabel();
        incomeInfo.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));

        infoPanel.add(expenseInfo);
        infoPanel.add(incomeInfo);
        infoPanel.add(info);

        budgetPanel.add(infoPanel, BorderLayout.PAGE_END);

        barChart = new BarChart(0, 0);
        budgetPanel.add(barChart);

        budgetPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    // MODIFIES: this
    // EFFECTS: updates the budget panel
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

    // TODO: remove duplication
    // MODIFIES: this
    // EFFECTS: constructs a JPanel for the expense report
    public void initializeExpenseReportPanel() {
        BorderLayout expensesLayout = new BorderLayout(0, 5);

        expensesPanel = new JPanel(expensesLayout);
        expensesPanel.setBackground(BACKGROUND_COLOR);

        JPanel titlePanel = getTitlePanel("Expense Report");
        expensesPanel.add(titlePanel, BorderLayout.NORTH);

        expensesModel = new DefaultListModel<>();
        expenses = new JList<>();
        JScrollPane listScrollPane = getJScrollPane(expenses, expensesModel);
        expensesPanel.add(listScrollPane, BorderLayout.CENTER);

        JPanel buttons = getReportButtons("expense");

        expensesPanel.add(buttons, BorderLayout.PAGE_END);
        expensesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    // MODIFIES: this
    // EFFECTS: constructs a JPanel for the income report
    public void initializeIncomeReportPanel() {
        BorderLayout incomesLayout = new BorderLayout(0, 5);

        incomesPanel = new JPanel(incomesLayout);
        incomesPanel.setBackground(BACKGROUND_COLOR);

        JPanel titlePanel = getTitlePanel("Income Report");
        incomesPanel.add(titlePanel, BorderLayout.NORTH);

        incomesModel = new DefaultListModel<>();
        incomes = new JList<>();
        JScrollPane listScrollPane = getJScrollPane(incomes, incomesModel);
        incomesPanel.add(listScrollPane, BorderLayout.CENTER);

        JPanel buttons = getReportButtons("income");

        incomesPanel.add(buttons, BorderLayout.PAGE_END);
        incomesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    // TODO: add specification
    private JScrollPane getJScrollPane(JList<Entry> entries, DefaultListModel<Entry> entryModel) {
        entries.setModel(entryModel);
        entries.setFont(new Font(Font.MONOSPACED, Font.BOLD, 15));

        entries.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        entries.setLayoutOrientation(JList.VERTICAL);
        entries.setVisibleRowCount(-1);

        JScrollPane listScrollPane = new JScrollPane(entries);
        listScrollPane.setPreferredSize(new Dimension(100, 100));
        return listScrollPane;
    }

    // EFFECTS: constructs a JPanel with a title and subtitle for a report panel
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

    // EFFECTS: constructs a JPanel with an add, modify, and delete button for a report panel
    private JPanel getReportButtons(String actionCommand) {
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

    // MODIFIES: this
    // EFFECTS: constructs a JPanel for adding expenses to the expense report
    public void initializeAddExpensePanel() {
        addExpenseTool = new EntryTool();
        addExpensePanel = new JPanel(new BorderLayout());
        addExpensePanel.setBackground(BACKGROUND_COLOR);
        addExpensePanel.add(initializeEntryPanel(addExpenseTool), BorderLayout.CENTER);
        addExpensePanel.add(getAddExpenseButtons(), BorderLayout.PAGE_END);
        addExpensePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    // MODIFIES: this
    // EFFECTS: constructs a JPanel for adding incomes to the income report
    public void initializeAddIncomePanel() {
        addIncomeTool = new EntryTool();
        addIncomePanel = new JPanel(new BorderLayout());
        addIncomePanel.setBackground(BACKGROUND_COLOR);
        addIncomePanel.add(initializeEntryPanel(addIncomeTool), BorderLayout.CENTER);
        addIncomePanel.add(getAddIncomeButtons(), BorderLayout.PAGE_END);
        addIncomePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    // EFFECTS: constructs a JPanel for adding entries to a report
    private JPanel initializeEntryPanel(EntryTool addEntryTool) {
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

    // EFFECTS: constructs a JPanel with a confirm and cancel button for the add expense panel
    public JPanel getAddExpenseButtons() {
        JPanel buttons = new JPanel(new GridLayout(0, 2, 10, 0));
        buttons.setBackground(BACKGROUND_COLOR);

        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(new ConfirmAddExpense());

        JButton cancelButton = getCancelButton("openExpenses");

        confirmButton.setBackground(Color.WHITE);

        buttons.add(confirmButton);
        buttons.add(cancelButton);

        return buttons;
    }

    // EFFECTS: constructs a JPanel with a confirm and cancel button for the add income panel
    private JPanel getAddIncomeButtons() {
        JPanel buttons = new JPanel(new GridLayout(0, 2, 10, 0));
        buttons.setBackground(BACKGROUND_COLOR);

        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(new ConfirmAddIncome());

        JButton cancelButton = getCancelButton("openIncomes");

        confirmButton.setBackground(Color.WHITE);

        buttons.add(confirmButton);
        buttons.add(cancelButton);

        return buttons;
    }

    // EFFECTS: returns
    private JButton getCancelButton(String actionCommand) {
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand(actionCommand);
        cancelButton.addActionListener(new OpenAction());
        cancelButton.setBackground(Color.WHITE);
        return cancelButton;
    }

    // MODIFIES: this
    // EFFECTS: constructs a JPanel for modifying an expense in the expense report
    public void initializeModifyExpensePanel() {
        modifyExpenseTool = new EntryTool();
        modifyExpensePanel = new JPanel(new BorderLayout());
        modifyExpensePanel.setBackground(BACKGROUND_COLOR);
        modifyExpensePanel.add(initializeEntryPanel(modifyExpenseTool), BorderLayout.CENTER);
        modifyExpensePanel.add(getModifyExpenseButtons(), BorderLayout.PAGE_END);
        modifyExpensePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    // MODIFIES: this
    // EFFECTS: constructs a JPanel for modifying an income in the income report
    public void initializeModifyIncomePanel() {
        modifyIncomeTool = new EntryTool();
        modifyIncomePanel = new JPanel(new BorderLayout());
        modifyIncomePanel.setBackground(BACKGROUND_COLOR);
        modifyIncomePanel.add(initializeEntryPanel(modifyIncomeTool), BorderLayout.CENTER);
        modifyIncomePanel.add(getModifyIncomeButtons(), BorderLayout.PAGE_END);
        modifyIncomePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    // EFFECTS: constructs a JPanel with a confirm and cancel button for the modify expense panel
    private JPanel getModifyExpenseButtons() {
        JPanel buttons = new JPanel(new GridLayout(0, 2, 10, 0));
        buttons.setBackground(BACKGROUND_COLOR);

        JButton confirmButton = getConfirmModifyButton("expense");
        JButton cancelButton = getCancelButton("openExpenses");

        buttons.add(confirmButton);
        buttons.add(cancelButton);

        return buttons;
    }

    // EFFECTS: constructs a JPanel with a confirm and cancel button for the modify income panel
    private JPanel getModifyIncomeButtons() {
        JPanel buttons = new JPanel(new GridLayout(0, 2, 10, 0));
        buttons.setBackground(BACKGROUND_COLOR);

        JButton confirmButton = getConfirmModifyButton("income");
        JButton cancelButton = getCancelButton("openIncomes");

        buttons.add(confirmButton);
        buttons.add(cancelButton);

        return buttons;
    }

    // EFFECTS: constructs a JButton for modifying an entry
    private JButton getConfirmModifyButton(String actionCommand) {
        JButton confirmButton = new JButton("Confirm");
        confirmButton.setActionCommand(actionCommand);
        confirmButton.addActionListener(new ConfirmModifyAction());
        confirmButton.setBackground(Color.WHITE);
        return confirmButton;
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

    /** ActionListener for navigation **/

    // Represents the action listener for all menu items in the menu bar
    public class OpenAction implements ActionListener {

        // MODIFIES: this
        // EFFECTS: changes the current panel in the content pane
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

    // TODO: remove duplication
    // Represents the action listener for the confirm button in the add expense panel
    public class ConfirmAddExpense implements ActionListener {

        // MODIFIES: this
        // EFFECTS: adds an expense to the expense report
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

    // Represents the action listener for the confirm button in the add income panel
    public class ConfirmAddIncome implements ActionListener {

        // MODIFIES: this
        // EFFECTS: adds an income to the income report
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

    // Represents the action listener for the delete button
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

        // MODIFIES: entries, entryModel, report
        // EFFECTS: helper method for deleting entries from a report; outputs a error message if no entry
        // is selected
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
    }

    // Represents the action listener for the confirm button in a modify entry panel
    public class ConfirmModifyAction implements ActionListener {

        // MODIFIES: this
        // EFFECTS: modifies an existing entry in the report
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

        // MODIFIES: modifyTool, entries, report
        // EFFECTS: helper method for modifying entries in the report
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
    }

    // TODO: add specification
    // Represents the action listener for the modify button in a report panel
    public class ModifyAction implements ActionListener {

        // MODIFIES: this
        // EFFECTS:
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("expense")) {
                setModifyPanel(expenses, modifyExpenseTool, expenseReport, modifyExpensePanel);
            } else {
                setModifyPanel(incomes, modifyIncomeTool, incomeReport, modifyIncomePanel);
            }
        }

        private void setModifyPanel(JList<Entry> entries, EntryTool modifyTool, Report report, JPanel modifyPanel) {
            int index = entries.getSelectedIndex();

            if (index != -1) {
                modifyTool.setAll(report.getAllEntries().get(index));
                changePanel(modifyPanel);
            } else {
                JOptionPane.showMessageDialog(mainFrame, "No entry is selected!", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /** ActionListeners for Data Persistence **/

    // Represents the action listener for the save menu item in the file menu
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


    // Represents the action listener for the load menu item in the file menu
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
                updateBudgetPanel();
                JOptionPane.showMessageDialog(mainFrame, "Budget successfully loaded from " + BUDGET_DATA);
            } catch (IOException exception) {
                JOptionPane.showMessageDialog(mainFrame, "Unable to load budget from " + BUDGET_DATA,
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}