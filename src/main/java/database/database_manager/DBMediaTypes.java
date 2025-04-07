package database.database_manager;

import database.enums.MediaType;

import java.sql.Connection;
import java.sql.PreparedStatement;

class DBMediaTypes extends DBHelper {

    static final String createTableSQL = "CREATE TABLE IF NOT EXISTS media_types (" +
            "mediaType_id integer PRIMARY KEY AUTOINCREMENT, " +
            "name TEXT NOT NULL, " +
            "CONSTRAINT unique_name UNIQUE (name)" +
            ");";

    static final String addEntrySQL = "INSERT OR IGNORE INTO media_types(name) VALUES (?)";


    static void addMediaTypes(Connection conn) {
        try (PreparedStatement ps = conn.prepareStatement(addEntrySQL)) {
            for (MediaType mediaType : MediaType.values()) {
                ps.setString(1, mediaType.toString());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static int getMediaTypeId(MediaType mediaTypeName, Connection conn) {
        String sql = "SELECT mediaType_id FROM media_types WHERE name = ?";
        return getIdByString(sql, mediaTypeName.toString(), conn);
    }

}
