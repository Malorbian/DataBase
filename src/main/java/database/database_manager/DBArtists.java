package database.database_manager;

import database.enums.Discipline;
import database.model.ArtistEntry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

class DBArtists extends DBHelper {

    // Tabelle erstellen, falls sie noch nicht existiert
    protected static void createTable() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS artists (" +
                    "artist_id INTEGER PRIMARY KEY AUTOINCREMENT , " +
                    "name TEXT NOT NULL, " +
                    "discipline TEXT, " +
                    "CONSTRAINT unique_artist UNIQUE (name, discipline)" +
                    ");";
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addArtist(ArtistEntry artist) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            addArtist(artist, conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addArtist(ArtistEntry artist, Connection conn) {
        String sql = "INSERT INTO artists (name, discipline) VALUES (?, ?)" +
                "ON CONFLICT(name, discipline) DO UPDATE SET name = excluded.name, discipline = excluded.discipline;";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, artist.getName());
            pstmt.setString(2, artist.getDiscipline());
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
                        Discipline.valueOf(rs.getString("discipline"))));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return artists;
    }

    protected static int getArtistId(String name, Discipline discipline, Connection conn) {

        String sql = "SELECT artist_id FROM artists WHERE LOWER(name) = LOWER(?) AND discipline = ?";
        try (PreparedStatement pStmt = conn.prepareStatement(sql)) {
            // Check if artist already exists
            pStmt.setString(1, name);
            pStmt.setString(2, discipline.toString());

            ResultSet rs = pStmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("artist_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    protected static int checkAndAddArtist(String name, Discipline discipline, Connection conn) {
        int artistId = getArtistId(name, discipline, conn);
        if (artistId == -1) {
            addArtist(new ArtistEntry(-1, name, discipline), conn);
            artistId = getArtistId(name, discipline, conn);
        }
        return artistId;
    }

}