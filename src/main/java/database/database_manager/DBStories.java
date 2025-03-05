package database.database_manager;

import database.entry_manager.StoryEntry;
import database.enums.State;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

class DBStories extends DBManager {

    private static DBStories instance;

    private DBStories() {
        createTable();
    }

    public static DBStories getInstance() {
        if (instance == null) {
            instance = new DBStories();
        }
        return instance;
    }

    // Tabelle erstellen, falls sie noch nicht existiert
    private void createTable() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            enableForeignKey(conn);
            String sql = "CREATE TABLE IF NOT EXISTS stories (" +
                    "story_id INTEGER PRIMARY KEY AUTOINCREMENT , " +
                    "title TEXT, " +
                    "author TEXT, " +
                    "genre TEXT, " +
                    "state TEXT, " +
                    "link TEXT, " +
                    "CONSTRAINT fk_author FOREIGN KEY (author) REFERENCES artists(name), " +
                    "CONSTRAINT fk_genre FOREIGN KEY (genre) REFERENCES genres(name), " +
                    "CONSTRAINT unique_name_author UNIQUE (title, author), " +
                    "CONSTRAINT check_state CHECK(state IN ('FIN','DEV','UNKNOWN'))" +
                    ");";
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Einen neuen Spieleintrag hinzufügen
    public static void addStory(StoryEntry story) {
        try (Connection conn = DriverManager.getConnection(DB_URL)){
            // Check if artist already exists
            DBArtists.checkForArtist(story.getAuthor(), conn);

            // Insert game
            String sql = "INSERT INTO stories(title, author, genre, state, link) VALUES(?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, story.getTitle());
                pstmt.setString(2, story.getAuthor());
                pstmt.setString(3, story.getGenre());
                pstmt.setString(4, story.getState());
                pstmt.setString(5, story.getLink());
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<StoryEntry> getAllStories() {
        List<StoryEntry> stories = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM stories")) {
            while (rs.next()) {
                stories.add(new StoryEntry(rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("genre"),
                        State.valueOf(rs.getString("state")),
                        rs.getString("link")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stories;
    }
}
