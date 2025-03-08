package database.database_manager;

import database.model.RatingEntry;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class DBRatings extends DBManager {

    // Tabelle erstellen, falls sie noch nicht existiert
    protected static void createTable() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            enableForeignKey(conn);
            String sql = "CREATE TABLE IF NOT EXISTS ratings (" +
                    "game_id INTEGER, " +
                    "personal TEXT, " +
                    "CONSTRAINT fk_game_id FOREIGN KEY (game_id) REFERENCES games(game_id), " +
                    "CONSTRAINT pk_game_id PRIMARY KEY (game_id)" +
                    ");";
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean addPlatform(String columnName) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            return addPlatform(columnName, conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean addPlatform(String columnName, Connection conn) {
        // Check if column name is valid
        boolean ret = false;
        if (!isValidColumnName(columnName)) {
            System.out.println("Invalid column name "+ columnName +" -> Regex: [a-z][a-z0-9_]*");
            return ret;
        }
        String sql = "ALTER TABLE ratings ADD COLUMN " + columnName + " TEXT";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            ret = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static void addRating(RatingEntry rating) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            addRating(rating, conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addRating(RatingEntry rating, Connection conn) {
        List<String> columnsToInsert = new ArrayList<>();
        List<String> placeholders = new ArrayList<>();
        List<String> values = new ArrayList<>();

        for (Map.Entry<String, String> entry : rating.getRatings().entrySet()) {
            columnsToInsert.add(entry.getKey());
            placeholders.add("?");
            values.add(entry.getValue());
        }

        System.out.println("columnsToInsert: " + String.join(", ", columnsToInsert));
        System.out.println("palceholders: " + String.join(", ", placeholders));
        System.out.println("values: " + String.join(", ", values));

        if (columnsToInsert.isEmpty()) {
            System.out.println("No valid columns to insert");
            return;
        }

        if (columnsToInsert.size() != values.size()) {
            throw new IllegalStateException("Spalten- und Werteanzahl stimmen nicht überein.");
        }

        String sql = "INSERT INTO ratings (game_id, " +
                String.join(", ", columnsToInsert) + ") " +
                "VALUES (?, " + String.join(", ", placeholders) + ")";

        System.out.println(sql);

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, rating.getGameId());
            System.out.println("gameId: " + rating.getGameId());
            for (int i = 0; i < values.size(); i++) {
                pstmt.setString(i + 2, values.get(i));
                System.out.println("value: " + values.get(i));
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getAllPlatforms() {
        String sql = "PRAGMA table_info(ratings)";
        List<String> platforms = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String platformName = rs.getString("name");
                if (!platformName.equals("game_id")) {
                    platforms.add(platformName);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return platforms;
    }

    public static Map<String, String> getRatingsForGame_Id(int gameId) {
        Map<String, String> ratings = new HashMap<>();
        // Get Platforms (Columns)
        List<String> platforms = getAllPlatforms();
        // Create a sql string from platform and gameId
        StringBuilder sql = new StringBuilder("SELECT ");
        for (String platform : platforms) {
            sql.append(platform).append(", ");
        }
        sql.delete(sql.length() - 2, sql.length());
        sql.append(" FROM ratings WHERE game_id = ?");

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            pstmt.setInt(1, gameId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {  // Assuming only one row per game_id
                for (String platform : platforms) {
                    String rating = rs.getString(platform);  // Get rating for each platform
                    ratings.put(platform, rating);  // Store in the map
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ratings;
    }

    private static boolean isValidColumnName(String columnName) {
        return columnName.matches("[a-z][a-z0-9_]*");
    }

}
