package ui.gui;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

// Documentation for Graphics: https://docs.oracle.com/javase/7/docs/api/java/awt/Graphics.html

// Represents a bar chart
public class BarChart extends JPanel {
    // bar constants
    private static final Color EXPENSE_COLOR = new Color(217, 42, 48);
    private static final Color INCOME_COLOR = new Color(65, 169, 76);
    private static final int BAR_WIDTH = 75;
    // chart constants
    private static final int PANEL_WIDTH = 980;
    private static final int PANEL_HEIGHT = 450;
    private static final int CHART_X1 = 100;
    private static final int CHART_X2 = PANEL_WIDTH - (CHART_X1 / 2);
    private static final int CHART_Y1 = 50;
    private static final int CHART_Y2 = PANEL_HEIGHT - (CHART_Y1 / 2);

    private final double expenseTotal;
    private final double incomeTotal;

    // EFFECTS: constructs a new bar chart with given expenseTotal and incomeTotal
    // as the heights of each bar
    public BarChart(double expenseTotal, double incomeTotal) {
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        setBackground(Color.WHITE);
        drawTitle();

        this.expenseTotal = expenseTotal;
        this.incomeTotal = incomeTotal;
    }

    // MODIFIES: this
    // EFFECTS: draws a bar chart onto the JPanel
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        double heightRatio = getHeightRatio();
        drawGrid(g);
        drawBar(g, EXPENSE_COLOR, (CHART_X2 / 2) - (CHART_X2 / 4),
                (int) (expenseTotal / heightRatio));
        drawBar(g, INCOME_COLOR, (CHART_X2 / 2) + (CHART_X2 / 4),
                (int) (incomeTotal / heightRatio));
        drawAxis(g);
    }

    // MODIFIES: this
    // EFFECTS: draws a title onto the JPanel
    public void drawTitle() {
        JLabel title = new JLabel("Expense vs. Income");
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        title.setForeground(Color.BLACK);
        add(title, BorderLayout.CENTER);
    }

    // MODIFIES: this
    // EFFECTS: draws the x and y-axis onto the JPanel
    public void drawAxis(Graphics g) {
        g.setColor(Color.BLACK);

        // x-axis
        g.drawLine(CHART_X1, CHART_Y2, CHART_X2, CHART_Y2);

        g.drawString("Expenses", (CHART_X2 / 2) - (CHART_X2 / 4) + 10, CHART_Y2 + 15);
        g.drawString("Income", (CHART_X2 / 2) + (CHART_X2 / 4) + 15, CHART_Y2 + 15);

        // y-axis
        double maxY = Math.max(expenseTotal, incomeTotal);
        g.drawLine(CHART_X1, CHART_Y1, CHART_X1, CHART_Y2);

        DecimalFormat decimalFormat = new DecimalFormat("###,##0.00");

        if (expenseTotal != 0 || incomeTotal != 0) {
            g.drawString("$0.00", 5, CHART_Y2);

            for (int i = 1; i <= 4; i++) {
                g.drawString("$" + decimalFormat.format(i * maxY / 4), 5,
                        CHART_Y2 - (i * (CHART_Y2 - CHART_Y1) / 4) + 5);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: draws a grid onto the JPanel
    public void drawGrid(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);

        g.drawLine(CHART_X1, CHART_Y1, CHART_X2, CHART_Y1);
        g.drawLine(CHART_X2, CHART_Y1, CHART_X2, CHART_Y2);

        for (int i = 1; i <=  4; i++) {
            g.drawLine(CHART_X1, CHART_Y2 - (i * (CHART_Y2 - CHART_Y1) / 4),
                    CHART_X2, CHART_Y2 - (i * (CHART_Y2 - CHART_Y1) / 4));
        }
    }

    // MODIFIES: this
    // EFFECTS: draws a vertical bar onto the JPanel with given properties
    public void drawBar(Graphics g, Color c, int x, int height) {
        g.setColor(c);
        g.drawRect(x, CHART_Y2 - height, BAR_WIDTH, height);
        g.fillRect(x, CHART_Y2 - height, BAR_WIDTH,  height);
    }

    // EFFECTS: returns the dollar amount per height of the bar chart;
    // this ratio is used to determine the height and and corresponding y-axis labels of each bar
    public double getHeightRatio() {
        return Math.max(expenseTotal, incomeTotal) / (CHART_Y2 - CHART_Y1);
    }
}
