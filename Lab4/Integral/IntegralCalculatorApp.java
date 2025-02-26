package Integral;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

import java.io.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Integral.RecIntegral;

import Exceptions.InvalidInputException;

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

        // Кнопки работы с файлами

        JPanel buttonFilePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbConstant = new GridBagConstraints();
        gbConstant.fill = GridBagConstraints.HORIZONTAL;
        gbConstant.weightx = 1;

        JButton writeToFileButton = new JButton("Записать в файл");
        gbConstant.gridy = 0;
        gbConstant.gridx = 0;
        buttonFilePanel.add(writeToFileButton, gbConstant);

        JButton readFromFileButton = new JButton("Считать из файла");
        gbConstant.gridy = 0;
        gbConstant.gridx = 1;
        buttonFilePanel.add(readFromFileButton, gbConstant);

        JButton writeToBinaryFileButton = new JButton("Записать в бинарный файл");
        gbConstant.gridy = 0;
        gbConstant.gridx = 2;
        buttonFilePanel.add(writeToBinaryFileButton , gbConstant);

        JButton readFromBinaryFileButton = new JButton("Считать из бинарного файла");
        readFromBinaryFileButton.setPreferredSize(new Dimension(100, 25)); // Размер кнопки
        gbConstant.gridy = 0;
        gbConstant.gridx = 3;
        buttonFilePanel.add(readFromBinaryFileButton, gbConstant);

        // Добавление компонентов в окно

        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS)); // Вертикал

        northPanel.add(buttonFilePanel);
        northPanel.add(inputPanel);
        add(northPanel, BorderLayout.NORTH);
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

        readFromFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                readDataFromFile(1);
            }
        });

        writeToFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                writeDataToFile(1);
            }
        });

        writeToBinaryFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                writeDataToFile(2);
            }
        });

        readFromBinaryFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                readDataFromFile(2);
            }
        });
    }

    // Метод для добавления строки в таблицу
    private void addRow() {
        try {
            double lowerBound = Double.parseDouble(lowerBoundField.getText());
            double upperBound = Double.parseDouble(upperBoundField.getText());
            double step = Double.parseDouble(stepField.getText());

            validateInput(lowerBound, upperBound, step);

            tableModel.addRow(new Object[]{lowerBound, upperBound, step, null});

            recIntegral.add(new RecIntegral(lowerBound, upperBound, step)); //Добавление данных в новый объект
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Введите число", "Ошибка ввода", JOptionPane.ERROR_MESSAGE);
        } catch (InvalidInputException e) {
            // System.out.println("Invalid input: " + e.getMessage());
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ввода", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void validateInput(double lowerBound, double upperBound, double step) throws InvalidInputException {
      if (lowerBound == 0 || lowerBound == 1) {
        throw new InvalidInputException("Нижняя граница не может быть 0 или 1");
      }

      if (step <= 0) {
        throw new InvalidInputException("Шаг должен быть положительным");
      }

      if ((lowerBound < 0.000001 || lowerBound > 1000000) ||
        (upperBound < 0.000001 || upperBound > 1000000) ||
        (step < 0.000001 || step > 1000000)) {
        throw new InvalidInputException("Значения должны быть в диапазоне от 0.000001 до 1000000");
      }

      if (lowerBound >= upperBound) {
        throw new InvalidInputException("Верхняя граница должна больше выше нижней");
      }

      if (step > upperBound - lowerBound) {
        throw new InvalidInputException("Шаг должен быть меньше интервала");
      }
    }

    // Метод для удаления выделенной строки из таблицы
    private void removeRow() {
          int selectedRow = table.getSelectedRow();

          tableModel.removeRow(selectedRow);

          recIntegral.remove(selectedRow);
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

      Iterator<RecIntegral> recIntegralIterator = recIntegral.iterator();

      while (recIntegralIterator.hasNext()) {
        RecIntegral rec = recIntegralIterator.next(); // Получаем следующий элемент
        double lowerBound = rec.getLowerBound();
        double upperBound = rec.getUpperBound();
        double step = rec.getStep();
        double result = rec.getResult();

        tableModel.addRow(new Object[]{lowerBound, upperBound, step, result});
      }
    }

    private void readDataFromFile(int flag) {
      JFrame frame = new JFrame("Выбор файла с данными");

      JFileChooser fileChooser = new JFileChooser();
      // Устанавливаем заголовок
      int userSelection = fileChooser.showOpenDialog(frame);
      // Проверяем, был ли выбран файл
      if (userSelection == JFileChooser.APPROVE_OPTION) {
        tableModel.setRowCount(0);
        recIntegral.clear();
        File fileToOpen = fileChooser.getSelectedFile();
        if (flag == 1) {
          JSONparse(fileToOpen.getAbsolutePath());
        }
        if (flag == 2) {
          SerializeParse(fileToOpen.getAbsolutePath());
        }

      } else {
        System.out.println("Выбор файла отменен.");
      }
    }

    private void JSONparse(String filePath) {
      String regex = "\"(lowerBound|upperBound|step|result)\":\\s*(\\d+\\.\\d+)";


      double lowerBound = 0;
      double upperBound = 0;
      double step = 0;
      double result = 0;

      try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int counter = 0;
            // Читаем файл построчно
            while ((line = br.readLine()) != null) {
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(line);

                while (matcher.find()) {
                  counter++;
                  String key = matcher.group(1); // Имя ключа
                  double value = Double.parseDouble(matcher.group(2)); // Значение ключа

                  switch (key) {
                      case "lowerBound":
                          lowerBound = value;
                          break;
                      case "upperBound":
                          upperBound = value;
                          break;
                      case "step":
                          step = value;
                          break;
                      case "result":
                          result = value;
                          break;
                  }

                  if (counter == 4) {
                    tableModel.addRow(new Object[]{lowerBound, upperBound, step, result});

                    recIntegral.add(new RecIntegral(lowerBound, upperBound, step).setResult(result));

                    counter = 0;
                  }
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Обрабатываем возможные исключения
        }
    }

    private void SerializeParse(String filePath) {
    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
        @SuppressWarnings("unchecked") // Подавляем предупреждение о небезопасном приведении типов
        List<RecIntegral> deserializedList = (List<RecIntegral>) ois.readObject();
        recIntegral = deserializedList;
        loadNotes();
    } catch (IOException | ClassNotFoundException e) {
        e.printStackTrace();
    }
  }

    private void writeDataToFile(int flag) {
      JFrame frame = new JFrame("Сохранение в файл");

      JFileChooser fileChooser = new JFileChooser();
      // Устанавливаем заголовок
      int userSelection = fileChooser.showOpenDialog(frame);
      // Проверяем, был ли выбран файл
      if (userSelection == JFileChooser.APPROVE_OPTION) {
        File fileToOpen = fileChooser.getSelectedFile();
        if (flag == 1) {
          JSONstringify(fileToOpen.getAbsolutePath());
        }
        if (flag == 2) {
            SerializeStringify(fileToOpen.getAbsolutePath());
        }

        // JSONparse(fileToOpen.getAbsolutePath());
      } else {
        System.out.println("Выбор файла отменен.");
      }
    }

    private void JSONstringify(String filePath) {
      try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
        writer.write("[\n"); // Начало JSON массива

        for (int i = 0; i < recIntegral.size(); i++) {  // Получение записей из коллекции
            double lowerBound = recIntegral.get(i).getLowerBound();
            double upperBound = recIntegral.get(i).getUpperBound();
            double step = recIntegral.get(i).getStep();
            double result = recIntegral.get(i).getResult();

            // Форматируем запись в JSON
            writer.write("  {\n");
            writer.write("    \"lowerBound\": " + lowerBound + ",\n");
            writer.write("    \"upperBound\": " + upperBound + ",\n");
            writer.write("    \"step\": " + step + ",\n");
            writer.write("    \"result\": " + result + "\n");
            writer.write("  }");

            // Если это не последняя запись, добавляем запятую
            if (i < recIntegral.size() - 1) {
                writer.write(",\n");
            } else {
                writer.write("\n");
            }
        }

        writer.write("]"); // Конец JSON массива
      } catch (IOException e) {
        e.printStackTrace(); // Обрабатываем возможные исключения
      }
    }

    private void SerializeStringify(String filePath) {
      try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(recIntegral);
        } catch (IOException e) {
            e.printStackTrace();
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
