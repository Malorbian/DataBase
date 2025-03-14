package database.database_manager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

class DBTags extends DBHelper {

    // Tabelle erstellen, falls sie noch nicht existiert
    protected static void createTable() {
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
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            addTag(tag, conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addTag(String tag, Connection conn) {
        String sql = "INSERT OR IGNORE INTO tags (name) VALUES (?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tag);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<String> getAllTags() {
        ObservableList<String> tags = FXCollections.observableArrayList();
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
