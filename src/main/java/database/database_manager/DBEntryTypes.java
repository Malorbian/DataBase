package database.database_manager;

import database.enums.MediaType;

import java.sql.Connection;

class DBEntryTypes extends DBHelper{

    static final String createEntryTypesTableSQL = "CREATE TABLE IF NOT EXISTS entry_types (" +
            "entryType_id integer PRIMARY KEY AUTOINCREMENT, " +
            "name TEXT NOT NULL, " +
            "CONSTRAINT unique_name UNIQUE (name)" +
            ");";
    
    static final String createEntryTypes_MediaTypesTableSQL = "CREATE TABLE IF NOT EXISTS entryTypes_mediaTypes (" +
            "entryType_id integer, " +
            "mediaType_id integer, " +
            "CONSTRAINT fk_entryType_id FOREIGN KEY (entryType_id) REFERENCES entry_types(entryType_id), " +
            "CONSTRAINT fk_mediaType_id FOREIGN KEY (mediaType_id) REFERENCES media_types(mediaType_id), " +
            "CONSTRAINT pk_entryType_id_mediaType_id PRIMARY KEY (entryType_id, mediaType_id)" +
            ");";


    static void addEntryType(String entryType, MediaType mediaType) {
        String addEntrySQL = "INSERT OR IGNORE INTO entryTypes (name) VALUES (?)";
        String mediaTypeRelationSql = "INSERT OR IGNORE INTO entryTypes_mediaTypes(entryType_id, mediaType_id) VALUES (?, ?)";
        addEntryByStringWithRelations(entryType, mediaType, addEntrySQL, mediaTypeRelationSql);
    }

    static int getEntryTypeId(String entryType, int mediaType_Id, Connection conn) {
        String sql = "SELECT entryType_id FROM entryTypes " +
                "LEFT JOIN mediaTypes_entryTypes ON mediaTypes_entryTypes.entryType_id = entryTypes.entryType_id " +
                "WHERE LOWER(name) = LOWER(?) AND mediaType_id = " + mediaType_Id;
        return getIdByString(sql, entryType, conn);
    }
}
