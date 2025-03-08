package database.database_manager;

import database.model.*;
import database.model.propertyModels.GameDataSet;
import database.model.propertyModels.StoryDataSet;
import database.model.propertyModels.VideoDataSet;

import java.io.File;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DBManager {
    protected static final String DB_Name = "Daten.db";
    protected static final String App_PATH = Paths.get(System.getProperty("user.home"), "AppData", "Local", "AppDatabase").toString();
    protected static final String DB_URL = "jdbc:sqlite:" + Paths.get(App_PATH, DB_Name).toString();

    static DBManager instance;

    protected DBManager() {
    }

    public static DBManager getInstance() {
        if (instance == null) {
            instance = new DBManager();
            createDatabase(App_PATH, DB_Name);
        }
        return instance;
    }



    // -------------------------------------------------
    // ---------- Database management methods ----------
    // -------------------------------------------------


    // ----- Add entries to database -----


    // Add artist
    public static void addArtist(ArtistEntry artist) {
        DBArtists.addArtist(artist);
    }

    // Add game
    public static int addGame(GameDataSet game) {
        // Add game
        DBGames.addGame(new GameEntry(game));

        // Add game_tags relation
        int game_id = DBGames.getGameId(game.getTitle(), game.getArtist());
        DBGames_Tags.addGameTagRelations(game_id, game.getTags());

        // Add ratings
        DBRatings.addRating(new RatingEntry(game_id, game.getRatings()));

        // Add played
        DBPlayed.addPlayedGame(new PlayedEntry(game_id, game.getLastPlayedDate(), game.getLastPlayedVersion()));

        return game_id;
    }

    public static boolean addPlatform (String platformName) {
        return DBRatings.addPlatform(platformName);
    }

    public static void addTag(String tag) {
        DBTags.addTag(tag);
    }

    public static void addGenre(String genre) {
        DBGenres.addGenre(genre);
    }


    // ----- Get entries from database -----


    // Get games from database
    public List<GameDataSet> getGameDataSetCollection() {

        List<GameDataSet> gameDataSets = new ArrayList<>();
        List<GameEntry> games = DBGames.getAllGames();

        for (GameEntry game : games) {
            List<String> tagList = DBGames_Tags.getTagsForGame_Id(game.getId());
            Map<String, String> ratings = DBRatings.getRatingsForGame_Id(game.getId());
            GameDataSet gameDataSet = new GameDataSet(game, ratings, tagList);
            gameDataSets.add(gameDataSet);
        }

        return gameDataSets;
    }

    // Get stories from database
    public List<StoryDataSet> getStoryDataSetCollection() {

        List<StoryDataSet> storyDataSets = new ArrayList<>();
        List<StoryEntry> stories = DBStories.getAllStories();

        for (StoryEntry story : stories) {
            List<String> tagList = DBStories_Tags.getTagsForStory_Id(story.getId());
            StoryDataSet storyDataSet = new StoryDataSet(story, tagList);
            storyDataSets.add(storyDataSet);
        }

        return storyDataSets;
    }

    // Get videos from database
    public List<VideoDataSet> getVideoDataSetCollection() {

        List<VideoDataSet> videoDataSets = new ArrayList<>();
        List<VideoEntry> videos = DBVideos.getAllVideos();

        for (VideoEntry video : videos) {
            List<String> tagList = DBVideos_Tags.getTagsForVideo_Id(video.getId());
            VideoDataSet videoDataSet = new VideoDataSet(video, tagList);
            videoDataSets.add(videoDataSet);
        }

        return videoDataSets;
    }

    // Get stories from database
    public static List<StoryDataSet> getStories() {
        // TODO
        return null;
    }

    // Get videos from database
    public static List<StoryDataSet> getVideos() {
        // TODO
        return null;
    }

    public List<ArtistEntry> getArtists() {
        return DBArtists.getAllArtists();
    }

    public List<String> getGenres() {
        return DBGenres.getAllGenres();
    }

    public List<String> getTags() {
        return DBTags.getAllTags();
    }

    public List<String> getPlatforms() {
        return DBRatings.getAllPlatforms();
    }

    // Create database
    public static void createDatabase(String path, String name) {
        File file = new File(path);
        // Check for directory
        if (!file.exists()) {
            if (file.mkdirs()) { // Erstellt das Verzeichnis (inklusive übergeordneter Verzeichnisse)
                System.out.println("Verzeichnis wurde erstellt.");
            } else {
                System.out.println("Fehler beim Erstellen des Verzeichnisses.");
            }
        }
        // Check for db
        file = new File(Paths.get(path, name).toString());
        try {
            if (file.createNewFile()) {
                System.out.println("Database created: " + file.getName());
                firstDBSetup();
            } else {
                System.out.println("Database already exists.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    // ------------------------------------
    // ---------- Helper methods ----------
    // ------------------------------------


    // Enable foreign key support
    protected static void enableForeignKey(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON;");
        }
    }

    // helper for getting tags for id
    protected static List<String> getTagsForIdHelper(String sql, int id) {
        List<String> tags = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                tags.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tags;
    }



    // --------------------------
    // ----- First DB Setup -----
    // --------------------------

    // Initialize all tables in the database
    private static void firstDBSetup() {
        DBArtists.createTable();
        DBGenres.createTable();
        DBGames.createTable();
        DBStories.createTable();
        DBVideos.createTable();
        DBTags.createTable();
        DBPlayed.createTable();
        DBRatings.createTable();
        DBGames_Tags.createTable();
        DBStories_Tags.createTable();
        DBVideos_Tags.createTable();
    }
}
