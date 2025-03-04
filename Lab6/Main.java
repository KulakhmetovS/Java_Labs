import Integral.IntegralCalculatorApp;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;


public class Main {

    private static List<Process> processes = new ArrayList<>(); // Список для хранения процессов

    public static void main(String[] args) {
        new IntegralCalculatorApp().setVisible(true);

        final int NUM_CLIENTS = 7; // Количество клиентов

        for (int i = 0; i < NUM_CLIENTS; i++) {
            try {
                ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", "/home/sabir/Documents/Java/UDP/UDPClient.jar", String.valueOf(i), "UDPClient");
                processBuilder.inheritIO(); // Для перенаправления ввода/вывода
                Process process = processBuilder.start(); // Запуск клиента
                processes.add(process); // Сохранение процесса в список
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            for (Process process : processes) {
                process.destroy(); // Завершение каждого процесса
            }
        }));

    }
}
