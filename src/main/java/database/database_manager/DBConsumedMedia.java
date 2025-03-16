package database.database_manager;

import database.model.ConsumedEntry;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

class DBConsumedMedia extends DBHelper {

    static final String createTableSQL = "CREATE TABLE IF NOT EXISTS consumed (" +
            "medium_id INTEGER, " +
            "date TEXT, " +
            "version TEXT NOT NULL, " +
            "CONSTRAINT fk_medium_id FOREIGN KEY (medium_id) REFERENCES media_entries(entry_id), " +
            "CONSTRAINT pk_medium_id PRIMARY KEY (medium_id)" +
            ");";


    public static void addConsumedMedium(ConsumedEntry played) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            addConsumedMedium(played, conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addConsumedMedium(ConsumedEntry consumed, Connection conn) {
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

    public static List<ConsumedEntry> getConsumedMedia() {
        List<ConsumedEntry> consumedMedia = new ArrayList<>();
        String sql = "SELECT * FROM played_games";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                consumedMedia.add(new ConsumedEntry(rs.getInt("game_id"),
                        rs.getString("date"),
                        rs.getString("version")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return consumedMedia;
    }
}
