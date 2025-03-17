package database.database_manager;


import database.enums.MediaType;
import database.enums.State;
import database.logic.Logic;
import database.model.ArtistEntry;
import database.model.ConsumedEntry;
import database.model.LogicDataClass;
import database.model.RatingEntry;
import database.model.propertyModels.DataSet;
import database.model.propertyModels.GameDataSet;
import database.model.propertyModels.LiteratureDataSet;
import database.model.propertyModels.VideoDataSet;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.sql.*;
import java.util.*;

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
                    "me.title AS title, " +
                    "art.name AS artist, " +
                    "gen.name AS genre," +
                    "me.state AS state," +
                    "me.link AS link," +
                    "me.storagePath AS path, " +
                    "me.length AS length " +
                    "GROUP_CONCAT(DISTINCT tags.name ORDER BY tags.name, ', ') AS tags," +
                    "GROUP_CONCAT(DISTINCT rp.name || ':' || rat.rating ORDER BY rp.name, ', ') AS ratings," +
                    "con.date AS date, " +
                    "con.version AS version " +
                "FROM media_entries me " +
                "LEFT JOIN media_types mt ON me.mediaType_id = mt.mediaType_id " +
                "LEFT JOIN entry_types et ON me.entryType_id = et.type_id " +
                "LEFT JOIN artists art ON me.artist_id = art.artist_id " +
                "LEFT JOIN genres gen ON me.genre_id = gen.genre_id " +
                "LEFT JOIN media_entries_tags met ON me.entry_id = met.medium_id " +
                "LEFT JOIN tags ON met.tag_id = tags.tag_id " +
                "LEFT JOIN ratings rat ON me.entry_id = ratings.medium_id " +
                "LEFT JOIN rating_platforms rp ON rat.ratingPlatform_id = rp.ratingPlatform_id " +
                "LEFT JOIN consumed con ON me.entry_id = con.medium_id " +
                "GROUP BY me.entry_id";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(mediaEntrySQL)) {

            // Clear all lists
            logicDataClass.getMediaEntries().clear();
            logicDataClass.getEntryTypes().clear();
            logicDataClass.getArtists().clear();
            logicDataClass.getGenres().clear();
            logicDataClass.getTags().clear();
            logicDataClass.getRatingPlatforms().clear();

            // Initialize sets for entryTypes, artists, genres, tags, platforms
            Map<MediaType, Set<String>> entryTypes = new HashMap<>();
            Map<MediaType, Set<String>> artists = new HashMap<>();
            Map<MediaType, Set<String>> genres = new HashMap<>();
            Map<MediaType, Set<String>> tags = new HashMap<>();
            Map<MediaType, Set<String>> platforms = new HashMap<>();
            initSets(entryTypes, artists, genres, tags, platforms);

            // Add media entries to logicDataClass
            while (rs.next()) {
                int id = rs.getInt("id");
                String mediaType = rs.getString("mediaType");
                String entryType = rs.getString("entryType");
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
                String date = rs.getString("date");
                String version = rs.getString("version");

                // Add media entry to logicDataClass depending on mediaType and adding entryTypes, artists, genres, tags, platforms to sets
                switch (mediaType) {
                    case "GAME":
                        GameDataSet gds = new GameDataSet(id, entryType, title, artist, genre, state, link, path, length, tagList, ratings, date, version);
                        logicDataClass.getMediaEntries().get(MediaType.GAMES).add(gds);
                        addEntriesToSet(MediaType.GAMES, entryTypes, artists, genres, tags, platforms,
                                entryType, artist, genre, tagList, ratings);
                        break;
                    case "LITERATURE":
                        LiteratureDataSet sds = new LiteratureDataSet(id, entryType, title, artist, genre, state, link, path, length, tagList, ratings);
                        logicDataClass.getMediaEntries().get(MediaType.LITERATURE).add(sds);
                        addEntriesToSet(MediaType.LITERATURE, entryTypes, artists, genres, tags, platforms,
                                entryType, artist, genre, tagList, ratings);
                        break;
                    case "VIDEO":
                        VideoDataSet vds = new VideoDataSet(id, entryType, title, artist, genre, state, link, path, length, tagList, ratings);
                        logicDataClass.getMediaEntries().get(MediaType.LITERATURE).add(vds);
                        addEntriesToSet(MediaType.VIDEO, entryTypes, artists, genres, tags, platforms,
                                entryType, artist, genre, tagList, ratings);
                        break;
                }
            }

            // Add sets to logicDataClass
            addSetsToLogicData(entryTypes, artists, genres, tags, platforms, logicDataClass);

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

    private static void initSets(Map<MediaType, Set<String>> entryTypesMap,
                     Map<MediaType, Set<String>> artistsMap,
                     Map<MediaType, Set<String>> genresMap,
                     Map<MediaType, Set<String>> tagsMap,
                     Map<MediaType, Set<String>> platformsMap) {
        for (MediaType mediaType : MediaType.values()) {
            entryTypesMap.put(mediaType, new HashSet<>());
            artistsMap.put(mediaType, new HashSet<>());
            genresMap.put(mediaType, new HashSet<>());
            tagsMap.put(mediaType, new HashSet<>());
            platformsMap.put(mediaType, new HashSet<>());
        }
    }

    private static void addEntriesToSet(MediaType mediaType,
                                      Map<MediaType, Set<String>> entryTypesMap,
                                      Map<MediaType, Set<String>> artistsMap,
                                      Map<MediaType, Set<String>> genresMap,
                                      Map<MediaType, Set<String>> tagsMap,
                                      Map<MediaType, Set<String>> platformsMap,
                                      String type, String artist, String genre, List<String> tags, Map<String, String> ratings) {
        entryTypesMap.get(mediaType).add(type);
        artistsMap.get(mediaType).add(artist);
        genresMap.get(mediaType).add(genre);
        tagsMap.get(mediaType).addAll(tags);
        platformsMap.get(mediaType).addAll(ratings.keySet());
    }

    private static void addSetsToLogicData(Map<MediaType, Set<String>> entryTypesMap,
                                           Map<MediaType, Set<String>> artistsMap,
                                           Map<MediaType, Set<String>> genresMap,
                                           Map<MediaType, Set<String>> tagsMap,
                                           Map<MediaType, Set<String>> platformsMap,
                                           LogicDataClass logicDataClass) {
        addSetsToLogicDataHelper(entryTypesMap, logicDataClass.getEntryTypes());
        addSetsToLogicDataHelper(artistsMap, logicDataClass.getArtists());
        addSetsToLogicDataHelper(genresMap, logicDataClass.getGenres());
        addSetsToLogicDataHelper(tagsMap, logicDataClass.getTags());
        addSetsToLogicDataHelper(platformsMap, logicDataClass.getRatingPlatforms());
    }

    private static void addSetsToLogicDataHelper (Map<MediaType, Set<String>> entryTypesMap,
                                                  ObservableMap<MediaType, ObservableList<String>> logicMap) {
        for (Map.Entry<MediaType, Set<String>> entry : entryTypesMap.entrySet()) {
            MediaType mediaType = entry.getKey();
            Set<String> entryTypes = entry.getValue();
            for (String entryType : entryTypes) {
                logicMap.get(mediaType).add(entryType);
            }
        }
    }



    // -----------------------------------
    // ----- Add entries to database -----
    // -----------------------------------


    // ----- Add artists/genres/tags/platforms -----

    public static void addArtist(ArtistEntry artist) { DBArtists.addArtist(artist); }

    public static void addRatingPlatform(String platformName, MediaType mediaType) { DBRatingPlatforms.addRatingPlatform(platformName, mediaType); }

    public static void addTag(String tag, MediaType mediaType) { DBTags.addTag(tag, mediaType); }

    public static void addGenre(String genre, MediaType mediaType) { DBGenres.addGenre(genre, mediaType); }


    // ----- Add media_entry to database -----

    public static <T extends DataSet> int addMediaEntry(T dataSet, MediaType mediaType) {
        try (Connection conn = DriverManager.getConnection(Logic.getInstance().getDB_URL())) {

            conn.setAutoCommit(false);

            // Add media entry
            int entryID = DBMediaEntries.addEntry(dataSet, mediaType, conn);

            // Add media_tags relation
            DBMedia_Tags.addMediumTagRelations(entryID, dataSet.getTags(), conn);

            // Add ratings
            DBRatings.addRating(new RatingEntry(entryID, dataSet.getRatings()), conn);

            // Add played
            if (dataSet.getClass().isInstance(GameDataSet.class)) {
                GameDataSet gameDataSet = (GameDataSet) dataSet;
                DBConsumedMedia.addConsumedMedium(new ConsumedEntry(entryID, gameDataSet.getLastPlayedDate(), gameDataSet.getLastPlayedVersion()), conn);
            }

            conn.commit();

            return entryID;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

}
