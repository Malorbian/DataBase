package database.database_manager;

import database.model.RatingEntry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

class DBRatings extends DBHelper {

    static final String createTableSQL = "CREATE TABLE IF NOT EXISTS ratings (" +
            "medium_id INTEGER, " +
            "platform_id INTEGER, " +
            "rating TEXT, " +
            "CONSTRAINT fk_medium_id FOREIGN KEY (medium_id) REFERENCES media_entries(entry_id), " +
            "CONSTRAINT fk_platform_id FOREIGN KEY (platform_id) REFERENCES rating_platforms(platform_id), " +
            "CONSTRAINT pk_game_id_platform_id PRIMARY KEY (medium_id, platform_id)" +
            ");";


    protected static void addRating(RatingEntry rating) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            addRating(rating, conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected static void addRating(RatingEntry rating, Connection conn) {
        String sql = "INSERT INTO ratings(medium_id, platform_id, rating) VALUES (?, ?, ?)";
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

    /*
    protected static Map<String, String> getRatingsForGame_Id(int gameId) {
        Map<String, String> ratings = new HashMap<>();
        String sql = "SELECT rp.name AS platfrom_Name, rating FROM ratings " +
                "LEFT JOIN main.rating_platforms rp on rp.platform_id = ratings.platform_id " +
                "WHERE game_id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, gameId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                ratings.put(rs.getString("platform_Name"), rs.getString("rating"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ratings;
    }
     */

}
