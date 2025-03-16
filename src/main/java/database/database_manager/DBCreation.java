package database.database_manager;

import java.io.File;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

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
                creatingTables();
            } else {
                System.out.println("Database already exists.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void creatingTables() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            enableForeignKey(conn);

            stmt.executeUpdate(DBMediaTypes.createTableSQL);
            stmt.executeUpdate(DBArtists.createTableSQL);
            stmt.executeUpdate(DBGenres.createTableSQL);
            stmt.executeUpdate(DBMediaEntries.getCreateTableSQL());
            stmt.executeUpdate(DBTags.createTableSQL);
            stmt.executeUpdate(DBPlayed.createTableSQL);
            stmt.executeUpdate(DBRatings.createTableSQL);
            stmt.executeUpdate(DBMedia_Tags.createTableSQL);
            stmt.executeUpdate(DBRatingPlatforms.createTableSQL);
            stmt.executeUpdate(DBMediaTypes_RatingPlatforms.createTableSQL);

        } catch (SQLException e) {
            System.out.println("Creating tables failed.");
            e.printStackTrace();
        }
    }
}
