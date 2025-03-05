package database.database_manager;

import database.entry_manager.VideoEntry;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

class DBVideos extends DBManager {

    private static DBVideos instance;

    private DBVideos() {
        createTable();
    }

    public static DBVideos getInstance() {
        if (instance == null) {
            instance = new DBVideos();
        }
        return instance;
    }

    // Tabelle erstellen, falls sie noch nicht existiert
    private void createTable() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            enableForeignKey(conn);
            String sql = "CREATE TABLE IF NOT EXISTS videos (" +
                    "video_id INTEGER PRIMARY KEY AUTOINCREMENT , " +
                    "title TEXT NOT NULL , " +
                    "artist TEXT NOT NULL , " +
                    "genre TEXT, " +
                    "link TEXT, " +
                    "length REAL, " +
                    "CONSTRAINT fk_artist FOREIGN KEY (artist) REFERENCES artists(name), " +
                    "CONSTRAINT fk_genre FOREIGN KEY (genre) REFERENCES genres(name), " +
                    "CONSTRAINT unique_name_artist_length UNIQUE (title, artist, length)" +
                    ");";
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Einen neuen Spieleintrag hinzufügen
    public static void addVideo(VideoEntry video) {
        try (Connection conn = DriverManager.getConnection(DB_URL)){
            // Check if artist already exists
            DBArtists.checkForArtist(video.getArtist(), conn);

            // Insert game
            String sql = "INSERT INTO videos(title, artist, genre, link, length) VALUES(?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, video.getTitle());
                pstmt.setString(2, video.getArtist());
                pstmt.setString(3, video.getGenre());
                pstmt.setString(4, video.getLink());
                pstmt.setDouble(5, video.getLength());
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<VideoEntry> getAllVideos() {
        List<VideoEntry> videos = new ArrayList<>();
        String sql = "SELECT title, artist, genre, link, length FROM videos";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                videos.add(new VideoEntry(rs.getString("title"),
                        rs.getString("artist"),
                        rs.getString("genre"),
                        rs.getString("link"),
                        rs.getDouble("length")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return videos;
    }
}
