package database.database_manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

class DBPlayed extends DBManager {

    private static DBPlayed instance;

    private DBPlayed() {
        createTable();
    }

    public static DBPlayed getInstance() {
        if (instance == null) {
            instance = new DBPlayed();
        }
        return instance;
    }

    // Tabelle erstellen, falls sie noch nicht existiert
    private void createTable() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            enableForeignKey(conn);
            String sql = "CREATE TABLE IF NOT EXISTS played_games (" +
                    "game_id INTEGER, " +
                    "date TEXT, " +
                    "version TEXT, " +
                    "CONSTRAINT fk_game_id FOREIGN KEY (game_id) REFERENCES games(game_id), " +
                    "CONSTRAINT fk_game_id_date PRIMARY KEY (game_id, date)" +
                    ");";
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
