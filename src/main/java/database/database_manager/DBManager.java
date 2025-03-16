package database.database_manager;


import database.enums.MediaType;
import database.enums.State;
import database.logic.Logic;
import database.model.ArtistEntry;
import database.model.LogicDataClass;
import database.model.propertyModels.GameDataSet;
import database.model.propertyModels.LiteratureDataSet;
import database.model.propertyModels.VideoDataSet;

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
                "LEFT JOIN rating_platforms rp ON rat.platform_id = rp.platform_id " +
                "LEFT JOIN consumed con ON me.entry_id = con.medium_id " +
                "GROUP BY me.entry_id";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(mediaEntrySQL)) {

            Map<MediaType, Set<String>> entryTypes = new HashMap<>();
            Map<MediaType, Set<String>> artists = new HashMap<>();
            Map<MediaType, Set<String>> genres = new HashMap<>();
            Map<MediaType, Set<String>> tags = new HashMap<>();
            Map<MediaType, Set<String>> platforms = new HashMap<>();
            initSets(entryTypes, artists, genres, tags, platforms);

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
        addSetsToLogicDataHelper(entryTypesMap, logicDataClass);
        addSetsToLogicDataHelper(artistsMap, logicDataClass);
        addSetsToLogicDataHelper(genresMap, logicDataClass);
        addSetsToLogicDataHelper(tagsMap, logicDataClass);
        addSetsToLogicDataHelper(platformsMap, logicDataClass);
    }

    private static void addSetsToLogicDataHelper (Map<MediaType, Set<String>> entryTypesMap, LogicDataClass logicDataClass) {
        for (Map.Entry<MediaType, Set<String>> entry : entryTypesMap.entrySet()) {
            MediaType mediaType = entry.getKey();
            Set<String> entryTypes = entry.getValue();
            for (String entryType : entryTypes) {
                logicDataClass.getEntryTypes().get(mediaType).add(entryType);
            }
        }
    }





    // -----------------------------------
    // ----- Add entries to database -----
    // -----------------------------------


    // ----- Add artists/genres/tags/platforms -----

    public static void addArtist(ArtistEntry artist) {
        DBArtists.addArtist(artist);
    }

    public static void addPlatform (String platformName) { DBRatingPlatforms.addRatingPlatform(platformName); }

    public static void addTag(String tag) { DBTags.addTag(tag); }

    public static void addGenre(String genre) { DBGenres.addGenre(genre); }

    /*
    // ----- Add game to database -----

    public static int addGame(GameDataSet game) {
        // Add game
        DBGames.addGame(new GameEntry(game));

        // Add game_tags relation
        int game_id = DBGames.getGameId(game.getTitle(), game.getArtist());
        DBGames_Tags.addGameTagRelations(game_id, game.getTags());

        // Add ratings
        DBRatings.addRating(new RatingEntry(game_id, game.getRatings()));

        // Add played
        DBConsumedMedia.addPlayedGame(new ConsumedEntry(game_id, game.getLastPlayedDate(), game.getLastPlayedVersion()));

        return game_id;
    }


    // ----- Add story to database -----

    public static int addStory(LiteratureDataSet story) {
        // Add story
        DBStories.addStory(new LiteratureEntry(story));

        // Add story_tags relation
        int story_id = DBStories.getStoryId(story.getTitle(), story.getArtist());
        DBStories_Tags.addStoryTagRelations(story_id, story.getTags());

        return story_id;
    }


    // ----- Add video to database -----

    public static int addVideo(VideoDataSet video) {
        // Add video
        DBVideos.addVideo(new VideoEntry(video));

        // Add video_tags relation
        int video_id = DBVideos.getVideoId(video.getTitle(), video.getArtist(), Double.parseDouble(video.getLength()));
        DBVideos_Tags.addVideoTagRelations(video_id, video.getTags());

        return video_id;
    }

     */


}
