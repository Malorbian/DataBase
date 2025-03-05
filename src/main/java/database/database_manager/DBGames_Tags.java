package database.database_manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

class DBGames_Tags extends DBManager {

    private static DBGames_Tags instance;

    private DBGames_Tags() {
        createTable();
    }

    public static DBGames_Tags getInstance() {
        if (instance == null) {
            instance = new DBGames_Tags();
        }
        return instance;
    }

    // Tabelle erstellen, falls sie noch nicht existiert
    private void createTable() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            enableForeignKey(conn);
            String sql = "CREATE TABLE IF NOT EXISTS games_tags (" +
                    "game_id INTEGER, " +
                    "tag_id INTEGER, " +
                    "CONSTRAINT fk_game_id FOREIGN KEY (game_id) REFERENCES games(game_id), " +
                    "CONSTRAINT fk_tag_id FOREIGN KEY (tag_id) REFERENCES tags(tag_id), " +
                    "CONSTRAINT pk_game_id_tag_id PRIMARY KEY (game_id, tag_id)" +
                    ");";
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
