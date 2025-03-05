package database.database_manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

class DBVideos_Tags extends DBManager {

    private static DBVideos_Tags instance;

    private DBVideos_Tags() {
        createTable();
    }

    public static DBVideos_Tags getInstance() {
        if (instance == null) {
            instance = new DBVideos_Tags();
        }
        return instance;
    }

    // Tabelle erstellen, falls sie noch nicht existiert
    private void createTable() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            enableForeignKey(conn);
            String sql = "CREATE TABLE IF NOT EXISTS videos_tags (" +
                    "video_id INTEGER, " +
                    "tag_id INTEGER, " +
                    "CONSTRAINT fk_video_id FOREIGN KEY (video_id) REFERENCES videos(video_id), " +
                    "CONSTRAINT fk_tag_id FOREIGN KEY (tag_id) REFERENCES tags(tag_id), " +
                    "CONSTRAINT pk_video_id_tag_id PRIMARY KEY (video_id, tag_id)" +
                    ");";
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
