package database.database_manager;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

class DBRatings extends DBManager {

    private static DBRatings instance;

    private DBRatings() {
        createTable();
    }

    public static DBRatings getInstance() {
        if (instance == null) {
            instance = new DBRatings();
        }
        return instance;
    }

    // Tabelle erstellen, falls sie noch nicht existiert
    private void createTable() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            enableForeignKey(conn);
            String sql = "CREATE TABLE IF NOT EXISTS ratings (" +
                    "game_id INTEGER PRIMARY KEY, " +
                    "personal TEXT, " +
                    "CONSTRAINT fk_game_id FOREIGN KEY (game_id) REFERENCES games(game_id)" +
                    ");";
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addPlatform(String columnName) {
        // Check if column name is valid
        if (!isValidColumnName(columnName)) {
            System.out.println("Invalid column name. Regex: [a-z][a-z0-9_]*");
            return;
        }
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            String sql = "ALTER TABLE ratings ADD COLUMN " + columnName + " TEXT";
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getAllPlatforms() {
        String sql = "PRAGMA table_info(ratings)";
        List<String> platforms = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                platforms.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return platforms;
    }

    private boolean isValidColumnName(String columnName) {
        return columnName.matches("[a-z][a-z0-9_]*");
    }
}
