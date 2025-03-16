package database.database_manager;

import database.model.PlayedEntry;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

class DBPlayed extends DBHelper {

    static final String createTableSQL = "CREATE TABLE IF NOT EXISTS played_games (" +
            "game_id INTEGER, " +
            "date TEXT, " +
            "version TEXT NOT NULL, " +
            "CONSTRAINT fk_game_id FOREIGN KEY (game_id) REFERENCES games(game_id), " +
            "CONSTRAINT pk_game_id PRIMARY KEY (game_id)" +
            ");";


    public static void addPlayedGame(PlayedEntry played) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            addPlayedGame(played, conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addPlayedGame(PlayedEntry played, Connection conn) {
        try {
            String sql = "INSERT INTO played_games(game_id, date, version) VALUES(?, ?, ?) " +
                    "ON CONFLICT(game_id) DO UPDATE SET date = excluded.date, version = excluded.version;";
            try (java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, played.getId());
                pstmt.setString(2, played.getDate());
                pstmt.setString(3, played.getVersion());
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<PlayedEntry> getPlayedGames() {
        List<PlayedEntry> playedGames = new ArrayList<>();
        String sql = "SELECT * FROM played_games";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                playedGames.add(new PlayedEntry(rs.getInt("game_id"),
                        rs.getString("date"),
                        rs.getString("version")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return playedGames;
    }
}
