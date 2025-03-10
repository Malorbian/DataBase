package database.database_manager;

import database.model.StoryEntry;
import database.enums.Discipline;
import database.enums.State;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

class DBStories extends DBHelper {

    // Tabelle erstellen, falls sie noch nicht existiert
    protected static void createTable() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            enableForeignKey(conn);
            String sql = "CREATE TABLE IF NOT EXISTS stories (" +
                    "story_id INTEGER PRIMARY KEY AUTOINCREMENT , " +
                    "title TEXT NOT NULL, " +
                    "artist_id INTEGER NOT NULL, " +
                    "genre_id INTEGER, " +
                    "state TEXT, " +
                    "link TEXT, " +
                    "CONSTRAINT fk_author FOREIGN KEY (artist_id) REFERENCES artists(artist_id), " +
                    "CONSTRAINT fk_genre FOREIGN KEY (genre_id) REFERENCES genres(genre_id), " +
                    "CONSTRAINT unique_name_author UNIQUE (title, artist_id), " +
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
            int artistId = DBArtists.getArtistId(story.getArtist(), Discipline.STORIES, conn);
            int genreId = DBGenres.getGenreId(story.getGenre(), conn);

            // Insert game
            String sql = "INSERT INTO stories(title, artist_id, genre_id, state, link) VALUES(?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, story.getTitle());
                pstmt.setInt(2, artistId);
                pstmt.setInt(3, genreId);
                pstmt.setString(4, story.getState());
                pstmt.setString(5, story.getLink());
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<StoryEntry> getAllStories() {
        List<StoryEntry> stories = new ArrayList<>();
        String sql = "SELECT story_id, title, a.name, g.name, state, link FROM stories " +
                        "LEFT JOIN main.artists a on a.artist_id = stories.artist_id " +
                        "LEFT JOIN main.genres g on g.genre_id = stories.genre_id";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                stories.add(new StoryEntry(rs.getInt("story_id"),
                        rs.getString("title"),
                        rs.getString("a.name"),
                        rs.getString("g.name"),
                        State.valueOf(rs.getString("state")),
                        rs.getString("link")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stories;
    }

    protected static int getStoryId(String title, String artist) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            return getStoryId(title, artist, conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    protected static int getStoryId(String title, String artist, Connection conn) {
        String sql = "SELECT story_id FROM stories " +
                "JOIN artists ON stories.artist_id = artists.artist_id " +
                "WHERE LOWER(title) = LOWER(?) AND LOWER(artists.name) = LOWER(?)";
        return getIdHelper(title, artist, "story_id", sql, conn);

    }

}
