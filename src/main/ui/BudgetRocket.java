package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Represents the GUI of the budget application
public class BudgetRocket extends JFrame {
    private static final int X = 500;
    private static final int Y = 200;
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 700;

    private JFrame mainFrame;
    private JPanel mainPanel;
    private JPanel budgetPanel;
    private JPanel expensesPanel;
    private JPanel incomesPanel;

    // EFFECTS: runs the application
    public BudgetRocket() {
        initializePanels();
        initializeMainFrame();
    }

    // MODIFIES: this
    // EFFECTS:
    public void initializeMainFrame() {
        mainFrame = new JFrame("Budget Rocket");
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setLocation(X, Y);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        mainFrame.getContentPane().add(mainPanel);
        initializeMenuBar();
        mainFrame.pack();
        mainFrame.setVisible(true);
    }

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

    public void initializeMainPanel() {
        mainPanel = new JPanel();
        mainPanel.setBackground(Color.GRAY);
        JLabel title = new JLabel("Budget Rocket");
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 50));
        title.setForeground(Color.WHITE);
        mainPanel.add(title, BorderLayout.NORTH);
    }

    // REQUIRES:
    // MODIFIES:
    // EFFECTS:
    public void initializeBudgetPanel() {
        budgetPanel = new JPanel();
        budgetPanel.setBackground(Color.GRAY);
        JLabel title = new JLabel("Budget Report");
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 50));
        title.setForeground(Color.WHITE);
        budgetPanel.add(title, BorderLayout.NORTH);
    }

    // REQUIRES:
    // MODIFIES:
    // EFFECTS:
    public void initializeExpensesPanel() {
        expensesPanel = new JPanel();
        expensesPanel.setBackground(Color.GRAY);
        JLabel title = new JLabel("Expense Report");
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 50));
        title.setForeground(Color.WHITE);
        expensesPanel.add(title, BorderLayout.NORTH);

        JMenuBar expensesMenuBar = new JMenuBar();
        JMenu expensesMenu = new JMenu("Options");
        JMenuItem addEntry = new JMenuItem("Add Entry");

        expensesPanel.add(expensesMenuBar, BorderLayout.CENTER);
        expensesMenuBar.add(expensesMenu);
        expensesMenu.add(addEntry);
    }

    // REQUIRES:
    // MODIFIES:
    // EFFECTS:
    public void initializeIncomesPanel() {
        incomesPanel = new JPanel();
        incomesPanel.setBackground(Color.GRAY);
        JLabel title = new JLabel("Income Report");
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 50));
        title.setForeground(Color.WHITE);
        incomesPanel.add(title, BorderLayout.NORTH);
    }

    public void initializePanels() {
        initializeMainPanel();
        initializeBudgetPanel();
        initializeExpensesPanel();
        initializeIncomesPanel();
    }

    // REQUIRES:
    // MODIFIES:
    // EFFECTS:
    public void initializeAdditionFrame() {
        // stub
    }

    // MODIFIES: this
    // EFFECTS:
    public void changePanel(JPanel panel) {
        mainFrame.getContentPane().removeAll();
        mainFrame.getContentPane().add(panel);
        mainFrame.getContentPane().validate();
        mainFrame.getContentPane().repaint();
    }

    // REQUIRES:
    // MODIFIES:
    // EFFECTS:
    public void initializeModificationFrame() {
        // stub
    }

    public static void main(String[] args) {
        new BudgetRocket();
    }

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

    public class SaveBudget implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            //Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(mainFrame, "Save successful!");
        }
    }

    public class LoadBudget implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            //Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(mainFrame, "Load successful!");
        }
    }
}
