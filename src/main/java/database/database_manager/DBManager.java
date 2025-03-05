package database.database_manager;

import database.entry_manager.ArtistEntry;
import database.entry_manager.GameEntry;

import java.io.File;
import java.nio.file.Paths;
import java.sql.*;
import java.util.List;

public class DBManager {
    protected static final String DB_Name = "Daten.db";
    protected static final String App_PATH = Paths.get(System.getProperty("user.home"), "AppData", "Local", "AppDatabase").toString();
    protected static final String DB_URL = "jdbc:sqlite:" + Paths.get(App_PATH, DB_Name).toString();

    static DBManager instance;
    static DBArtists dbArtists;
    static DBGenres dbGenres;
    static DBGames dbGames;
    static DBStories dbStories;
    static DBVideos dbVideos;
    static DBTags dbTags;
    static DBPlayed dbPlayed;
    static DBRatings dbRatings;
    static DBGames_Tags dbGames_Tags;
    static DBStories_Tags dbStories_Tags;
    static DBVideos_Tags dbVideos_Tags;

    protected DBManager() {
    }

    public static DBManager getInstance() {
        if (instance == null) {
            instance = new DBManager();
            createDatabase();
            firstDBSetup();
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
    public static void addGame(GameEntry game) {
        DBGames.addGame(game);
    }


    // ----- Get entries from database -----
    public static List<GameEntry> getAllGames() {
        return DBGames.getAllGames();
    }




    // ------------------------------------
    // ---------- Helper methods ----------
    // ------------------------------------


    // Enable foreign key support
    protected void enableForeignKey(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON;");
        }
    }



    // -----------------------
    // ----- First Setup -----
    // -----------------------


    // Create database
    private static void createDatabase() {
        File file = new File(App_PATH);
        // Check for directory
        if (!file.exists()) {
            if (file.mkdirs()) { // Erstellt das Verzeichnis (inklusive übergeordneter Verzeichnisse)
                System.out.println("Verzeichnis wurde erstellt.");
            } else {
                System.out.println("Fehler beim Erstellen des Verzeichnisses.");
            }
        }
        // Check for db
        file = new File(Paths.get(App_PATH, DB_Name).toString());
        try {
            if (file.createNewFile()) {
                System.out.println("Database created: " + file.getName());
            } else {
                System.out.println("Database already exists.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Initialize all tables in the database
    private static void firstDBSetup() {
        dbArtists = DBArtists.getInstance();
        dbGenres = DBGenres.getInstance();
        dbGames = DBGames.getInstance();
        dbStories = DBStories.getInstance();
        dbVideos = DBVideos.getInstance();
        dbTags = DBTags.getInstance();
        dbPlayed = DBPlayed.getInstance();
        dbRatings = DBRatings.getInstance();
        dbGames_Tags = DBGames_Tags.getInstance();
        dbStories_Tags = DBStories_Tags.getInstance();
        dbVideos_Tags = DBVideos_Tags.getInstance();
    }
}
