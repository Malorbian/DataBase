package database.database_manager;

import database.model.GameEntry;
import database.enums.Discipline;
import database.enums.State;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

class DBGames extends DBHelper {

    // Tabelle erstellen, falls sie noch nicht existiert
    protected static void createTable() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            enableForeignKey(conn);
            String sql = "CREATE TABLE IF NOT EXISTS games (" +
                    "game_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "title TEXT NOT NULL, " +
                    "artist_id INTEGER NOT NULL, " +
                    "genre_id INTEGER, " +
                    "state TEXT, " +
                    "link TEXT, " +
                    "imagePath TEXT, " +
                    "CONSTRAINT fk_artist FOREIGN KEY (artist_id) REFERENCES artists(artist_id), " +
                    "CONSTRAINT fk_genre FOREIGN KEY (genre_id) REFERENCES genres(genre_id), " +
                    "CONSTRAINT unique_title_artist UNIQUE(title, artist_id), " +
                    "CONSTRAINT check_state CHECK(state IN ('FIN','DEV','UNKNOWN'))" +
                    ");";
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Einen neuen Spieleintrag hinzufügen
    public static void addGame(GameEntry game) {
        try (Connection conn = DriverManager.getConnection(DB_URL)){
            // Check if artist already exists
            int artisId = DBArtists.getArtistId(game.getArtist(), Discipline.GAMES, conn);
            int genreId = DBGenres.getGenreId(game.getGenre(), conn);

            // Insert game
            String sql = "INSERT INTO games(title, artist_id, genre_id, state, link, imagePath) VALUES(?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, game.getTitle());
                pstmt.setInt(2, artisId);
                pstmt.setInt(3, genreId);
                pstmt.setString(4, game.getState());
                pstmt.setString(5, game.getLink());
                pstmt.setString(6, game.getImagePath());
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Alle Spieleinträge abrufen
    public static List<GameEntry> getAllGames() {
        List<GameEntry> list = new ArrayList<>();
        String sql = "SELECT games.game_id, title, a.name AS artist_name, g.name AS genre_name, state, link, imagePath, pg.date, pg.version FROM games " +
            "LEFT JOIN main.artists a on a.artist_id = games.artist_id " +
                "LEFT JOIN main.genres g on g.genre_id = games.genre_id " +
                "LEFT JOIN main.played_games pg on games.game_id = pg.game_id";
        try (Connection conn = DriverManager.getConnection(DB_URL);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            int counter = 0;
            while (rs.next()) {
                GameEntry game = new GameEntry(rs.getInt("game_id"),
                        rs.getString("title"),
                        rs.getString("artist_name"),
                        rs.getString("genre_name"),
                        State.valueOf(rs.getString("state")),
                        rs.getString("link"),
                        rs.getString("imagePath"),
                        rs.getString("date"),
                        rs.getString("version")
                );
                list.add(game);
                counter++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    protected static int getGameId(String title, String artist) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            return getGameId(title, artist, conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    protected static int getGameId(String title, String artist, Connection conn) {
        String sql = "SELECT game_id FROM games " +
                        "JOIN artists ON games.artist_id = artists.artist_id " +
                        "WHERE LOWER(games.title) = LOWER(?) AND LOWER(artists.name) = LOWER(?)";
        return getIdHelper(title, artist, "game_id", sql, conn);
    }


}
