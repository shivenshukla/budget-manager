package ui;

import model.*;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Calendar;
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
    private JPanel expenseReportPanel;
    private JPanel incomeReportPanel;
    private JPanel addExpensePanel;
    private JPanel addIncomePanel;
    private JPanel modifyIncomePanel;
    private JPanel modifyExpensePanel;

    private DefaultListModel<Entry> expensesModel;
    private JList<Entry> expenses;
    private DefaultListModel<Entry> incomesModel;
    private JList<Entry> incomes;

    JSpinner daySpinner;
    JSpinner monthSpinner;
    JSpinner yearSpinner;
    JFormattedTextField amountField;
    JTextField descriptionField;

    SpinnerNumberModel dayModel;
    SpinnerNumberModel monthModel;
    SpinnerNumberModel yearModel;

    // MODIFIES: this
    // EFFECTS: runs the application
    public BudgetRocket() {
        initializeBudgetFields();
        initializePanels();
        initializeMainFrame();
    }

    // MODIFIES: this
    // EFFECTS: constructs a new budget with an empty income and expense report.
    // Instantiates a new JsonReader and JsonWriter with the BUDGET_DATA file.
    public void initializeBudgetFields() {
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
        JMenuItem homeItem =  new JMenuItem("Home");
        JMenuItem budgetItem = new JMenuItem("Budget");
        JMenuItem expensesItem = new JMenuItem("Expenses");
        JMenuItem incomesItem = new JMenuItem("Incomes");

        saveItem.addActionListener(new SaveBudget());
        loadItem.addActionListener(new LoadBudget());

        budgetItem.setActionCommand("openBudget");
        budgetItem.addActionListener(new OpenAction());

        expensesItem.setActionCommand("openExpenseReport");
        expensesItem.addActionListener(new OpenAction());

        incomesItem.setActionCommand("openIncomeReport");
        incomesItem.addActionListener(new OpenAction());

        homeItem.setActionCommand("openHome");
        homeItem.addActionListener(new OpenAction());

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
        initializeExpenseReportPanel();
        initializeIncomeReportPanel();
        initializeAddIncomePanel();
        initializeAddExpensePanel();
        initializeModifyIncomePanel();
        initializeModifyExpensePanel();
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
    public void initializeExpenseReportPanel() {
        JLabel title = new JLabel("Expense Report");
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
        title.setForeground(Color.WHITE);

        BorderLayout expensesLayout = new BorderLayout();
        expensesLayout.setVgap(5);

        expenseReportPanel = new JPanel(expensesLayout);
        expenseReportPanel.setBackground(Color.GRAY);
        expenseReportPanel.add(title, BorderLayout.NORTH);

        expensesModel = new DefaultListModel<>();
        expenses = new JList<>();
        expenses.setModel(expensesModel);

        expenses.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        expenses.setLayoutOrientation(JList.VERTICAL);
        expenses.setVisibleRowCount(-1);

        JScrollPane listScrollPane = new JScrollPane(expenses);
        listScrollPane.setPreferredSize(new Dimension(100, 100));
        expenseReportPanel.add(listScrollPane, BorderLayout.CENTER);

        setReportPanelButtons(expenseReportPanel, "expense");

        expenseReportPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    // MODIFIES: this
    // EFFECTS: constructs a JPanel for the income report
    public void initializeIncomeReportPanel() {
        JLabel title = new JLabel("Income Report");
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
        title.setForeground(Color.WHITE);

        BorderLayout expensesLayout = new BorderLayout();
        expensesLayout.setVgap(5);

        incomeReportPanel = new JPanel(expensesLayout);
        incomeReportPanel.setBackground(Color.GRAY);
        incomeReportPanel.add(title, BorderLayout.NORTH);

        incomesModel = new DefaultListModel<>();
        incomes = new JList<>();
        incomes.setModel(incomesModel);

        incomes.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        incomes.setLayoutOrientation(JList.VERTICAL);
        incomes.setVisibleRowCount(-1);

        JScrollPane listScrollPane = new JScrollPane(incomes);
        listScrollPane.setPreferredSize(new Dimension(100, 100));
        incomeReportPanel.add(listScrollPane, BorderLayout.CENTER);

        setReportPanelButtons(incomeReportPanel, "income");

        incomeReportPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    public void initializeAddExpensePanel() {
        addExpensePanel = new JPanel(new GridLayout(0, 2, 10, 0));

        setEntryLeftPanel(addExpensePanel, "expense");
        setEntryRightPanel(addExpensePanel, "openExpenseReport");

        addExpensePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    // MODIFIES: this
    // EFFECTS:
    public void initializeAddIncomePanel() {
        addIncomePanel = new JPanel(new GridLayout(0, 2, 10, 0));

        setEntryLeftPanel(addIncomePanel, "income");
        setEntryRightPanel(addIncomePanel, "openIncomeReport");

        addIncomePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    // MODIFIES: this
    // EFFECTS:
    public void initializeModifyExpensePanel() {
        modifyExpensePanel = new JPanel(new GridLayout(0, 2, 10, 0));

        setEntryLeftPanel(modifyExpensePanel, "expense");
        setEntryRightPanel(modifyExpensePanel, "openExpenseReport");

        modifyExpensePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    public void initializeModifyIncomePanel() {
        modifyIncomePanel = new JPanel(new GridLayout(0, 2, 10, 0));

        setEntryLeftPanel(modifyIncomePanel, "income");
        setEntryRightPanel(modifyIncomePanel, "openIncomeReport");

        modifyIncomePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    public void setReportPanelButtons(JComponent parent, String actionCommand) {
        JPanel buttons = new JPanel(new GridLayout(0, 3, 2, 0));

        JButton addButton = new JButton("Add");
        addButton.setActionCommand(actionCommand);
        addButton.addActionListener(new AddEntry());

        JButton modifyButton = new JButton("Modify");
        modifyButton.setActionCommand(actionCommand);
        modifyButton.addActionListener(new ModifyEntry());

        JButton deleteButton = new JButton("Delete");
        deleteButton.setActionCommand(actionCommand);
        deleteButton.addActionListener(new DeleteEntry());

        addButton.setBackground(Color.WHITE);
        modifyButton.setBackground(Color.WHITE);
        deleteButton.setBackground(Color.WHITE);

        buttons.add(addButton);
        buttons.add(modifyButton);
        buttons.add(deleteButton);

        parent.add(buttons, BorderLayout.PAGE_END);
    }

    /** Helper methods for panels **/

    public void setEntryLeftPanel(JComponent parent, String actionCommand) {
        JPanel labelPanel = new JPanel(new GridLayout(6, 1, 0, 10));

        JLabel dayLabel = new JLabel("Day", JLabel.CENTER);
        JLabel monthLabel = new JLabel("Month", JLabel.CENTER);
        JLabel yearLabel = new JLabel("Year", JLabel.CENTER);
        JLabel amountLabel = new JLabel("Amount", JLabel.CENTER);
        JLabel descriptionLabel = new JLabel("Description", JLabel.CENTER);

        labelPanel.add(dayLabel);
        labelPanel.add(monthLabel);
        labelPanel.add(yearLabel);
        labelPanel.add(amountLabel);
        labelPanel.add(descriptionLabel);

        JButton confirmButton = new JButton("Confirm");
        confirmButton.setActionCommand(actionCommand);
        confirmButton.addActionListener(new ConfirmEntry());
        labelPanel.add(confirmButton);

        parent.add(labelPanel);
    }

    public void setEntryRightPanel(JComponent parent, String actionCommand) {
        JPanel fieldsPanel = new JPanel(new GridLayout(6, 1, 0, 10));

        NumberFormat amountFormat = NumberFormat.getNumberInstance();
        amountFormat.setMinimumFractionDigits(2);
        amountField = new JFormattedTextField(amountFormat);
        amountField.setValue(0);

        descriptionField = new JTextField();
        descriptionField.setText("Enter here");

        setEntrySpinners();

        fieldsPanel.add(daySpinner);
        fieldsPanel.add(monthSpinner);
        fieldsPanel.add(yearSpinner);
        fieldsPanel.add(amountField);
        fieldsPanel.add(descriptionField);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand(actionCommand);
        cancelButton.addActionListener(new OpenAction());
        fieldsPanel.add(cancelButton);

        parent.add(fieldsPanel);
    }

    public void setEntrySpinners() {
        Calendar currentDate = new GregorianCalendar();
        int currentDay = currentDate.get(Calendar.DAY_OF_MONTH);
        int currentMonth = currentDate.get(Calendar.MONTH) + 1;
        int currentYear = currentDate.get(Calendar.YEAR);

        dayModel = new SpinnerNumberModel(currentDay, 1, 31, 1);
        daySpinner = new JSpinner(dayModel);

        monthModel = new SpinnerNumberModel(currentMonth, 1, 12, 1);
        monthSpinner = new JSpinner(monthModel);

        yearModel = new SpinnerNumberModel(currentYear, 1900, 2100, 1);
        yearSpinner = new JSpinner(yearModel);
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
    // EFFECTS: updates the the entries in the given list model
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
                    changePanel(budgetPanel);
                    break;
                case "openExpenseReport":
                    changePanel(expenseReportPanel);
                    break;
                case "openIncomeReport":
                    changePanel(incomeReportPanel);
                    break;
            }
        }
    }

    /** ActionListeners for entry operations **/

    public class AddEntry implements ActionListener {

        // MODIFIES: this
        // EFFECTS: adds a new entry to the report
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("expense")) {
                changePanel(addExpensePanel);
            } else {
                changePanel(addIncomePanel);
            }
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

    public class ModifyEntry implements ActionListener {

        // MODIFIES: this
        // EFFECTS: modifies an existing entry in the report
        @Override
        public void actionPerformed(ActionEvent e) {
            int index = expenses.getSelectedIndex();
            Entry entryToModify = expenseReport.getAllEntries().get(index);
            String description = entryToModify.getDescription();
            double amount = entryToModify.getAmount();
            int year = entryToModify.getDate().get(Calendar.YEAR);
            int month = entryToModify.getDate().get(Calendar.MONTH) + 1;
            int day = entryToModify.getDate().get(Calendar.DAY_OF_MONTH);

            descriptionField.setText(description);
            amountField.setValue(amount);
            yearSpinner.setValue(year);
            monthSpinner.setValue(month);
            daySpinner.setValue(day);

            if (e.getActionCommand().equals("expense")) {
                changePanel(modifyExpensePanel);
            } else {
                changePanel(modifyIncomePanel);
            }
        }
    }

    public class ConfirmEntry implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String description = descriptionField.getText();
            double amount = ((Number) amountField.getValue()).doubleValue();
            int month = (int) monthSpinner.getValue() - 1;
            int day = (int) daySpinner.getValue();
            int year = (int) yearSpinner.getValue();

            if (e.getActionCommand().equals("expense")) {
                Expense expense = new Expense(description, amount, new GregorianCalendar(year, month, day));
                budget.addExpense(expense);
                expensesModel.addElement(expense);
                changePanel(expenseReportPanel);
            } else {
                Income income = new Income(description, amount, new GregorianCalendar(year, month, day));
                budget.addIncome(income);
                incomesModel.addElement(income);
                changePanel(incomeReportPanel);
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
