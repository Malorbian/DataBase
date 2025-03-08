package database.database_manager;

import database.model.VideoEntry;
import database.enums.Discipline;
import database.enums.State;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

class DBVideos extends DBManager {

    // Tabelle erstellen, falls sie noch nicht existiert
    protected static void createTable() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            enableForeignKey(conn);
            String sql = "CREATE TABLE IF NOT EXISTS videos (" +
                    "video_id INTEGER PRIMARY KEY AUTOINCREMENT , " +
                    "title TEXT NOT NULL , " +
                    "artist_id INTEGER NOT NULL , " +
                    "genre_id INTEGER, " +
                    "state TEXT, " +
                    "link TEXT, " +
                    "length REAL, " +
                    "CONSTRAINT fk_artist FOREIGN KEY (artist_id) REFERENCES artists(artist_id), " +
                    "CONSTRAINT fk_genre FOREIGN KEY (genre_id) REFERENCES genres(genre_id), " +
                    "CONSTRAINT unique_name_artist_length UNIQUE (title, artist_id, length), " +
                    "CONSTRAINT check_state CHECK(state IN ('FIN','DEV','UNKNOWN'))" +
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
            int artistId = DBArtists.checkAndAddArtist(video.getArtist(), Discipline.VIDEOS, conn);
            int genreId = DBGenres.checkAndAddGenre(video.getGenre(), conn);

            // Insert game
            String sql = "INSERT INTO videos(title, artist_id, genre_id, state, link, length) VALUES(?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, video.getTitle());
                pstmt.setInt(2, artistId);
                pstmt.setInt(3, genreId);
                pstmt.setString(4, video.getState());
                pstmt.setString(4, video.getLink());
                pstmt.setDouble(5, video.getLength());
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<VideoEntry> getAllVideos() {
        List<VideoEntry> videos = new ArrayList<>();
        String sql = "SELECT video_id, title, a.name, g.name, state, link, length FROM videos " +
                        "LEFT JOIN main.artists a on a.artist_id = videos.artist_id " +
                        "LEFT JOIN main.genres g on g.genre_id = videos.genre_id";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                videos.add(new VideoEntry(rs.getInt("video_id"),
                        rs.getString("title"),
                        rs.getString("a.name"),
                        rs.getString("g.name"),
                        State.valueOf(rs.getString("state")),
                        rs.getString("link"),
                        rs.getDouble("length")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return videos;
    }
}
