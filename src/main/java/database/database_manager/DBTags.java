package database.database_manager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

class DBTags extends DBManager {

    private static DBTags instance;

    private DBTags() {
        createTable();
    }

    public static DBTags getInstance() {
        if (instance == null) {
            instance = new DBTags();
        }
        return instance;
    }

    // Tabelle erstellen, falls sie noch nicht existiert
    private void createTable() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS tags (" +
                    "tag_id INTEGER PRIMARY KEY AUTOINCREMENT , " +
                    "name TEXT, " +
                    "CONSTRAINT unique_name UNIQUE (name)" +
                    ");";
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addTag(String tag) {
        String sql = "INSERT OR IGNORE INTO tags (name) VALUES (?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tag);
            pstmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addTag(String tag, Connection conn) {
        String sql = "INSERT OR IGNORE INTO tags (name) VALUES (?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tag);
            pstmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getAllTags() {
        List<String> tags = new ArrayList<>();
        String sql = "SELECT name FROM tags";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                tags.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tags;
    }
}
