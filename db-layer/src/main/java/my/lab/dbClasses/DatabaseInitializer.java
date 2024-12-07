package my.lab.dbClasses;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    private static final String JDBC_URL = "jdbc:clickhouse://localhost:8123";
    private static final String USERNAME = "default";
    private static final String PASSWORD = "";

    public static void initializeSchema() throws SQLException {
        try {
            Class.forName("com.clickhouse.jdbc.ClickHouseDriver");
            try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
                 Statement statement = connection.createStatement()) {

                String schemaSQL = Files.readString(Paths.get("./db-layer/src/main/resources/schema.sql"));
                statement.execute(schemaSQL);

            } catch (IOException | RuntimeException | SQLException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            System.err.println("ClickHouse JDBC драйвер не найден: " + e.getMessage());
        }

    }
}

