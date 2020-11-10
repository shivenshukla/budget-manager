package ui.tools;

import javax.swing.*;
import java.awt.*;

public class BarChart extends JPanel {
    private static final Color EXPENSE_COLOR = Color.RED;
    private static final Color INCOME_COLOR = Color.GREEN;
    private static final int PANEL_WIDTH = 750;
    private static final int PANEL_HEIGHT = 500;
    private static final int BAR_WIDTH = 75;

    private double expenseTotal;
    private double incomeTotal;

    public BarChart(double expenseTotal, double incomeTotal) {
        JLabel title = new JLabel("Total Budget");
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
        title.setForeground(Color.BLACK);
        setBackground(Color.WHITE);
        add(title);
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.expenseTotal = expenseTotal;
        this.incomeTotal = incomeTotal;
    }

    public double getHeightRatio() {
        double maxTotal;
        if (expenseTotal > incomeTotal) {
            maxTotal = expenseTotal;
        } else {
            maxTotal = incomeTotal;
        }

        return maxTotal / (PANEL_HEIGHT - 50);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        double heightRatio = getHeightRatio();
        makeBar(g, EXPENSE_COLOR, (PANEL_WIDTH / 2) - (PANEL_WIDTH / 4), (int) (expenseTotal / heightRatio));
        makeBar(g, INCOME_COLOR, (PANEL_WIDTH / 2) + (PANEL_WIDTH / 4), (int) (incomeTotal / heightRatio));
        g.setColor(Color.BLACK);
        g.drawString("Expenses", (PANEL_WIDTH / 2) - (PANEL_WIDTH / 4), PANEL_HEIGHT);
        g.drawString("Income", (PANEL_WIDTH / 2) + (PANEL_WIDTH / 4), PANEL_HEIGHT);
        g.drawString("500", 0, 10);
        g.drawString("250", 0, (PANEL_HEIGHT / 2));
        g.drawString("0", 0, PANEL_HEIGHT - 10);
        g.drawLine(0, PANEL_HEIGHT - 10, PANEL_WIDTH, PANEL_HEIGHT - 10);
        g.drawLine(10, PANEL_HEIGHT - 1, 10, 0);
    }

    public void makeBar(Graphics g, Color c, int x, int height) {
        g.setColor(c);
        g.drawRect(x, PANEL_HEIGHT - height, BAR_WIDTH, height);
        g.fillRect(x, PANEL_HEIGHT - height, BAR_WIDTH,  height);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);
        frame.setResizable(false);
        frame.add(new BarChart(100, 50));
        frame.pack();
        frame.setVisible(true);
    }
}
