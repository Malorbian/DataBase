package database.logic;

import database.database_manager.DBManager;
import database.enums.MediaType;
import database.model.ArtistEntry;
import database.model.LogicDataClass;
import database.model.propertyModels.DataSet;
import javafx.collections.ObservableList;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Logic {
    private static Logic instance;

    private final LogicDataClass logicDataClass;

    protected static final String DB_Default_Name = "Data.db";
    protected static final String App_Default_PATH = Paths.get(System.getProperty("user.home"), "AppData", "Local", "AppDatabase").toString();
    protected static final String DB_Default_URL = "jdbc:sqlite:" + Paths.get(App_Default_PATH, DB_Default_Name).toString();

    protected static String DB_Name = DB_Default_Name;
    protected static String App_PATH = App_Default_PATH;
    protected static String DB_URL = DB_Default_URL;


    private Logic() {
        logicDataClass = new LogicDataClass();
        logicDataClass.loadData();
        //DBManager.initDefaultDatabase();
        //reloadData();
    }

    public static Logic getInstance() {
        if (instance == null) {
            instance = new Logic();
        }
        return instance;
    }





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

    public void loadDB() {
        logicDataClass.loadData();
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


    // ----- Add artist/genre/tag/platform -----

    public void addArtist(ArtistEntry artist) {
        logicDataClass.addArtist(artist);
    }

    public void addGenre(String genre, MediaType mediaType) {
        logicDataClass.addGenre(genre, mediaType);
    }

    public void addTag(String tag, MediaType mediaType) {
        logicDataClass.addTag(tag, mediaType);
    }

    public void addRatingPlatform(String ratingPlatform, MediaType mediaType) {
        logicDataClass.addRatingPlatform(ratingPlatform, mediaType);
    }

    // ----- Add meda entry -----

    public void addMediaEntry(DataSet entry, MediaType mediaType) {
        logicDataClass.addMediaEntry(entry, mediaType);
    }




    // -----------------------------------------
    // ---------- Get data from logic ----------
    // -----------------------------------------


    // ----- Get artists/genres/tags/platforms -----

    public ObservableList<String> getArtists(MediaType mediaType) {
        return logicDataClass.getArtists().get(mediaType);
    }

    public ObservableList<String> getGenres(MediaType mediaType) {
        return logicDataClass.getGenres().get(mediaType);
    }

    public ObservableList<String> getTags(MediaType mediaType) {
        return logicDataClass.getTags().get(mediaType);
    }

    public ObservableList<String> getPlatforms(MediaType mediaType) {
        return logicDataClass.getRatingPlatforms().get(mediaType);
    }


    // ----- Get media entries -----

    public ObservableList<DataSet> getMediaEntries(MediaType mediaType) {
        return logicDataClass.getMediaEntries().get(mediaType);
    }


    // ----- Get database path data -----

    public String getDBPath() { return App_PATH; }

    public String getDBName() { return DB_Name; }

    public String getDB_URL() { return DB_URL; }

}
