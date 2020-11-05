package ui;

import com.sun.org.apache.xpath.internal.objects.XNumber;
import model.Budget;
import model.Entry;
import model.Expense;
import model.Report;
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
    private JPanel expensesPanel;
    private JPanel incomesPanel;
    private JPanel addEntryPanel;

    private DefaultListModel<Entry> expensesModel;
    private JList<Entry> expenses;
    private DefaultListModel<Entry> incomesModel;
    private JList<Entry> incomes;

    JSpinner daySpinner;
    JSpinner monthSpinner;
    JSpinner yearSpinner;
    JFormattedTextField amountField;
    JTextField descriptionField;

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
        initializeAddEntryPanel();
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
        addButton.addActionListener(new AddEntry());
        JButton modifyButton = new JButton("Modify");
        JButton deleteButton = new JButton("Delete");

        addButton.setBackground(Color.WHITE);
        modifyButton.setBackground(Color.WHITE);
        deleteButton.setBackground(Color.WHITE);

        buttons.add(addButton);
        buttons.add(modifyButton);
        buttons.add(deleteButton);

        deleteButton.addActionListener(new DeleteEntry());

        expensesPanel.add(buttons, BorderLayout.PAGE_END);
        expensesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }


    // MODIFIES: this
    // EFFECTS: constructs a JPanel for the income report
    public void initializeIncomesPanel() {
        JLabel title = new JLabel("Income Report");
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
        title.setForeground(Color.WHITE);

        incomesPanel = new JPanel();
        incomesPanel.setBackground(Color.GRAY);
        incomesPanel.add(title, BorderLayout.NORTH);
    }

    // MODIFIES: this
    // EFFECTS:
    public void initializeAddEntryPanel() {
        addEntryPanel = new JPanel(new GridLayout(6, 2,10, 10));

        // Date Spinners
        Calendar currentDate = new GregorianCalendar();
        int currentDay = currentDate.get(Calendar.DAY_OF_MONTH);
        int currentMonth = currentDate.get(Calendar.MONTH) + 1;
        int currentYear = currentDate.get(Calendar.YEAR);

        SpinnerNumberModel dayModel = new SpinnerNumberModel(currentDay, 1, 31, 1);
        daySpinner = new JSpinner(dayModel);

        SpinnerNumberModel monthModel = new SpinnerNumberModel(currentMonth, 1, 12, 1);
        monthSpinner = new JSpinner(monthModel);

        SpinnerNumberModel yearModel = new SpinnerNumberModel(currentYear, 1900, 2100, 1);
        yearSpinner = new JSpinner(yearModel);

        NumberFormat amountFormat = NumberFormat.getNumberInstance();
        amountFormat.setMinimumFractionDigits(2);
        amountField = new JFormattedTextField(amountFormat);
        amountField.setValue(0);

        descriptionField = new JTextField();
        descriptionField.setText("Enter here");

        JLabel dayLabel = new JLabel("Day", JLabel.CENTER);
        JLabel monthLabel = new JLabel("Month", JLabel.CENTER);
        JLabel yearLabel = new JLabel("Year", JLabel.CENTER);
        JLabel amountLabel = new JLabel("Amount", JLabel.CENTER);
        JLabel descriptionLabel = new JLabel("Description", JLabel.CENTER);

        addEntryPanel.add(dayLabel);
        addEntryPanel.add(daySpinner);

        addEntryPanel.add(monthLabel);
        addEntryPanel.add(monthSpinner);

        addEntryPanel.add(yearLabel);
        addEntryPanel.add(yearSpinner);

        addEntryPanel.add(amountLabel);
        addEntryPanel.add(amountField);

        addEntryPanel.add(descriptionLabel);
        addEntryPanel.add(descriptionField);

        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(new DoConfirm());

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new OpenExpenses());

        addEntryPanel.add(confirmButton);
        addEntryPanel.add(cancelButton);

        addEntryPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    // MODIFIES: this
    // EFFECTS:
    public void initializeModifyEntryPanel() {
        // TODO: complete the implementation of this method
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
    // EFFECTS: updates the expenses in the JList
    public void updateExpenseReport() {
        expensesModel.clear();

        for (Entry e: expenseReport.getAllEntries()) {
            expensesModel.addElement(e);
        }
    }

    public static void main(String[] args) {
        new BudgetRocket();
    }

    /** ActionListeners **/

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
            changePanel(addEntryPanel);
        }
    }

    public class DoConfirm implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String description = descriptionField.getText();
            double amount = ((Number) amountField.getValue()).doubleValue();
            int month = (int) monthSpinner.getValue() - 1;
            int day = (int) daySpinner.getValue();
            int year = (int) yearSpinner.getValue();

            Expense entry = new Expense(description, amount, new GregorianCalendar(year, month, day));
            budget.addExpense(entry);
            expensesModel.addElement(entry);
            changePanel(expensesPanel);
        }
    }

    public class DeleteEntry implements ActionListener {

        // MODIFIES: this
        // EFFECTS: deletes an existing entry from the report
        @Override
        public void actionPerformed(ActionEvent e) {
            int index = expenses.getSelectedIndex();
            expensesModel.remove(index);

            Entry entryToDelete = expenseReport.getAllEntries().get(index);
            expenseReport.deleteEntry(entryToDelete);
        }
    }

    public class ModifyEntry implements ActionListener {

        // MODIFIES: this
        // EFFECTS: modifies an existing entry in the report
        @Override
        public void actionPerformed(ActionEvent e) {
            // TODO: complete the implementation of this method
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
            } finally {
                //Toolkit.getDefaultToolkit().beep();
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
                updateExpenseReport();
                JOptionPane.showMessageDialog(mainFrame, "Budget successfully loaded from " + BUDGET_DATA);
            } catch (IOException exception) {
                JOptionPane.showMessageDialog(mainFrame, "Unable to load budget from " + BUDGET_DATA,
                        "Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                //Toolkit.getDefaultToolkit().beep();
            }
        }
    }
}
