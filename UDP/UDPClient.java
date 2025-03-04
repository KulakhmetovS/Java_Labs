import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import java.util.*;
import java.util.ArrayList;
import java.util.List;

import Runnable.TrapezoidCalculator;

public class UDPClient {
    public static double trapezoidMethod(double a, double b, double eps) {
      int numThreads = 7; // Количество потоков

      double intervalWidth = (b - a) / numThreads;  // Вычисление ширины интервала для каждого потока
      List<TrapezoidCalculator> calculators = new ArrayList<>();  // Экземпляры класса TrapezoidCalculator
      List<Thread> threads = new ArrayList<>(); // Потоки

      for (int i = 0; i < numThreads; i++) {
        // Обработка интервалов
          double start = a + i * intervalWidth;
          double end = (i == numThreads - 1) ? b : start + intervalWidth;
          TrapezoidCalculator calculator = new TrapezoidCalculator(start, end, eps);
          calculators.add(calculator);

          Thread thread = new Thread(calculator);
          threads.add(thread);
          thread.start();
      }

      // Ожидание завершения всех потоков
      threads.forEach(thread -> {
        try {
          thread.join();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      });

      double totalResult = 0.0;
      for (TrapezoidCalculator calculator : calculators) {
          totalResult += calculator.getResult();
      }

      return totalResult;
    }

    public static void main(String[] args) {
      final int BASE_PORT = 9000; // Базовый порт, на котором работает сервер

      int portOffset = 0; // Смещение порта по умолчанию
      if (args.length > 0) {
        portOffset = Integer.parseInt(args[0]); // Преобразование первого аргумента в целое число
      }
      int PORT = BASE_PORT + portOffset; // Вычисление итогового порта

        try (DatagramSocket socket = new DatagramSocket(PORT)) {  // Создание соединения
            System.out.println("Port listening...");

            double[] data;
            data = new double[3];

            while (true) {
                byte[] receiveBuffer = new byte[1024];  // Буфер для получения данных
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                socket.receive(receivePacket); // Получение пакета

                // Преобразование данных в строку
                String receivedData = new String(receivePacket.getData(), 0, receivePacket.getLength());

                String[] numbers = receivedData.split(" "); // Разделение строки на числа

                int i = 0;
                for (String number : numbers) {
                    data[i] = Double.parseDouble(number);
                    i++;
                }

                double result = trapezoidMethod(data[0], data[1], data[2]);  // Вычисление интеграла

                // Отправка результата обратно клиенту
                String response = Double.toString(result);;
                byte[] sendBuffer = response.getBytes();
                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();
                DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, clientAddress, clientPort);
                socket.send(sendPacket);

                System.out.println("Sended: " + response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
