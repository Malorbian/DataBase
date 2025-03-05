package database.database_manager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBGenres extends DBManager {

    private static DBGenres instance;

    private DBGenres() {
        createTable();
    }

    public static DBGenres getInstance() {
        if (instance == null) {
            instance = new DBGenres();
        }
        return instance;
    }

    // Tabelle erstellen, falls sie noch nicht existiert
    private void createTable() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS genres (" +
                    "genre_id INTEGER PRIMARY KEY AUTOINCREMENT , " +
                    "name TEXT, " +
                    "CONSTRAINT unique_name UNIQUE (name)" +
                    ");";
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addGenre(String genre) {
        String sql = "INSERT OR IGNORE INTO genres (name) VALUES (?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addGenre(String genre, Connection conn) {
        String sql = "INSERT OR IGNORE INTO genres (name) VALUES (?)";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getAllGenres() {
        List<String> genres = new ArrayList<>();
        String sql = "SELECT name FROM genres";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                genres.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return genres;
    }

}
