package ui.tools;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

// Represents a bar chart
public class BarChart extends JPanel {
    private static final Color EXPENSE_COLOR = new Color(217, 42, 48);
    private static final Color INCOME_COLOR = new Color(65, 169, 76);
    private static final int CHART_WIDTH = 930;
    private static final int CHART_HEIGHT = 430;
    private static final int BAR_WIDTH = 75;

    private final double expenseTotal;
    private final double incomeTotal;

    // EFFECTS: constructs a new bar chart with given expenseTotal and incomeTotal
    // as the heights of each bar
    public BarChart(double expenseTotal, double incomeTotal) {
        setPreferredSize(new Dimension(980, 450));
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
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
        drawBar(g, EXPENSE_COLOR, (CHART_WIDTH / 2) - (CHART_WIDTH / 4), (int) (expenseTotal / heightRatio));
        drawBar(g, INCOME_COLOR, (CHART_WIDTH / 2) + (CHART_WIDTH / 4), (int) (incomeTotal / heightRatio));
        drawAxis(g);
    }

    // MODIFIES: this
    // EFFECTS: draws a title onto the JPanel
    public void drawTitle() {
        JLabel title = new JLabel("Total Budget");
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
        title.setForeground(Color.BLACK);
        add(title, BorderLayout.CENTER);
    }

    // MODIFIES: this
    // EFFECTS: draws x and y-axis onto the JPanel
    public void drawAxis(Graphics g) {
        g.setColor(Color.BLACK);

        // x-axis
        g.drawLine(75, CHART_HEIGHT, CHART_WIDTH, CHART_HEIGHT);
        g.drawString("Expenses", (CHART_WIDTH / 2) - (CHART_WIDTH / 4) + 10, CHART_HEIGHT + 15);
        g.drawString("Income", (CHART_WIDTH / 2) + (CHART_WIDTH / 4) + 15, CHART_HEIGHT + 15);

        // y-axis
        double maxY = Math.max(expenseTotal, incomeTotal);
        g.drawLine(75, 50, 75, CHART_HEIGHT);

        g.drawString("$0.00", 5, CHART_HEIGHT);

        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        if (expenseTotal != 0 || incomeTotal != 0) {
            for (int i = 1; i <= 4; i++) {
                g.drawString("$" + decimalFormat.format(i * maxY / 4), 5,
                        CHART_HEIGHT - (i * (CHART_HEIGHT - 50) / 4) + 5);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: draws a grid onto the JPanel
    public void drawGrid(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);

        g.drawLine(75, 50, CHART_WIDTH, 50);
        g.drawLine(CHART_WIDTH, 50, CHART_WIDTH, CHART_HEIGHT);

        for (int i = 1; i <=  4; i++) {
            g.drawLine(75, CHART_HEIGHT - (i * (CHART_HEIGHT - 50) / 4), CHART_WIDTH,
                    CHART_HEIGHT - (i * (CHART_HEIGHT - 50) / 4));
        }
    }

    // MODIFIES: this
    // EFFECTS: draws a vertical bar onto the JPanel with given properties
    public void drawBar(Graphics g, Color c, int x, int height) {
        g.setColor(c);
        g.drawRect(x, CHART_HEIGHT - height, BAR_WIDTH, height);
        g.fillRect(x, CHART_HEIGHT - height, BAR_WIDTH,  height);
    }

    // TODO: add specification
    public double getHeightRatio() {
        return Math.max(expenseTotal, incomeTotal) / (CHART_HEIGHT - 50);
    }
}
