package database.database_manager;


import database.enums.MediaType;
import database.enums.State;
import database.logic.Logic;
import database.model.ArtistEntry;
import database.model.DataSet;
import database.model.LogicDataClass;
import database.model.RatingEntry;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBManager {

    // -------------------------------------------------
    // ---------- Database management methods ----------
    // -------------------------------------------------


    // ----- Change/Create database -----
    public static void changeDatabase() {
        DBHelper.changeDatabase();
    }

    public static void createNewDatabase() {
        DBHelper.changeDatabase();
        DBCreation.createDatabase();
    }



    // --------------------------------------------------------------
    // ---------- Load database data into logic data class ----------
    // --------------------------------------------------------------


    public static void loadDB(LogicDataClass logicDataClass) {
        String DB_URL = Logic.getInstance().getDB_URL();
        String mediaEntrySQL = "SELECT " +
                    "me.entry_id AS id, " +
                    "mt.name AS mediaType," +
                    "et.name AS entryType, " +
                    "fra.name AS franchise , " +
                    "me.title AS title, " +
                    "art.name AS artist, " +
                    "gen.name AS genre, " +
                    "me.state AS state, " +
                    "me.link AS link, " +
                    "me.storagePath AS path, " +
                    "me.length AS length, " +
                    "GROUP_CONCAT(DISTINCT tags.name ORDER BY tags.name, ', ') AS tags, " +
                    "GROUP_CONCAT(DISTINCT rp.name || ':' || rat.rating ORDER BY rp.name, ', ') AS ratings, " +
                    "con.date AS date, " +
                    "con.version AS version " +
                "FROM media_entries me " +
                "LEFT JOIN media_types mt ON me.mediaType_id = mt.mediaType_id " +
                "LEFT JOIN entry_types et ON me.entryType_id = et.entryType_id " +
                "LEFT JOIN franchises fra ON me.franchise_id = fra.franchise_id " +
                "LEFT JOIN artists art ON me.artist_id = art.artist_id " +
                "LEFT JOIN genres gen ON me.genre_id = gen.genre_id " +
                "LEFT JOIN mediaEntries_tags met ON me.entry_id = met.medium_id " +
                "LEFT JOIN tags ON met.tag_id = tags.tag_id " +
                "LEFT JOIN ratings rat ON me.entry_id = rat.medium_id " +
                "LEFT JOIN rating_platforms rp ON rat.ratingPlatform_id = rp.ratingPlatform_id " +
                "LEFT JOIN consumed con ON me.entry_id = con.medium_id " +
                "GROUP BY me.entry_id";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(mediaEntrySQL)) {

            // Init MediaTypes table
            DBMediaTypes.addMediaTypes(conn);

            // Clear all lists
            logicDataClass.clearLists();

            // Add media entries to logicDataClass
            while (rs.next()) {
                int id = rs.getInt("id");
                String mediaType = rs.getString("mediaType");
                String entryType = rs.getString("entryType");
                String franchise = rs.getString("franchise");
                String title = rs.getString("title");
                String artist = rs.getString("artist");
                String genre = rs.getString("genre");
                State state = State.valueOf(rs.getString("state"));
                String link = rs.getString("link");
                String path = rs.getString("path");
                double length;
                try {
                    length = Double.parseDouble(rs.getString("length"));
                } catch (NumberFormatException e) {
                    length = -1.0;
                }
                List<String> tagList = List.of(rs.getString("tags").split(", "));
                Map<String, String> ratings = arrayToMap(rs.getString("ratings").split(", "));
                String dateString = rs.getString("date");
                LocalDate date;
                try {
                    date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("dd.MM.yy"));
                } catch (NullPointerException e) {
                    date = null;
                }
                String version = rs.getString("version");

                // Add media entry to logicDataClass depending on mediaType and adding entryTypes, artists, genres, tags, platforms to sets
                MediaType mt = MediaType.valueOf(mediaType);
                DataSet dataSet = new DataSet(
                        id,
                        mt,
                        entryType,
                        franchise,
                        title,
                        artist,
                        genre,
                        state,
                        link,
                        path,
                        length,
                        date,
                        version,
                        tagList,
                        ratings);
                logicDataClass.getMediaEntries().get(mt).add(dataSet);
                logicDataClass.addEntryType(entryType, mt);
                logicDataClass.addFranchise(franchise, entryType, mt);
                logicDataClass.addArtist(new ArtistEntry(-1, artist, mt));
                logicDataClass.addGenre(genre, mt);
                for (String tag : tagList) {
                    logicDataClass.addTag(tag, mt);
                }
                for (String platform : ratings.keySet()) {
                    logicDataClass.addRatingPlatform(platform, mt);
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // ----- Load database Helper methods -----

    private static Map<String, String> arrayToMap(String[] array) {
        Map<String, String> map = new HashMap<>();
        for (String entry : array) {
            String[] split = entry.split(":");
            map.put(split[0], split[1]);
        }
        return map;
    }



    // -----------------------------------
    // ----- Add entries to database -----
    // -----------------------------------


    // ----- Add artists/genres/tags/platforms -----

    public static void addEntryType(String entryType, MediaType mediaType) { DBEntryTypes.addEntryType(entryType, mediaType); }

    public static void addFranchise(String franchise, String entryType, MediaType mediaType) { DBFranchises.addFranchise(franchise, entryType, mediaType); }

    public static void addArtist(ArtistEntry artist) { DBArtists.addArtist(artist); }

    public static void addRatingPlatform(String platformName, MediaType mediaType) { DBRatingPlatforms.addRatingPlatform(platformName, mediaType); }

    public static void addTag(String tag, MediaType mediaType) { DBTags.addTag(tag, mediaType); }

    public static void addGenre(String genre, MediaType mediaType) { DBGenres.addGenre(genre, mediaType); }


    // ----- Add media_entry to database -----

    public static int addMediaEntry(DataSet dataSet, MediaType mediaType) {
        try (Connection conn = DriverManager.getConnection(Logic.getInstance().getDB_URL())) {

            conn.setAutoCommit(false);

            // Add media entry
            int entryID = DBMediaEntries.addEntry(dataSet, mediaType, conn);

            // Add media_tags relation
            DBMedia_Tags.addMediumTagRelations(entryID, dataSet.getTags(), conn);

            // Add ratings
            DBRatings.addRating(new RatingEntry(entryID, dataSet.getRatings()), conn);

            // Add consumed
            LocalDate date = LocalDate.parse(dataSet.getConsumedDate());
            DBConsumedMedia.addConsumedMedium(entryID, date, dataSet.getConsumedVersion(), conn);


            conn.commit();

            return entryID;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

}
