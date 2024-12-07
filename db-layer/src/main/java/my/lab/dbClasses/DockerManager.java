package my.lab.dbClasses;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class DockerManager {
    private static final String DOCKER_COMPOSE_UP = "docker-compose up -d";
    private static final String DOCKER_COMPOSE_STOP = "docker-compose stop";
    private static final String DOCKER_PS = "docker ps -a";
    private static final String DOCKER_INSPECT = "docker inspect --format={{.State.Running}}";
    private static final String CONTAINER_NAME = "clickhouse-server";
    private static final String DOCKER_COMPOSE_PATH = "./db-layer/src/main/resources";

    public static void startDockerCompose() {
        Integer containerState = getContainerState(CONTAINER_NAME);
        if (containerState == null) {
            System.out.println("Контейнер не существует. Запуск Docker Compose...");
            executeCommand(DOCKER_COMPOSE_UP);
        } else if (containerState == 0) {
            System.out.println("Контейнер существует, но остановлен. Запуск контейнера...");
            executeCommand("docker start " + CONTAINER_NAME);
        } else {
            System.out.println("Контейнер уже существует и запущен.");
        }
    }

    public static void stopDockerCompose() {
        System.out.println("Остановка Docker Compose...");
        executeCommand(DOCKER_COMPOSE_STOP);
    }

    /**
     * Проверяет, существует ли контейнер, и его состояние.
     *
     * @param containerName имя контейнера
     * @return null - контейнер не существует, 0 - контейнер остановлен, 1 - контейнер запущен
     */
    private static Integer getContainerState(String containerName) {
        try {
            // Проверка существования контейнера
            ProcessBuilder processBuilder = new ProcessBuilder(DOCKER_PS.split(" "));
            processBuilder.directory(new File(DOCKER_COMPOSE_PATH));
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();

            boolean containerExists = false;
            try (Scanner scanner = new Scanner(process.getInputStream())) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (line.contains(containerName)) {
                        containerExists = true;
                        break;
                    }
                }
            }

            process.waitFor();

            if (!containerExists) {
                return null; // Контейнер не существует
            }

            // Проверка состояния контейнера
            processBuilder = new ProcessBuilder((DOCKER_INSPECT + " " + containerName).split(" "));
            processBuilder.directory(new File(DOCKER_COMPOSE_PATH));
            process = processBuilder.start();

            try (Scanner scanner = new Scanner(process.getInputStream())) {
                if (scanner.hasNextLine()) {
                    String state = scanner.nextLine().trim();
                    return "true".equals(state) ? 1 : 0; // true -> запущен, false -> остановлен
                }
            }

            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return null; // Возврат null в случае ошибки
    }

    private static void executeCommand(String command) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
            processBuilder.directory(new File(DOCKER_COMPOSE_PATH));
            processBuilder.inheritIO();
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Команда успешно выполнена: " + command);
            } else {
                System.err.println("Команда завершилась с ошибкой: " + command);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
