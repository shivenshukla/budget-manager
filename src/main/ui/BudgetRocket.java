package ui;

import model.*;
import persistence.JsonReader;
import persistence.JsonWriter;
import ui.tools.AddEntryTool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.GregorianCalendar;

// Represents the GUI of the budget application
public class BudgetRocket extends JFrame {
    private static final String BUDGET_DATA = "./data/budget.json";

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

    private DefaultListModel<Entry> expensesModel;
    private JList<Entry> expenses;
    private DefaultListModel<Entry> incomesModel;
    private JList<Entry> incomes;

    private AddEntryTool addExpenseTool;
    private AddEntryTool addIncomeTool;
    private AddEntryTool modifyExpenseTool;
    private AddEntryTool modifyIncomeTool;

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

        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem loadItem = new JMenuItem("Load");
        JMenuItem budgetItem = new JMenuItem("Budget");
        JMenuItem expensesItem = new JMenuItem("Expenses");
        JMenuItem incomesItem = new JMenuItem("Incomes");

        saveItem.addActionListener(new SaveBudget());

        loadItem.addActionListener(new LoadBudget());

        budgetItem.addActionListener(new OpenBudget());

        expensesItem.addActionListener(new OpenExpenses());

        incomesItem.addActionListener(new OpenIncomes());

        JMenuItem homeItem =  new JMenuItem("Home");

        homeItem.addActionListener(new ReturnHome());

        JMenu fileMenu = new JMenu("File");
        JMenu viewMenu = new JMenu("View");

        fileMenu.add(loadItem);
        fileMenu.add(saveItem);

        viewMenu.add(homeItem);
        viewMenu.add(budgetItem);
        viewMenu.add(expensesItem);
        viewMenu.add(incomesItem);

        menuBar.add(fileMenu);
        menuBar.add(viewMenu);

        mainFrame.setJMenuBar(menuBar);
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
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 50));
        title.setForeground(Color.WHITE);

        budgetPanel = new JPanel();
        budgetPanel.setBackground(Color.GRAY);
        budgetPanel.add(title, BorderLayout.NORTH);
    }


    // MODIFIES: this
    // EFFECTS: constructs a JPanel for the expense report
    public void initializeExpensesPanel() {
        JLabel title = new JLabel("Expense Report");
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
        title.setForeground(Color.WHITE);

        BorderLayout expensesLayout = new BorderLayout();
        expensesLayout.setVgap(5);

        expensesPanel = new JPanel(expensesLayout);
        expensesPanel.setBackground(Color.GRAY);
        expensesPanel.add(title, BorderLayout.NORTH);

        expensesModel = new DefaultListModel<>();
        expenses = new JList<>();
        expenses.setModel(expensesModel);

        expenses.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        expenses.setLayoutOrientation(JList.VERTICAL);
        expenses.setVisibleRowCount(-1);

        JScrollPane listScrollPane = new JScrollPane(expenses);
        listScrollPane.setPreferredSize(new Dimension(100, 100));
        expensesPanel.add(listScrollPane, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new GridLayout(0, 3, 2, 0));

        JButton addButton = new JButton("Add");
        addButton.setActionCommand("expense");
        addButton.addActionListener(new AddEntry());

        JButton modifyButton = new JButton("Modify");
        modifyButton.setActionCommand("expense");
        modifyButton.addActionListener(new ModifyEntry());

        JButton deleteButton = new JButton("Delete");
        deleteButton.setActionCommand("expense");
        deleteButton.addActionListener(new DeleteEntry());

        addButton.setBackground(Color.WHITE);
        modifyButton.setBackground(Color.WHITE);
        deleteButton.setBackground(Color.WHITE);

        buttons.add(addButton);
        buttons.add(modifyButton);
        buttons.add(deleteButton);

        expensesPanel.add(buttons, BorderLayout.PAGE_END);
        expensesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }


    // MODIFIES: this
    // EFFECTS: constructs a JPanel for the income report
    public void initializeIncomesPanel() {
        JLabel title = new JLabel("Income Report");
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
        title.setForeground(Color.BLACK);

        BorderLayout expensesLayout = new BorderLayout();
        expensesLayout.setVgap(5);

        incomesPanel = new JPanel(expensesLayout);
        incomesPanel.setBackground(Color.GRAY);
        incomesPanel.add(title, BorderLayout.NORTH);

        incomesModel = new DefaultListModel<>();
        incomes = new JList<>();
        incomes.setModel(incomesModel);

        incomes.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        incomes.setLayoutOrientation(JList.VERTICAL);
        incomes.setVisibleRowCount(-1);

        JScrollPane listScrollPane = new JScrollPane(incomes);
        listScrollPane.setPreferredSize(new Dimension(100, 100));
        incomesPanel.add(listScrollPane, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new GridLayout(0, 3, 2, 0));

        JButton addButton = new JButton("Add");
        addButton.setActionCommand("income");
        addButton.addActionListener(new AddEntry());

        JButton modifyButton = new JButton("Modify");
        modifyButton.setActionCommand("income");
        modifyButton.addActionListener(new ModifyEntry());

        JButton deleteButton = new JButton("Delete");
        deleteButton.setActionCommand("income");
        deleteButton.addActionListener(new DeleteEntry());

        addButton.setBackground(Color.WHITE);
        modifyButton.setBackground(Color.WHITE);
        deleteButton.setBackground(Color.WHITE);

        buttons.add(addButton);
        buttons.add(modifyButton);
        buttons.add(deleteButton);

        incomesPanel.add(buttons, BorderLayout.PAGE_END);
        incomesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    public void initializeAddExpensePanel() {
        addExpenseTool = new AddEntryTool();
        addExpensePanel = new JPanel(new BorderLayout());
        addExpensePanel.add(initializeAddEntryPanel(addExpenseTool), BorderLayout.CENTER);
        addExpensePanel.add(setAddExpenseButtons(), BorderLayout.PAGE_END);
    }

    public void initializeAddIncomePanel() {
        addIncomeTool = new AddEntryTool();
        addIncomePanel = new JPanel(new BorderLayout());
        addIncomePanel.add(initializeAddEntryPanel(addIncomeTool), BorderLayout.CENTER);
        addIncomePanel.add(setAddIncomeButtons(), BorderLayout.PAGE_END);
    }

    // MODIFIES: this
    // EFFECTS:
    public JPanel initializeAddEntryPanel(AddEntryTool addEntryTool) {
        JPanel addEntryPanel = new JPanel(new GridLayout(5, 2, 10, 10));

        addEntryTool.getAmountField().setValue(0);
        addEntryTool.getDescriptionField().setText("Enter here");

        JLabel dayLabel = new JLabel("Day", JLabel.CENTER);
        JLabel monthLabel = new JLabel("Month", JLabel.CENTER);
        JLabel yearLabel = new JLabel("Year", JLabel.CENTER);
        JLabel amountLabel = new JLabel("Amount", JLabel.CENTER);
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

        addEntryPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        return addEntryPanel;
    }

    public JPanel setAddExpenseButtons() {
        JPanel buttons = new JPanel(new GridLayout(0, 2, 0, 0));

        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(new ConfirmAddExpense());

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new OpenExpenses());

        buttons.add(confirmButton);
        buttons.add(cancelButton);

        return buttons;
    }

    public JPanel setAddIncomeButtons() {
        JPanel buttons = new JPanel(new GridLayout(0, 2, 0, 0));

        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(new ConfirmAddIncome());

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new OpenIncomes());

        buttons.add(confirmButton);
        buttons.add(cancelButton);

        return buttons;
    }

    // MODIFIES: this
    // EFFECTS:
    public void initializeModifyExpensePanel() {
        modifyExpenseTool = new AddEntryTool();
        modifyExpensePanel = new JPanel(new BorderLayout());
        modifyExpensePanel.add(initializeAddEntryPanel(modifyExpenseTool), BorderLayout.CENTER);
        modifyExpensePanel.add(setModifyExpenseButtons(), BorderLayout.PAGE_END);
    }

    // MODIFIES: this
    // EFFECTS:
    public void initializeModifyIncomePanel() {
        modifyIncomeTool = new AddEntryTool();
        modifyIncomePanel = new JPanel(new BorderLayout());
        modifyIncomePanel.add(initializeAddEntryPanel(modifyIncomeTool), BorderLayout.CENTER);
        modifyIncomePanel.add(setModifyIncomeButtons(), BorderLayout.PAGE_END);
    }

    public JPanel setModifyExpenseButtons() {
        JPanel buttons = new JPanel(new GridLayout(0, 2, 0, 0));

        JButton confirmButton = new JButton("Confirm");
        confirmButton.setActionCommand("expense");
        confirmButton.addActionListener(new ConfirmModify());

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new OpenExpenses());

        buttons.add(confirmButton);
        buttons.add(cancelButton);

        return buttons;
    }

    public JPanel setModifyIncomeButtons() {
        JPanel buttons = new JPanel(new GridLayout(0, 2, 0, 0));

        JButton confirmButton = new JButton("Confirm");
        confirmButton.setActionCommand("income");
        confirmButton.addActionListener(new ConfirmModify());

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new OpenIncomes());

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

    public class ReturnHome implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            changePanel(mainPanel);
        }
    }

    public class OpenBudget implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            changePanel(budgetPanel);
        }
    }

    public class OpenExpenses implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            changePanel(expensesPanel);
        }
    }

    public class OpenIncomes implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            changePanel(incomesPanel);
        }
    }

    /** ActionListeners for entry operations **/

    public class AddEntry implements ActionListener {

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

    public class DeleteEntry implements ActionListener {

        // MODIFIES: this
        // EFFECTS: deletes an existing entry from the report
        @Override
        public void actionPerformed(ActionEvent e) {
            int index;

            if (e.getActionCommand().equals("expense")) {
                index = expenses.getSelectedIndex();
                expensesModel.remove(index);

                Entry entryToDelete = expenseReport.getAllEntries().get(index);
                expenseReport.deleteEntry(entryToDelete);
            } else {
                index = incomes.getSelectedIndex();
                incomesModel.remove(index);

                Entry entryToDelete = incomeReport.getAllEntries().get(index);
                incomeReport.deleteEntry(entryToDelete);
            }
        }
    }

    public class ConfirmModify implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int index;

            if (e.getActionCommand().equals("expense")) {
                String description = modifyExpenseTool.getDescriptionField().getText();
                double amount = ((Number) modifyExpenseTool.getAmountField().getValue()).doubleValue();
                int month = (int) modifyExpenseTool.getMonthSpinner().getValue() - 1;
                int day = (int) modifyExpenseTool.getDaySpinner().getValue();
                int year = (int) modifyExpenseTool.getYearSpinner().getValue();

                index = expenses.getSelectedIndex();

                Entry entryToModify = expenseReport.getAllEntries().get(index);
                entryToModify.setDescription(description);
                entryToModify.setAmount(amount);
                entryToModify.setDate(year, month, day);

                changePanel(expensesPanel);
            } else {
                String description = modifyIncomeTool.getDescriptionField().getText();
                double amount = ((Number) modifyIncomeTool.getAmountField().getValue()).doubleValue();
                int month = (int) modifyIncomeTool.getMonthSpinner().getValue() - 1;
                int day = (int) modifyIncomeTool.getDaySpinner().getValue();
                int year = (int) modifyIncomeTool.getYearSpinner().getValue();

                index = incomes.getSelectedIndex();

                Entry entryToModify = incomeReport.getAllEntries().get(index);
                entryToModify.setDescription(description);
                entryToModify.setAmount(amount);
                entryToModify.setDate(year, month, day);

                changePanel(incomesPanel);
            }
        }
    }


    public class ModifyEntry implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("expense")) {
                int index = expenses.getSelectedIndex();
                modifyExpenseTool.setAll(expenseReport.getAllEntries().get(index));
                changePanel(modifyExpensePanel);
            } else {
                int index = incomes.getSelectedIndex();
                modifyIncomeTool.setAll(incomeReport.getAllEntries().get(index));
                changePanel(modifyIncomePanel);
            }
        }
    }

    /** ActionListeners for Data Persistence **/

    public class SaveBudget implements ActionListener {

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


    public class LoadBudget implements ActionListener {

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