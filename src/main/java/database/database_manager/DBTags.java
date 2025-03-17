package database.database_manager;

import database.enums.MediaType;

import java.sql.Connection;

class DBTags extends DBHelper {

    static final String createTagsTableSQL = "CREATE TABLE IF NOT EXISTS tags (" +
            "tag_id INTEGER PRIMARY KEY AUTOINCREMENT , " +
            "name TEXT, " +
            "CONSTRAINT unique_name UNIQUE (name)" +
            ");";

    static final String createTag_MediaTypeTableSQL = "CREATE TABLE IF NOT EXISTS tags_mediaTypes (" +
            "tag_id INTEGER, " +
            "mediaType_id INTEGER, " +
            "CONSTRAINT fk_mediaType_id FOREIGN KEY (mediaType_id) REFERENCES media_types(mediaType_id), " +
            "CONSTRAINT fk_tag_id FOREIGN KEY (tag_id) REFERENCES tags(tag_id), " +
            "CONSTRAINT pk_tag_id_mediaType_id PRIMARY KEY (mediaType_id, tag_id)" +
            ");";

    
    static void addTag(String tag, MediaType mediaType) {
        String addEntrySQL = "INSERT OR IGNORE INTO tags (name) VALUES (?)";
        String mediaTypeRelationSql = "INSERT OR IGNORE INTO tags_mediaTypes(tags_id, mediaType_id) VALUES (?, ?)";
        addEntryByString(tag, mediaType, addEntrySQL, mediaTypeRelationSql);
    }

    static int getTagId(String tag, int mediaType_Id, Connection conn) {
        String sql = "SELECT tag_id FROM tags " +
                "LEFT JOIN mediaTypes_tags ON mediaTypes_tags.tag_id = tags.tag_id " +
                "WHERE LOWER(name) = LOWER(?) AND mediaType_id = " + mediaType_Id;
        return getIdByString(sql, tag, conn);
    }

}
