package database.database_manager;

import database.model.ConsumedEntry;

import java.sql.Connection;
import java.sql.SQLException;

class DBConsumedMedia extends DBHelper {

    static final String createTableSQL = "CREATE TABLE IF NOT EXISTS consumed (" +
            "medium_id INTEGER, " +
            "date TEXT, " +
            "version TEXT NOT NULL, " +
            "CONSTRAINT fk_medium_id FOREIGN KEY (medium_id) REFERENCES media_entries(entry_id), " +
            "CONSTRAINT pk_medium_id PRIMARY KEY (medium_id)" +
            ");";

    static void addConsumedMedium(ConsumedEntry consumed, Connection conn) {
        try {
            String sql = "INSERT INTO consumed(medium_id, date, version) VALUES(?, ?, ?) " +
                    "ON CONFLICT(medium_id) DO UPDATE SET date = excluded.date, version = excluded.version;";
            try (java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, consumed.getId());
                pstmt.setString(2, consumed.getDate());
                pstmt.setString(3, consumed.getVersion());
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
