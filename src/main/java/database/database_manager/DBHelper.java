package database.database_manager;

import database.enums.MediaType;
import database.logic.Logic;

import java.nio.file.Paths;
import java.sql.*;

class DBHelper {

    protected static final String DB_Default_Name = "Daten.db";
    protected static final String App_Default_PATH = Paths.get(System.getProperty("user.home"), "AppData", "Local", "AppDatabase").toString();
    protected static final String DB_Default_URL = "jdbc:sqlite:" + Paths.get(App_Default_PATH, DB_Default_Name).toString();

    protected static String DB_Name = DB_Default_Name;
    protected static String App_PATH = App_Default_PATH;
    protected static String DB_URL = DB_Default_URL;

    // Change database
    static void changeDatabase() {
        Logic logic = Logic.getInstance();
        DB_Name = logic.getDBName();
        App_PATH = logic.getDBPath();
        DB_URL = "jdbc:sqlite:" + Paths.get(App_PATH, DB_Name).toString();
    }

    // Set database to default
    static void setDefaultDatabase() {
        DB_Name = DB_Default_Name;
        App_PATH = App_Default_PATH;
        DB_URL = DB_Default_URL;
    }

    // Enable foreign key support
    static void enableForeignKey(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON;");
        }
    }


    static int getIdByString(String sql, String name, Connection conn) {
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    static void addEntryByString(String entryName, MediaType mediaType, String addEntrySQL, String mediaTypeRelationSql) {

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = conn.prepareStatement(addEntrySQL, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement ps2 = conn.prepareStatement(mediaTypeRelationSql);) {

            // Add entry to genres table
            ps.setString(1, entryName);
            ps.executeUpdate();

            // Get the ids for relation table
            int entryId = -1;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    entryId = rs.getInt(1);
                }
            }
            if (entryId == -1) throw new SQLException("Entry not added");
            int mediaTypeId = DBMediaTypes.getMediaTypeId(mediaType, conn);

            // Add entry to genres_mediaTypes table
            ps2.setInt(1, entryId);
            ps2.setInt(2, mediaTypeId);
            ps2.executeUpdate();

        } catch (SQLException e) {
            System.out.println("SQLException: " + entryName);
            e.printStackTrace();

        }
    }





}
