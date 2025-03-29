package database.database_manager;

import database.enums.MediaType;
import database.enums.State;
import database.model.DataSet;

import java.sql.*;
import java.util.Arrays;
import java.util.stream.Collectors;

class DBMediaEntries extends DBHelper {


    // ----- Add methods -----

    static int addEntry(DataSet entry, MediaType mediaType, Connection conn) {
        String addEntrySql = "INSERT INTO media_entries(mediaType_id, entryType_id, franchise_id, title, artist_id, genre_id, state, link, storagePath, length) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(addEntrySql, Statement.RETURN_GENERATED_KEYS)) {

            int mediaTypeId = DBMediaTypes.getMediaTypeId(mediaType, conn);
            int entryTypeId = DBEntryTypes.getEntryTypeId(entry.getEntryType(), mediaTypeId, conn);
            int franchiseId = DBFranchises.getFranchiseId(entry.getFranchise(), entryTypeId, conn);
            int artistId = DBArtists.getArtistId(entry.getArtist(), mediaTypeId, conn);
            int genreId = DBGenres.getGenreId(entry.getGenre(), mediaTypeId, conn);

            ps.setInt(1, mediaTypeId);
            ps.setInt(2, entryTypeId);
            ps.setInt(3, franchiseId);
            ps.setString(4, entry.getTitle());
            ps.setInt(5, artistId);
            ps.setInt(6, genreId);
            ps.setString(7, entry.getState());
            ps.setString(8, entry.getLink());
            ps.setString(9, entry.getStoragePath());
            ps.setDouble(10, parseDouble(entry.getLength()));

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return -1;
    }


    // ----- Get methods -----

    static String getCreateTableSQL() {
        return "CREATE TABLE IF NOT EXISTS media_entries (" +
                "entry_id INTEGER PRIMARY KEY AUTOINCREMENT , " +
                "mediaType_id INTEGER NOT NULL, " +
                "entryType_id INTEGER, " +
                "franchise_id INTEGER, " +
                "title TEXT NOT NULL, " +
                "artist_id INTEGER NOT NULL, " +
                "genre_id INTEGER, " +
                "state TEXT, " +
                "link TEXT, " +
                "storagePath TEXT, " +
                "length REAL, " +
                "CONSTRAINT fk_mediaType FOREIGN KEY (mediaType_id) REFERENCES media_types(mediaType_id), " +
                "CONSTRAINT fk_entryType FOREIGN KEY (entryType_id) REFERENCES entry_types(type_id), " +
                "CONSTRAINT fk_artist FOREIGN KEY (artist_id) REFERENCES artists(artist_id), " +
                "CONSTRAINT fk_genre FOREIGN KEY (genre_id) REFERENCES genres(genre_id), " +
                "CONSTRAINT unique_name_artist_mediaType UNIQUE (mediaType_id, title, artist_id), " +
                "CONSTRAINT check_state CHECK(state IN (" + stateCheckHelper() + "))" +
                ");";
    }


    // ----- Helper methods -----

    private static String stateCheckHelper() {
        return Arrays.stream(State.values())
                .map(type -> "'" + type.name() + "'")
                .collect(Collectors.joining(","));
    }

    private static double parseDouble(String doubleString) {
        try {
            return Double.parseDouble(doubleString);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

}
