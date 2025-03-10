package database.database_manager;

import database.enums.Discipline;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class DBGames_Tags extends DBHelper {

    // Tabelle erstellen, falls sie noch nicht existiert
    protected static void createTable() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            enableForeignKey(conn);
            String sql = "CREATE TABLE IF NOT EXISTS games_tags (" +
                    "game_id INTEGER, " +
                    "tag_id INTEGER, " +
                    "CONSTRAINT fk_game_id FOREIGN KEY (game_id) REFERENCES games(game_id), " +
                    "CONSTRAINT fk_tag_id FOREIGN KEY (tag_id) REFERENCES tags(tag_id), " +
                    "CONSTRAINT pk_game_id_tag_id PRIMARY KEY (game_id, tag_id)" +
                    ");";
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected static void addGameTagRelations(int game_id, List<String> tags) {
        addRelationsHelper(game_id, tags, Discipline.GAMES);
    }

    protected static Map<Integer, List<Integer>> getAllGameTagRelations() {
        Map<Integer, List<Integer>> gameTagRelations = new HashMap<>();
        String sql = "SELECT * FROM games_tags";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                if(gameTagRelations.containsKey(rs.getInt("game_id"))) {
                    gameTagRelations.get(rs.getInt("game_id")).add(rs.getInt("tag_id"));
                } else {
                    List<Integer> tags = new ArrayList<>();
                    tags.add(rs.getInt("tag_id"));
                    gameTagRelations.put(rs.getInt("game_id"), tags);
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return gameTagRelations;
    }

    protected static List<String> getTagsForGame_Id(int id) {
        String sql = "SELECT name FROM games_tags tbl "
                + "JOIN tags ON tbl.tag_id = tags.tag_id "
                + "WHERE tbl.game_id = ?";
        return getTagsForIdHelper(sql, id);
    }

}
