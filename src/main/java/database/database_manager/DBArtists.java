package database.database_manager;

import database.enums.MediaType;
import database.model.ArtistEntry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

class DBArtists extends DBHelper {

    static final String createTableSQL = "CREATE TABLE IF NOT EXISTS artists (" +
            "artist_id INTEGER PRIMARY KEY AUTOINCREMENT , " +
            "mediaType_id INTEGER, " +
            "name TEXT NOT NULL, " +
            "CONSTRAINT fk_mediaType_id FOREIGN KEY (mediaType_id) REFERENCES media_types(mediaType_id), " +
            "CONSTRAINT unique_artist_mediaType UNIQUE (name, mediaType_id)" +
            ");";


    public static void addArtist(ArtistEntry artist) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            addArtist(artist, conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addArtist(ArtistEntry artist, Connection conn) {
        String sql = "INSERT INTO artists (mediaType_id, name) VALUES (?, ?)" +
                "ON CONFLICT(mediaType_id, name) DO UPDATE SET name = excluded.name, mediaType_id = excluded.mediaType_id;";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            int mediaTypeId = DBMediaTypes.getMediaTypeId(artist.getMediaType(), conn);
            pstmt.setInt(1, mediaTypeId);
            pstmt.setString(2, artist.getName());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<ArtistEntry> getAllArtists() {
        ObservableList<ArtistEntry> artists = FXCollections.observableArrayList();
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            String sql = "SELECT * FROM artists";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                artists.add(new ArtistEntry(rs.getInt("artist_id"),
                        rs.getString("name"),
                        MediaType.valueOf(rs.getString("mediaType_id"))));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return artists;
    }

    protected static int getArtistId(String name, MediaType mediaType, Connection conn) {

        String sql = "SELECT artist_id FROM artists WHERE LOWER(name) = LOWER(?) AND mediaType = ?";
        try (PreparedStatement pStmt = conn.prepareStatement(sql)) {
            // Check if artist already exists
            pStmt.setString(1, name);
            pStmt.setString(2, mediaType.toString());

            ResultSet rs = pStmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("artist_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

}