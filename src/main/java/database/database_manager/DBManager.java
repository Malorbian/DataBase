package database.database_manager;

import database.enums.MediaType;
import database.model.ArtistEntry;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

public class DBManager {

    // -----------------------------
    // ----- database creation -----
    // -----------------------------

    public static void initDefaultDatabase() {
        DBCreation.createDatabase();
    }

    public static void setDatabaseToDefault() {
        DBCreation.setDefaultDatabase();
    }






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
        DBPlayed.addPlayedGame(new PlayedEntry(game_id, game.getLastPlayedDate(), game.getLastPlayedVersion()));

        return game_id;
    }


    // ----- Add story to database -----

    public static int addStory(StoryDataSet story) {
        // Add story
        DBStories.addStory(new StoryEntry(story));

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



    // -------------------------------------
    // ----- Get entries from database -----
    // -------------------------------------


    // ----- Get artists/genres/tags/platforms -----

    public static ObservableList<ArtistEntry> getArtists() { return DBArtists.getAllArtists(); }

    public static ObservableList<String> getGenres() { return DBGenres.getAllGenres(); }

    public static ObservableList<String> getTags() { return DBTags.getAllTags(); }

    public static ObservableMap<MediaType, String[]> getPlatforms() { return DBRatingPlatforms.getAllRatingPlatforms(); }


    /*
    // ----- Get games from database -----

    public static ObservableList<GameDataSet> getGameDataSetCollection() {

        ObservableList<GameDataSet> gameDataSets = FXCollections.observableArrayList();
        List<GameEntry> games = DBGames.getAllGames();

        for (GameEntry game : games) {
            List<String> tagList = DBGames_Tags.getTagsForGame_Id(game.getId());
            Map<String, String> ratings = DBRatings.getRatingsForGame_Id(game.getId());
            GameDataSet gameDataSet = new GameDataSet(game, ratings, tagList);
            gameDataSets.add(gameDataSet);
        }

        return gameDataSets;
    }


    // ----- Get stories from database -----

    public static ObservableList<StoryDataSet> getStoryDataSetCollection() {

        ObservableList<StoryDataSet> storyDataSets = FXCollections.observableArrayList();
        List<StoryEntry> stories = DBStories.getAllStories();

        for (StoryEntry story : stories) {
            List<String> tagList = DBStories_Tags.getTagsForStory_Id(story.getId());
            StoryDataSet storyDataSet = new StoryDataSet(story, tagList);
            storyDataSets.add(storyDataSet);
        }

        return storyDataSets;
    }


    // ----- Get videos from database -----

    public static ObservableList<VideoDataSet> getVideoDataSetCollection() {

        ObservableList<VideoDataSet> videoDataSets = FXCollections.observableArrayList();
        List<VideoEntry> videos = DBVideos.getAllVideos();

        for (VideoEntry video : videos) {
            List<String> tagList = DBVideos_Tags.getTagsForVideo_Id(video.getId());
            VideoDataSet videoDataSet = new VideoDataSet(video, tagList);
            videoDataSets.add(videoDataSet);
        }

        return videoDataSets;
    }

     */





}
