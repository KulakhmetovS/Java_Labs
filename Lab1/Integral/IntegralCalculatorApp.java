package Integral;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class IntegralCalculatorApp extends JFrame {

    private JTextField lowerBoundField;
    private JTextField upperBoundField;
    private JTextField stepField;
    private JTable table;
    private DefaultTableModel tableModel;

    public IntegralCalculatorApp() {
        // Определение окна
        setTitle("Вычислить интеграл 1/ln(x)");
        setSize(600, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Панель для полей ввода и кнопок
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        // Нижний предел
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(2, 5, 2, 0); // Отступы для меток
        inputPanel.add(new JLabel("Нижний предел:"), gbc);
        gbc.insets = new Insets(2, 130, 2, 100); // Отступы для полей ввода
        lowerBoundField = new JTextField();
        lowerBoundField.setPreferredSize(new Dimension(150, 25)); // Размер поля ввода
        gbc.gridx = 0;
        inputPanel.add(lowerBoundField, gbc);

        JButton addButton = new JButton("Добавить");
        addButton.setPreferredSize(new Dimension(100, 25)); // Размер кнопки
        gbc.gridx = 2;
        gbc.insets = new Insets(10, 120, 10, 5); // Отступы для кнопок
        inputPanel.add(addButton, gbc);

        // Верхний предел
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(2, 5, 2, 0); // Отступы для меток
        inputPanel.add(new JLabel("Верхний предел:"), gbc);
        gbc.insets = new Insets(2, 130, 2, 100); // Отступы для полей ввода
        upperBoundField = new JTextField();
        upperBoundField.setPreferredSize(new Dimension(150, 25)); // Размер поля ввода
        gbc.gridx = 0;
        inputPanel.add(upperBoundField, gbc);

        JButton removeButton = new JButton("Удалить");
        removeButton.setPreferredSize(new Dimension(100, 25)); // Размер кнопки
        gbc.gridx = 2;
        gbc.insets = new Insets(10, 120, 10, 5); // Отступы для кнопок
        inputPanel.add(removeButton, gbc);

        // Шаг
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.insets = new Insets(2, 5, 2, 0); // Отступы для меток
        inputPanel.add(new JLabel("Шаг:"), gbc);
        gbc.insets = new Insets(2, 130, 2, 100); // Отступы для полей ввода
        stepField = new JTextField();
        stepField.setPreferredSize(new Dimension(150, 25)); // Размер поля ввода
        gbc.gridx = 0;
        inputPanel.add(stepField, gbc);

        JButton calculateButton = new JButton("Вычислить");
        calculateButton.setPreferredSize(new Dimension(100, 25)); // Размер кнопки
        gbc.gridx = 2;
        gbc.insets = new Insets(10, 120, 10, 5); // Отступы для кнопок
        inputPanel.add(calculateButton, gbc);

        // Таблица результатов
        String[] columnNames = {"Нижний предел", "Верхний предел", "Шаг", "Результат"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // колонки не редактируемы
                return false;
            }
        };
        table = new JTable(tableModel);

        // Добавление компонентов в окно
        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Обработчики событий для кнопок
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addRow();
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeRow();
            }
        });

        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateIntegral();
            }
        });
    }

    // Метод для добавления строки в таблицу
    private void addRow() {
        double lowerBound = Double.parseDouble(lowerBoundField.getText());
        double upperBound = Double.parseDouble(upperBoundField.getText());
        double step = Double.parseDouble(stepField.getText());

        tableModel.addRow(new Object[]{lowerBound, upperBound, step, null});
    }

    // Метод для удаления выделенной строки из таблицы
    private void removeRow() {
        int selectedRow = table.getSelectedRow();

        tableModel.removeRow(selectedRow);
    }

    private void calculateIntegral() {
        int selectedRow = table.getSelectedRow();

        double lowerBound = (double) tableModel.getValueAt(selectedRow, 0);
        double upperBound = (double) tableModel.getValueAt(selectedRow, 1);
        double eps = (double) tableModel.getValueAt(selectedRow, 2);

        double result = trapezoidMethod(lowerBound, upperBound, eps);
        tableModel.setValueAt(result, selectedRow, 3);
    }

    // Метод для вычисления интеграла методом трапеций
    public double trapezoidMethod(double a, double b, double eps) {
        double sum = 0.0;
        double currentX = a;

        while (currentX < b) {
            double nextX = (currentX + eps < b) ? currentX + eps : b; // Проверка на достижение верхнего предела
            sum += (f(currentX) + f(nextX)) * (nextX - currentX) / 2; // Вычисление площади трапеции
            currentX = nextX; // Переход к следующему шагу
        }

        return sum;
    }

    // Определение функции 1/ln(x)
    public double f(double x) {
        return 1 / Math.log(x); // Определение функции 1/ln(x)
    }
}
