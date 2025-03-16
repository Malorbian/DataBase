package database.database_manager;

import java.sql.*;
import java.util.List;

public class DBMediaTypes extends DBHelper {

    static final String createTableSQL = "CREATE TABLE IF NOT EXISTS media_types (" +
            "mediaType_id integer PRIMARY KEY AUTOINCREMENT, " +
            "name TEXT NOT NULL, " +
            "CONSTRAINT unique_name UNIQUE (name)" +
            ");";

    static final String addEntrySQL = "INSERT INTO media (name) VALUES (?)";


    static void addMediaType(String mediaTypeName) {
        addEntryByString(addEntrySQL, mediaTypeName);
    }

    static void addMediaType(List<String> mediaTypeNames) {
        addEntriesByList(addEntrySQL, mediaTypeNames);
    }

    static int getMediaTypeId(String mediaTypeName) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            return getMediaTypeId(mediaTypeName, conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    static int getMediaTypeId(String mediaTypeName, Connection conn) {
        String sql = "SELECT mediaType_id FROM media WHERE name = ?";
        return getIdByString(sql, mediaTypeName, conn);
    }
}
