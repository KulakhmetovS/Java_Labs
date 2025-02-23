package Integral;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;
import java.util.List;

import Integral.RecIntegral;

public class IntegralCalculatorApp extends JFrame {

    private JTextField lowerBoundField;
    private JTextField upperBoundField;
    private JTextField stepField;
    private JTable table;
    private DefaultTableModel tableModel;

    private List<RecIntegral> recIntegral = new ArrayList<>();  //Коллекйия объектов класса RecIntegral

    public IntegralCalculatorApp() {
        // Определение окна
        setTitle("Вычислить интеграл 1/ln(x)");
        setSize(600, 400);
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
                // Только первые три колонки редактируемы
                return column < 3;
            }
        };
        table = new JTable(tableModel);

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbConst = new GridBagConstraints();
        gbConst.fill = GridBagConstraints.HORIZONTAL;
        gbConst.weightx = 1;

        JButton clearButton = new JButton("Очистить");
        clearButton.setPreferredSize(new Dimension(100, 25)); // Размер кнопки
        gbConst.gridy = 0;
        gbConst.gridx = 0;
        gbc.insets = new Insets(10, 120, 10, 5); // Отступы для кнопок
        buttonPanel.add(clearButton, gbConst);

        JButton fullButton = new JButton("Заполнить");
        fullButton.setPreferredSize(new Dimension(100, 25)); // Размер кнопки
        gbConst.gridy = 0;
        gbConst.gridx = 1;
        gbc.insets = new Insets(10, 120, 10, 5); // Отступы для кнопок
        buttonPanel.add(fullButton, gbConst);

        // Добавление компонентов в окно
        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

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

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeNotes();
            }
        });

        fullButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadNotes();
            }
        });
    }

    // Метод для добавления строки в таблицу
    private void addRow() {
        double lowerBound = Double.parseDouble(lowerBoundField.getText());
        double upperBound = Double.parseDouble(upperBoundField.getText());
        double step = Double.parseDouble(stepField.getText());

        tableModel.addRow(new Object[]{lowerBound, upperBound, step, null});

        recIntegral.add(new RecIntegral(lowerBound, upperBound, step)); //Добавление данных в новый объект
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

        recIntegral.get(selectedRow).setResult(result); //добавление результата в объект коллекции
    }

    private void removeNotes() {
      tableModel.setRowCount(0);  //Удаление записей из таблицы
    }

    private void loadNotes() {
      tableModel.setRowCount(0);

      for (int i = 0; i < recIntegral.size(); i++) {  //Получение записей из коллекции
        double lowerBound = recIntegral.get(i).getLowerBound();
        double upperBound = recIntegral.get(i).getUpperBound();
        double step = recIntegral.get(i).getStep();
        double result = recIntegral.get(i).getResult();

        tableModel.addRow(new Object[]{lowerBound, upperBound, step, result});  //Заполнение строки таблицы полученными данными
      }
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
