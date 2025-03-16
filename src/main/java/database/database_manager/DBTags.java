package database.database_manager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.List;

class DBTags extends DBHelper {

    static final String createTableSQL = "CREATE TABLE IF NOT EXISTS tags (" +
            "tag_id INTEGER PRIMARY KEY AUTOINCREMENT , " +
            "name TEXT, " +
            "CONSTRAINT unique_name UNIQUE (name)" +
            ");";

    static final String addEntrySQL = "INSERT INTO tags (name) VALUES (?)";


    public static void addTag(String tag) {
        addEntryByString(addEntrySQL, tag);
    }

    public static void addTag(List<String> tags) {
        addEntriesByList(addEntrySQL, tags);
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
