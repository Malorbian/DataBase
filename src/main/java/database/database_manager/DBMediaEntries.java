package database.database_manager;

import database.enums.MediaType;
import database.enums.State;
import database.model.propertyModels.DataSet;
import database.model.propertyModels.GameDataSet;
import database.model.propertyModels.StoryDataSet;
import database.model.propertyModels.VideoDataSet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class DBMediaEntries extends DBHelper {


    // ----- Add methods -----

    public static <T extends DataSet> void addEntry(T entry) {
        try (Connection conn = DriverManager.getConnection(DB_URL)){
            String sql = "INSERT INTO media_entries(mediaType_id, entryType_id, title, artist_id, genre_id, state, link, storagePath) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                addEntryHelper(entry, ps, conn);

                int artistId = DBArtists.getArtistId(entry.getArtist(), MediaType.LITERATURE, conn);
                int genreId = DBGenres.getGenreId(entry.getGenre(), conn);

                ps.setString(3, entry.getTitle());
                ps.setInt(4, artistId);
                ps.setInt(5, genreId);
                ps.setString(6, entry.getState());
                ps.setString(7, entry.getLink());
                ps.setString(8, entry.getStoragePath());


                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // ----- Get methods -----

    static String getCreateTableSQL() {
        return "CREATE TABLE IF NOT EXISTS media_entries (" +
                "entry_id INTEGER PRIMARY KEY AUTOINCREMENT , " +
                "mediaType_id INTEGER NOT NULL, " +
                "entryType_id INTEGER, " +
                "title TEXT NOT NULL, " +
                "artist_id INTEGER NOT NULL, " +
                "genre_id INTEGER, " +
                "state TEXT, " +
                "link TEXT, " +
                "storagePath TEXT, " +
                "CONSTRAINT fk_mediaType FOREIGN KEY (mediaType_id) REFERENCES media_types(mediaType_id), " +
                "CONSTRAINT fk_entryType FOREIGN KEY (entryType_id) REFERENCES entry_types(type_id), " +
                "CONSTRAINT fk_artist FOREIGN KEY (artist_id) REFERENCES artists(artist_id), " +
                "CONSTRAINT fk_genre FOREIGN KEY (genre_id) REFERENCES genres(genre_id), " +
                "CONSTRAINT unique_name_artist_mediaType UNIQUE (mediaType_id, title, artist_id), " +
                "CONSTRAINT check_state CHECK(state IN (" + stateCheckHelper() + "))" +
                ");";
    }


    // ----- Helper methods -----

    private static <T extends DataSet> void addEntryHelper(T entry, PreparedStatement ps, Connection conn) throws SQLException {
        switch (getMediaType(entry)) {
            case LITERATURE:
                ps.setInt(1, DBMediaTypes.getMediaTypeId(String.valueOf(MediaType.LITERATURE), conn));
                ps.setInt(2, DBEntryTypes.getTypeId(((StoryDataSet) entry).getType(), conn));
                break;
            case GAMES:
                ps.setInt(1, DBMediaTypes.getMediaTypeId(String.valueOf(MediaType.GAMES), conn));
                ps.setInt(2, -1);
                break;
            case VIDEO:
                ps.setInt(1, DBMediaTypes.getMediaTypeId(String.valueOf(MediaType.VIDEO), conn));
                ps.setInt(2, DBEntryTypes.getTypeId(((VideoDataSet) entry).getType(), conn));
                break;
        }
    }

    private static <T extends DataSet> MediaType getMediaType(T entry) {
        return switch (entry) {
            case StoryDataSet ignored -> MediaType.LITERATURE;
            case GameDataSet ignored -> MediaType.GAMES;
            case VideoDataSet ignored -> MediaType.VIDEO;
            case null, default ->
                    throw new IllegalArgumentException("Unsupported entry type");
        };
    }

    private static String stateCheckHelper() {
        return Arrays.stream(State.values())
                .map(type -> "'" + type.name() + "'")
                .collect(Collectors.joining(","));
    }

}
