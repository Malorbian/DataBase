package database.database_manager;

import database.model.RatingEntry;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

class DBRatings extends DBHelper {

    static final String createTableSQL = "CREATE TABLE IF NOT EXISTS ratings (" +
            "medium_id INTEGER, " +
            "ratingPlatform_id INTEGER, " +
            "rating TEXT, " +
            "CONSTRAINT fk_medium_id FOREIGN KEY (medium_id) REFERENCES media_entries(entry_id), " +
            "CONSTRAINT fk_platform_id FOREIGN KEY (ratingPlatform_id) REFERENCES rating_platforms(ratingPlatform_id), " +
            "CONSTRAINT pk_game_id_platform_id PRIMARY KEY (medium_id, platform_id)" +
            ");";


    static void addRating(RatingEntry rating, Connection conn) {
        String sql = "INSERT INTO ratings(medium_id, ratingPlatform_id, rating) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (Map.Entry<String, String> entry : rating.getRatings().entrySet()) {
                int platformId = DBRatingPlatforms.getRatingPlatformId(entry.getKey(), conn);
                pstmt.setInt(1, rating.getMediumId());
                pstmt.setInt(2, platformId);
                pstmt.setString(3, entry.getValue());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
