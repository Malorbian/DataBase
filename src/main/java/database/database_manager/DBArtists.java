package database.database_manager;

import database.entry_manager.ArtistEntry;
import database.entry_manager.GameEntry;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

class DBArtists extends DBManager {

    private static DBArtists instance;

    private DBArtists() {
        createTable();
    }

    public static DBArtists getInstance() {
        if (instance == null) {
            instance = new DBArtists();
        }
        return instance;
    }

    // Tabelle erstellen, falls sie noch nicht existiert
    private void createTable() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS artists (" +
                    "artist_id INTEGER PRIMARY KEY AUTOINCREMENT , " +
                    "name TEXT NOT NULL, " +
                    "discipline TEXT, " +
                    "CONSTRAINT unique_artist UNIQUE (name)" +
                    ");";
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addArtist(ArtistEntry artist) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO artists (name, discipline) VALUES (?, ?)")) {
            pstmt.setString(1, artist.getName());
            pstmt.setString(2, artist.getDiscipline());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addArtist(ArtistEntry artist, Connection conn) {
        try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO artists (name, discipline) VALUES (?, ?)")) {
            pstmt.setString(1, artist.getName());
            pstmt.setString(2, artist.getDiscipline());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<ArtistEntry> getAllArtists() {
        List<ArtistEntry> artists = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            String sql = "SELECT * FROM artists";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                artists.add(new ArtistEntry(rs.getString("name"), rs.getString("discipline")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return artists;
    }

    protected static void checkForArtist(String name, Connection conn) {
        String sql = "SELECT COUNT(*) FROM artists WHERE LOWER(name) = LOWER(?)";
        try (PreparedStatement checkStmt = conn.prepareStatement(sql)) {
            // Check if artist already exists
            checkStmt.setString(1, name);
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);

            // If artist does not exist, add artist
            if (count == 0) {
                addArtist(new ArtistEntry(name, "Games"), conn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}