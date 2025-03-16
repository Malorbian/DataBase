package database.database_manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBMediaTypes_RatingPlatforms extends DBHelper {

    static final String createTableSQL = "CREATE TABLE IF NOT EXISTS mediaTypes_ratingPlatforms (" +
            "mediaType_id INTEGER, " +
            "ratingPlatform_id INTEGER, " +
            "CONSTRAINT fk_mediaType_id FOREIGN KEY (mediaType_id) REFERENCES media_types(mediaType_id), " +
            "CONSTRAINT fk_ratingPlatform_id FOREIGN KEY (ratingPlatform_id) REFERENCES rating_platforms(platform_id), " +
            "CONSTRAINT pk_medium_id_tag_id PRIMARY KEY (mediaType_id, ratingPlatform_id)" +
            ");";


    static void addMediaTypeRatingPlatformRelation(String mediaType, String ratingPlatform) {
        String sql = "INSERT INTO mediaTypes_ratingPlatforms(mediaType_id, ratingPlatform_id) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            int mediaTypeId = DBMediaTypes.getMediaTypeId(mediaType, conn);
            int ratingPlatformId = DBRatingPlatforms.getRatingPlatformId(ratingPlatform, conn);
            ps.setInt(1, mediaTypeId);
            ps.setInt(2, ratingPlatformId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
