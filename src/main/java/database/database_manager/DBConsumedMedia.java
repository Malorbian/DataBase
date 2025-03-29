package database.database_manager;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

class DBConsumedMedia extends DBHelper {

    static final String createTableSQL = "CREATE TABLE IF NOT EXISTS consumed (" +
            "medium_id INTEGER, " +
            "date TEXT, " +
            "version TEXT NOT NULL, " +
            "CONSTRAINT fk_medium_id FOREIGN KEY (medium_id) REFERENCES media_entries(entry_id), " +
            "CONSTRAINT pk_medium_id PRIMARY KEY (medium_id)" +
            ");";

    static void addConsumedMedium(int id, LocalDate date, String version, Connection conn) {
        try {
            String sql = "INSERT INTO consumed(medium_id, date, version) VALUES(?, ?, ?) " +
                    "ON CONFLICT(medium_id) DO UPDATE SET date = excluded.date, version = excluded.version;";
            try (java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                ps.setString(2, date.toString());
                ps.setString(3, version);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
