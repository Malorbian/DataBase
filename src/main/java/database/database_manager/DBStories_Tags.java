package database.database_manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

class DBStories_Tags extends DBManager {

    private static DBStories_Tags instance;

    private DBStories_Tags() {
        createTable();
    }

    public static DBStories_Tags getInstance() {
        if (instance == null) {
            instance = new DBStories_Tags();
        }
        return instance;
    }

    // Tabelle erstellen, falls sie noch nicht existiert
    private void createTable() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            enableForeignKey(conn);
            String sql = "CREATE TABLE IF NOT EXISTS stories_tags (" +
                    "story_id INTEGER, " +
                    "tag_id INTEGER, " +
                    "CONSTRAINT fk_story_id FOREIGN KEY (story_id) REFERENCES stories(story_id), " +
                    "CONSTRAINT fk_tag_id FOREIGN KEY (tag_id) REFERENCES tags(tag_id), " +
                    "CONSTRAINT pk_story_id_tag_id PRIMARY KEY (story_id, tag_id)" +
                    ");";
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
