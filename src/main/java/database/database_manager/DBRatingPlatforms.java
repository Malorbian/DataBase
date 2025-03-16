package database.database_manager;

import database.enums.MediaType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import java.sql.*;
import java.util.List;

public class DBRatingPlatforms extends DBHelper{

    static final String createTableSQL = "CREATE TABLE IF NOT EXISTS rating_platforms (" +
            "platform_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "name TEXT NOT NULL, " +
            "CONSTRAINT unique_name UNIQUE (name)" +
            ");";

    static final String addEntrySQL = "INSERT INTO rating_platforms(name) VALUES (?)";

    static void addRatingPlatform(String platform_Name) {
        addEntryByString(addEntrySQL, platform_Name);
    }

    static void addRatingPlatform(List<String> platform_Names) {
        addEntriesByList(addEntrySQL, platform_Names);
    }

    static ObservableMap<MediaType, String[]> getAllRatingPlatforms() {
        ObservableMap<MediaType, String[]> ratingPlatforms = FXCollections.observableHashMap();
        String sql = "SELECT mr.mediaType_id AS mediaType, " +
                "GROUP_CONCAT(rp.name, ', ') AS platform_names " +
                "FROM rating_platforms rp " +
                "LEFT JOIN mediaTypes_ratingPlatforms mr ON rp.platform_id = mr.ratingPlatform_id " +
                "GROUP BY mr.mediaType_id";

        try (Connection conn = DriverManager.getConnection(DB_URL);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                MediaType mediaType = MediaType.valueOf(rs.getString("mediaType"));
                String[] platformNames = rs.getString("platform_names").split(", ");
                ratingPlatforms.put(mediaType, platformNames);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ratingPlatforms;
    }

    static int getRatingPlatformId(String platform_Name) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            return getRatingPlatformId(platform_Name, conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    static int getRatingPlatformId(String platform_Name, Connection conn) {
        String sql = "SELECT platform_id FROM rating_platforms WHERE name = ?";
        return getIdByString(sql, platform_Name, conn);
    }

}
