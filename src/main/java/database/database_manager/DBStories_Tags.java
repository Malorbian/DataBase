package database.database_manager;

import database.enums.Discipline;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class DBStories_Tags extends DBHelper {

    // Tabelle erstellen, falls sie noch nicht existiert
    protected static void createTable() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            enableForeignKey(conn);
            String sql = "CREATE TABLE IF NOT EXISTS stories_tags (" +
                    "story_id INTEGER, " +
                    "tag_id INTEGER, " +
                    "CONSTRAINT fk_story_id FOREIGN KEY (story_id) REFERENCES stories(story_id), " +
                    "CONSTRAINT fk_tag_id FOREIGN KEY (tag_id) REFERENCES tags(tag_id), " +
                    "CONSTRAINT pk_story_id_tag_id PRIMARY KEY (story_id, tag_id)" +
                    ");";
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected static void addStoryTagRelations(int story_id, List<String> tags) {
        addRelationsHelper(story_id, tags, Discipline.STORIES);
    }

    protected static Map<Integer, List<Integer>> getAllStoryTagRelations() {
        Map<Integer, List<Integer>> storyTagRelations = new HashMap<>();
        String sql = "SELECT * FROM stories_tags";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                if(storyTagRelations.containsKey(rs.getInt("story_id"))) {
                    storyTagRelations.get(rs.getInt("story_id")).add(rs.getInt("tag_id"));
                } else {
                    List<Integer> tags = new ArrayList<>();
                    tags.add(rs.getInt("tag_id"));
                    storyTagRelations.put(rs.getInt("story_id"), tags);
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return storyTagRelations;
    }

    protected static List<String> getTagsForStory_Id(int id) {
        String sql = "SELECT name FROM stories_tags tbl "
                + "JOIN tags ON tbl.tag_id = tags.tag_id "
                + "WHERE tbl.stories_id = ?";
        return getTagsForIdHelper(sql, id);
    }
}
