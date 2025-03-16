package database.logic;

import database.database_manager.DBManager;
import database.enums.MediaType;
import database.enums.TableNames;
import database.model.ArtistEntry;
import database.model.propertyModels.GameDataSet;
import database.model.propertyModels.StoryDataSet;
import database.model.propertyModels.VideoDataSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Logic {
    private static Logic instance;

    private ObservableList<GameDataSet> games;
    private ObservableList<StoryDataSet> stories;
    private ObservableList<VideoDataSet> videos;
    private ObservableList<String> genres;
    private ObservableList<String> tags;
    private ObservableList<String> artistsGames;
    private ObservableList<String> artistsStories;
    private ObservableList<String> artistsVideos;
    private ObservableList<String> platforms;


    protected static final String DB_Default_Name = "Data.db";
    protected static final String App_Default_PATH = Paths.get(System.getProperty("user.home"), "AppData", "Local", "AppDatabase").toString();
    protected static final String DB_Default_URL = "jdbc:sqlite:" + Paths.get(App_Default_PATH, DB_Default_Name).toString();

    protected static String DB_Name = DB_Default_Name;
    protected static String App_PATH = App_Default_PATH;
    protected static String DB_URL = DB_Default_URL;


    private Logic() {
        DBManager.initDefaultDatabase();
        //reloadData();
    }

    public static Logic getInstance() {
        if (instance == null) {
            instance = new Logic();
        }
        return instance;
    }


    // ----- Constructor Helper -----

    private void fillArtistLists() {
        artistsGames = FXCollections.observableArrayList();
        artistsStories = FXCollections.observableArrayList();
        artistsVideos = FXCollections.observableArrayList();
        for (ArtistEntry artist : DBManager.getArtists()) {
            switch (MediaType.valueOf(artist.getMediaType())) {
                case GAMES:
                    artistsGames.add(artist.getName());
                    break;
                case LITERATURE:
                    artistsStories.add(artist.getName());
                    break;
                case VIDEO:
                    artistsVideos.add(artist.getName());
                    break;
                default:
                    break;
            }
        }
    }

    /*
    public void reloadData() {
        games = DBManager.getGameDataSetCollection();
        stories = DBManager.getStoryDataSetCollection();
        videos = DBManager.getVideoDataSetCollection();
        genres = DBManager.getGenres();
        tags = DBManager.getTags();
        fillArtistLists();
        platforms = DBManager.getPlatforms();
    }

     */




    // -------------------------------------
    // ---------- Data Management ----------
    // -------------------------------------



    // ----- Database selection/creation -----

    public void openDatabase(String dbNamePath) {
        updatePaths(dbNamePath);
        DBManager.changeDatabase();
        //reloadData();
    }

    public void createNewDatabase(String dbNamePath) {
        updatePaths(dbNamePath);
        DBManager.createNewDatabase();
        //reloadData();
    }

    private void updatePaths(String dbNamePath) {
        Path path = Paths.get(dbNamePath);
        DB_Name = path.getFileName().toString();
        if (path.getParent() != null) App_PATH = path.getParent().toString();
        DB_URL = "jdbc:sqlite:" + Paths.get(App_PATH, DB_Name).toString();
    }



    // -------------------------------------------
    // ----- Add entries to database / logic -----
    // -------------------------------------------


    // ----- Add artists/genres/tags/platforms -----

    public void addPlatform(String platformName) {
        if (platformName == null || platforms.contains(platformName)) { return; }
        platforms.add(platformName);
        DBManager.addPlatform(platformName);
    }

    public void addTag(String tag) {
        if (tag == null || tags.contains(tag)) { return; }
        tags.add(tag);
        DBManager.addTag(tag);
    }

    public void addGenre(String genre) {
        if (genre == null || genres.contains(genre)) { return; }
        genres.add(genre);
        DBManager.addGenre(genre);
    }

    public void addArtistName(ArtistEntry artist) {
        if (artist.getName() == null) { return; }
        switch (MediaType.valueOf(artist.getMediaType())) {
            case GAMES:
                if (artistsGames.contains(artist.getName())) { return; }
                artistsGames.add(artist.getName());
                break;
            case LITERATURE:
                if (artistsStories.contains(artist.getName())) { return; }
                artistsStories.add(artist.getName());
                break;
            case VIDEO:
                if (artistsVideos.contains(artist.getName())) { return; }
                artistsVideos.add(artist.getName());
                break;
            default:
                break;
        }
        DBManager.addArtist(artist);
    }

    public void addStringToTable(String string, TableNames tableName) {
        if (tableName == TableNames.ARTIST) {
            throw new IllegalArgumentException("MediaType must be provided for ARTIST table");
        }
        addStringToTable(string, tableName, null);
    }

    public void addStringToTable(String string, TableNames tableName, MediaType mediaType) {
        switch (tableName) {
            case GENRE:
                addGenre(string);
                break;
            case TAG:
                addTag(string);
                break;
            case PLATFORM:
                addPlatform(string);
                break;
            case ARTIST:
                addArtistName(new ArtistEntry(-1, string, mediaType));
                break;
            default:
                break;
        }
    }


    // ----- Add game/story/video -----

    public void addGame(GameDataSet game) {
        //game.setId(String.valueOf(DBManager.addGame(game)));
        games.add(game);
    }

    public void addStory(StoryDataSet story) {
        //story.setId(String.valueOf(DBManager.addStory(story)));
        stories.add(story);
    }

    public void addVideo(VideoDataSet video) {
        //video.setId(String.valueOf(DBManager.addVideo(video)));
        videos.add(video);
    }





    // -----------------------------------------
    // ---------- Get data from logic ----------
    // -----------------------------------------


    // ----- Get artists/genres/tags/platforms -----

    public ObservableList<String> getArtistsGames() { return artistsGames; }

    public ObservableList<String> getArtistsStories() { return artistsStories; }

    public ObservableList<String> getArtistsVideos() { return artistsVideos; }

    public ObservableList<String> getGenres() { return genres; }

    public ObservableList<String> getTags() { return tags; }

    public ObservableList<String> getPlatforms() { return platforms; }


    // ----- Get games/stories/videos -----

    public ObservableList<GameDataSet> getGames() { return games; }

    public ObservableList<StoryDataSet> getStories() { return stories; }

    public ObservableList<VideoDataSet> getVideos() { return videos; }


    // ----- Get database path data -----

    public String getDBPath() { return App_PATH; }

    public String getDBName() { return DB_Name; }

}
