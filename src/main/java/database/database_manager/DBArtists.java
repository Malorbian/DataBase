package database.database_manager;

import database.model.ArtistEntry;

import java.sql.Connection;

class DBArtists extends DBHelper {

    static final String createArtistsTableSQL = "CREATE TABLE IF NOT EXISTS artists (" +
            "artist_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "name TEXT NOT NULL" +
            ");";

    static final String createArtist_MediaTypeTableSQL = "CREATE TABLE IF NOT EXISTS artists_mediaTypes (" +
            "artist_id INTEGER, " +
            "mediaType_id INTEGER, " +
            "CONSTRAINT fk_mediaType_id FOREIGN KEY (mediaType_id) REFERENCES media_types(mediaType_id), " +
            "CONSTRAINT fk_artist_id FOREIGN KEY (artist_id) REFERENCES artists(artist_id), " +
            "CONSTRAINT pk_artist_id_mediaType_id PRIMARY KEY (mediaType_id, artist_id)" +
            ");";


    static void addArtist(ArtistEntry artist) {
        String addEntrySQL = "INSERT OR IGNORE INTO artists (name) VALUES (?)";
        String mediaTypeRelationSql = "INSERT OR IGNORE INTO artists_mediaTypes(artists_id, mediaType_id) VALUES (?, ?)";
        addEntryByString(artist.getName(), artist.getMediaType(), addEntrySQL, mediaTypeRelationSql);
    }

    static int getArtistId(String artist, int mediaType_Id, Connection conn) {
        String sql = "SELECT artist_id FROM artists " +
                "LEFT JOIN mediaTypes_artists ON mediaTypes_artists.artist_id = artists.artist_id " +
                "WHERE LOWER(name) = LOWER(?) AND mediaType_id = " + mediaType_Id;
        return getIdByString(sql, artist, conn);
    }

}