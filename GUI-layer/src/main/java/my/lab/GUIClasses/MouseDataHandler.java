package my.lab.GUIClasses;

import my.lab.model.MouseMovementData;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MouseDataHandler {
    private static final String JDBC_URL = "jdbc:clickhouse://localhost:8123";
    private static final String USERNAME = "default";
    private static final String PASSWORD = "";

    public void saveMouseMovement(MouseMovementData data) {
        String insertQuery = "INSERT INTO mouse_movements (x, y, deltaX, deltaY, clientTimeStamp, button, target) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

            preparedStatement.setInt(1, data.getX());
            preparedStatement.setInt(2, data.getY());
            preparedStatement.setInt(3, data.getDeltaX());
            preparedStatement.setInt(4, data.getDeltaY());
            preparedStatement.setFloat(5, data.getClientTimeStamp());
            preparedStatement.setByte(6, data.getButton());
            preparedStatement.setString(7, data.getTarget());

            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public long getMouseMovementCount() {
        String countQuery = "SELECT COUNT(*) FROM mouse_movements";

        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(countQuery)) {

            if (resultSet.next()) {
                return resultSet.getLong(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<String> getGroupedMovements() {
        String groupQuery = "SELECT target, COUNT(*) FROM mouse_movements WHERE x < 1000 AND y < 1000 GROUP BY target";
        List<String> results = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(groupQuery)) {

            while (resultSet.next()) {
                results.add("Target: " + resultSet.getString(1) + ", Count: " + resultSet.getLong(2));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    public String getLargestMovements() {
        String largestQuery = "SELECT target, MAX(deltaX + deltaY) FROM mouse_movements GROUP BY target";

        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(largestQuery)) {

            if (resultSet.next()) {
                return "Target: " + resultSet.getString(1) + ", Max Delta: " + resultSet.getInt(2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "No data available.";
    }
}

