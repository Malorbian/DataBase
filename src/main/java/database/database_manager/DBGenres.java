package database.database_manager;

import database.enums.MediaType;

import java.sql.Connection;

class DBGenres extends DBHelper {

    static final String createGenresTableSQL = "CREATE TABLE IF NOT EXISTS genres (" +
            "genre_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "name TEXT, " +
            "CONSTRAINT unique_name UNIQUE (name)" +
            ");";

    static final String createGenres_MediaTypeTableSQL = "CREATE TABLE IF NOT EXISTS genres_mediaTypes (" +
            "genre_id INTEGER, " +
            "mediaType_id INTEGER, " +
            "CONSTRAINT fk_mediaType_id FOREIGN KEY (mediaType_id) REFERENCES media_types(mediaType_id), " +
            "CONSTRAINT fk_genre_id FOREIGN KEY (genre_id) REFERENCES genres(genre_id), " +
            "CONSTRAINT pk_genre_id_mediaType_id PRIMARY KEY (mediaType_id, genre_id)" +
            ");";


    static void addGenre(String genre, MediaType mediaType) {
        String addEntrySQL = "INSERT OR IGNORE INTO genres (name) VALUES (?)";
        String mediaTypeRelationSql = "INSERT OR IGNORE INTO genres_mediaTypes(genres_id, mediaType_id) VALUES (?, ?)";
        addEntryByStringWithRelations(genre, mediaType, addEntrySQL, mediaTypeRelationSql);
    }

    static int getGenreId(String genre, int mediaType_Id, Connection conn) {
        String sql = "SELECT genre_id FROM genres " +
                    "LEFT JOIN mediaTypes_genres ON mediaTypes_genres.genre_id = genres.genre_id " +
                    "WHERE LOWER(name) = LOWER(?) AND mediaType_id = " + mediaType_Id;
        return getIdByString(sql, genre, conn);
    }

}
