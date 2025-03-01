package database.database_manager;

import database.entry_manager.GameEntry;

import java.io.File;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManagerMain {
    private static final String DB_Name = "Daten.db";
    private static final String DB_URL = "jdbc:sqlite:" + DB_Name;
    private static DatabaseManagerMain instance;
    private static final String DB_PATH = Paths.get(System.getProperty("user.home"), "AppData", "Local", "AppDatabase").toString();


    private DatabaseManagerMain() {
        System.out.println(DB_PATH);
        createDatabase();
        //createTable();
    }

    public static DatabaseManagerMain getInstance() {
        if (instance == null) {
            instance = new DatabaseManagerMain();
        }
        return instance;
    }

    private void createDatabase() {
        File file = new File(DB_PATH);
        // Check for directory
        if (!file.exists()) {
            if (file.mkdirs()) { // Erstellt das Verzeichnis (inklusive übergeordneter Verzeichnisse)
                System.out.println("Verzeichnis wurde erstellt.");
            } else {
                System.out.println("Fehler beim Erstellen des Verzeichnisses.");
            }
        }
        // Check for db
        file = new File(Paths.get(DB_PATH, DB_Name).toString());
        try {
            if (file.createNewFile()) {
                System.out.println("Database created: " + file.getName());
            } else {
                System.out.println("Database already exists.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Tabelle erstellen, falls sie noch nicht existiert
    private void createTable() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS games (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "title TEXT NOT NULL, " +
                    "tags TEXT, " +
                    "imagePath TEXT" +
                    ");";
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Einen neuen Spieleintrag hinzufügen
    public void addGame(GameEntry game) {
        String sql = "INSERT INTO games(title, tags, imagePath) VALUES(?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, game.getTitle());
            pstmt.setString(2, game.getTags());
            pstmt.setString(3, game.getImagePath());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Alle Spieleinträge abrufen
    public List<GameEntry> getAllGames() {
        List<GameEntry> list = new ArrayList<>();
        String sql = "SELECT * FROM games";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                GameEntry game = new GameEntry(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("tags"),
                        rs.getString("imagePath")
                );
                list.add(game);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
