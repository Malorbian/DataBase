package database.database_manager;

import database.logic.Logic;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.nio.file.Paths;
import java.sql.*;
import java.util.List;

public class DBHelper {

    protected static final String DB_Default_Name = "Daten.db";
    protected static final String App_Default_PATH = Paths.get(System.getProperty("user.home"), "AppData", "Local", "AppDatabase").toString();
    protected static final String DB_Default_URL = "jdbc:sqlite:" + Paths.get(App_Default_PATH, DB_Default_Name).toString();

    protected static String DB_Name = DB_Default_Name;
    protected static String App_PATH = App_Default_PATH;
    protected static String DB_URL = DB_Default_URL;

    // Change database
    protected static void changeDatabase() {
        Logic logic = Logic.getInstance();
        DB_Name = logic.getDBName();
        App_PATH = logic.getDBPath();
        DB_URL = "jdbc:sqlite:" + Paths.get(App_PATH, DB_Name).toString();
    }

    // Set database to default
    protected static void setDefaultDatabase() {
        DB_Name = DB_Default_Name;
        App_PATH = App_Default_PATH;
        DB_URL = DB_Default_URL;
    }

    // Enable foreign key support
    protected static void enableForeignKey(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON;");
        }
    }


    protected static int getIdHelper(String title, String artist, Double length, String columnLabel, String sql, Connection conn) {
        try (PreparedStatement pStmt = conn.prepareStatement(sql)) {
            // Check if artist already exists
            pStmt.setString(1, title);
            pStmt.setString(2, artist);
            if (length != -1.0) {
                pStmt.setDouble(3, length);
            }
            ResultSet rs = pStmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(columnLabel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;

    }

    protected static int getIdHelper(String title, String artist, String columnLabel, String sql, Connection conn) {
        return getIdHelper(title, artist, -1.0, columnLabel, sql, conn);
    }

    protected static int getIdByString(String sql, String name, Connection conn) {
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

    protected static void addEntryByString(String sql, String name) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected static void addEntriesByList(String sql, List<String> names) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (String platform_Name : names) {
                ps.setString(1, platform_Name);
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static ObservableList<String> getEntries(String sql) {
        ObservableList<String> entries = FXCollections.observableArrayList();
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                entries.add(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entries;
    }


}
