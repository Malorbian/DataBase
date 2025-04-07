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

            conn.setAutoCommit(false);
            enableForeignKey(conn);

            stmt.executeUpdate(DBMediaTypes.createTableSQL);

            stmt.executeUpdate(DBArtists.createArtistsTableSQL);
            stmt.executeUpdate(DBArtists.createArtist_MediaTypeTableSQL);

            stmt.executeUpdate(DBGenres.createGenresTableSQL);
            stmt.executeUpdate(DBGenres.createGenres_MediaTypeTableSQL);

            stmt.executeUpdate(DBRatingPlatforms.createRatingPlatformTableSQL);
            stmt.executeUpdate(DBRatingPlatforms.createRatingPlatform_MediaTypeTableSQL);

            stmt.executeUpdate(DBEntryTypes.createEntryTypesTableSQL);
            stmt.executeUpdate(DBEntryTypes.createEntryTypes_MediaTypesTableSQL);

            stmt.executeUpdate(DBFranchises.createFranchisesTableSQL);
            stmt.executeUpdate(DBFranchises.createFranchises_EntryTypeTableSQL);

            stmt.executeUpdate(DBMediaEntries.getCreateTableSQL());

            stmt.executeUpdate(DBTags.createTagsTableSQL);
            stmt.executeUpdate(DBTags.createTag_MediaTypeTableSQL);

            stmt.executeUpdate(DBMediaEntries_Tags.createTableSQL);

            stmt.executeUpdate(DBConsumedMedia.createTableSQL);

            stmt.executeUpdate(DBRatings.createTableSQL);

            conn.commit();

        } catch (SQLException e) {
            System.out.println("Creating tables failed.");
            e.printStackTrace();
        }
    }
}
