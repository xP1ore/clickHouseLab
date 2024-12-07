package my.lab.MainApp;

import my.lab.GUIClasses.MouseDataHandler;
import my.lab.GUIClasses.MouseEventCollector;
import my.lab.dbClasses.DatabaseInitializer;
import my.lab.dbClasses.DockerManager;

import java.util.Scanner;

// подключится через браузер http://localhost:8123/play

public class MainApp {
    public static void main(String[] args) {
        try {
            // Запуск Docker Compose
            DockerManager.startDockerCompose();

            // Инициализация базы данных
            DatabaseInitializer.initializeSchema();

            // Сбор данных о движении мыши
            MouseEventCollector collector = new MouseEventCollector();
            collector.start();


            MouseDataHandler handler = new MouseDataHandler();

            Scanner scanner = new Scanner(System.in);
            boolean continueRunning = true;

            while (continueRunning) {
                System.out.println("Выберите запрос:");
                System.out.println("1: Общее количество движений");
                System.out.println("2: Фильтрация и группировка");
                System.out.println("3: Наибольшие движения");
                System.out.println("0: Выход");
                System.out.print("Ваш выбор: ");

                int queryOption = scanner.nextInt();

                switch (queryOption) {
                    case 1:
                        System.out.println("Общее количество движений: " + handler.getMouseMovementCount());
                        break;
                    case 2:
                        handler.getGroupedMovements().forEach(System.out::println);
                        break;
                    case 3:
                        System.out.println("Наибольшие движения: " + handler.getLargestMovements());
                        break;
                    case 0:
                        continueRunning = false;
                        System.out.println("Выход из программы...");
                        break;
                    default:
                        System.out.println("Неверный ввод. Пожалуйста, попробуйте снова.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } /*finally {
            DockerManager.stopDockerCompose();
        }*/
    }
}

