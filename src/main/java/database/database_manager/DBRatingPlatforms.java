package database.database_manager;

import database.enums.MediaType;

import java.sql.Connection;

class DBRatingPlatforms extends DBHelper{

    static final String createRatingPlatformTableSQL = "CREATE TABLE IF NOT EXISTS rating_platforms (" +
            "ratingPlatform_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "name TEXT NOT NULL, " +
            "CONSTRAINT unique_name UNIQUE (name)" +
            ");";

    static final String createRatingPlatform_MediaTypeTableSQL = "CREATE TABLE IF NOT EXISTS ratingPlatforms_mediaTypes (" +
            "ratingPlatform_id INTEGER, " +
            "mediaType_id INTEGER, " +
            "CONSTRAINT fk_mediaType_id FOREIGN KEY (mediaType_id) REFERENCES media_types(mediaType_id), " +
            "CONSTRAINT fk_ratingPlatform_id FOREIGN KEY (ratingPlatform_id) REFERENCES rating_platforms(ratingPlatform_id), " +
            "CONSTRAINT pk_ratingPlatform_id_mediaType_id PRIMARY KEY (mediaType_id, ratingPlatform_id)" +
            ");";


    static void addRatingPlatform(String ratingPlatform, MediaType mediaType) {
        String addEntrySQL = "INSERT OR IGNORE INTO ratingPlatforms (name) VALUES (?)";
        String mediaTypeRelationSql = "INSERT OR IGNORE INTO ratingPlatforms_mediaTypes(ratingPlatforms_id, mediaType_id) VALUES (?, ?)";
        addEntryByString(ratingPlatform, mediaType, addEntrySQL, mediaTypeRelationSql);
    }

    static int getRatingPlatformId(String ratingPlatform, int mediaType_Id, Connection conn) {
        String sql = "SELECT ratingPlatform_id FROM ratingPlatforms " +
                "LEFT JOIN mediaTypes_ratingPlatforms ON mediaTypes_ratingPlatforms.ratingPlatform_id = ratingPlatforms.ratingPlatform_id " +
                "WHERE LOWER(name) = LOWER(?) AND mediaType_id = " + mediaType_Id;
        return getIdByString(sql, ratingPlatform, conn);
    }

    static int getRatingPlatformId(String ratingPlatform, Connection conn) {
        String sql = "SELECT ratingPlatform_id FROM ratingPlatforms " +
                "WHERE LOWER(name) = LOWER(?)";
        return getIdByString(sql, ratingPlatform, conn);
    }

}
