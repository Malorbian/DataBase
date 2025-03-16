package database.database_manager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class DBGenres extends DBHelper {

    static final String createTableSQL = "CREATE TABLE IF NOT EXISTS genres (" +
            "genre_id INTEGER PRIMARY KEY AUTOINCREMENT , " +
            "name TEXT, " +
            "CONSTRAINT unique_name UNIQUE (name)" +
            ");";


    public static void addGenre(String genre) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            addGenre(genre, conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addGenre(String genre, Connection conn) {
        String sql = "INSERT OR IGNORE INTO genres (name) VALUES (?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, genre);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<String> getAllGenres() {
        ObservableList<String> genres = FXCollections.observableArrayList();
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

    protected static int getGenreId(String name, Connection conn) {

        String sql = "SELECT genre_id FROM genres WHERE LOWER(name) = LOWER(?)";
        try (PreparedStatement pStmt = conn.prepareStatement(sql)) {
            // Check if artist already exists
            pStmt.setString(1, name);

            ResultSet rs = pStmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("genre_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    protected static int checkAndAddGenre(String name, Connection conn) {
        int genreId = getGenreId(name, conn);
        if (genreId == -1) {
            addGenre(name, conn);
            genreId = getGenreId(name, conn);
        }
        return genreId;
    }

}
