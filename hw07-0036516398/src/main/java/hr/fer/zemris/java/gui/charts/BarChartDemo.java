package hr.fer.zemris.java.gui.charts;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author MatejCubek
 * @project hw07-0036516398
 * @created 16/12/2020
 */
public class BarChartDemo extends JFrame {
    private final BarChart barChart;
    private final Path barChartPath;

    public BarChartDemo(Path barChartPath) throws HeadlessException, IOException {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("BarChart");
        setSize(500, 400);

        this.barChart = parseBarChartFile(barChartPath);
        this.barChartPath = barChartPath;

        initGUI();
    }

    private static BarChart parseBarChartFile(Path barChartPath) throws IOException {
        var lines = Files.readAllLines(barChartPath);
        String xLabel = lines.get(0);
        String yLabel = lines.get(1);
        var values = Arrays.stream(lines.get(2).split("\\s+"))
                .map(v -> {
                    String[] array = v.split(",");
                    int x = Integer.parseInt(array[0]);
                    int y = Integer.parseInt(array[1]);
                    return new XYValue(x, y);
                })
                .collect(Collectors.toList());
        int minY = Integer.parseInt(lines.get(3));
        int maxY = Integer.parseInt(lines.get(4));
        int gap = Integer.parseInt(lines.get(5));

        return new BarChart(values, xLabel, yLabel, minY, maxY, gap);
    }

    private void initGUI() {
        Container container = getContentPane();
        container.setLayout(new BorderLayout());

        JLabel label = new JLabel(barChartPath.toAbsolutePath().toString());
        label.setHorizontalAlignment(SwingConstants.CENTER);
        container.add(label, BorderLayout.PAGE_START);

        container.add(new BarChartComponent(barChart), BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        if (args.length > 1) throw new IllegalArgumentException("Too many arguments!");

        Path path = args.length == 1 ? Path.of(args[0]) : Path.of("src/main/resources/testLayout.txt");

        SwingUtilities.invokeLater(() -> {
            try {
                new BarChartDemo(path).setVisible(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
