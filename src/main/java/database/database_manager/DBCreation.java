package database.database_manager;

import java.io.File;
import java.nio.file.Paths;

public class DBCreation extends DBHelper{


    // ------------------------------------
    // ---------- Database Setup ----------
    // ------------------------------------


    // ----- Create database -----

    public static void createDatabase() {
        createDatabase(App_PATH, DB_Name);
    }

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
