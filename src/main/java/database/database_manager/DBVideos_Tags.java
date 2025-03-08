package database.database_manager;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class DBVideos_Tags extends DBManager {

    // Tabelle erstellen, falls sie noch nicht existiert
    protected static void createTable() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            enableForeignKey(conn);
            String sql = "CREATE TABLE IF NOT EXISTS videos_tags (" +
                    "video_id INTEGER, " +
                    "tag_id INTEGER, " +
                    "CONSTRAINT fk_video_id FOREIGN KEY (video_id) REFERENCES videos(video_id), " +
                    "CONSTRAINT fk_tag_id FOREIGN KEY (tag_id) REFERENCES tags(tag_id), " +
                    "CONSTRAINT pk_video_id_tag_id PRIMARY KEY (video_id, tag_id)" +
                    ");";
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected static void addStoryTagRelation(int video_id, int tag_id) {
        String sql = "INSERT OR IGNORE INTO videos_tags(video_id, tag_id) VALUES(?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            addVideoTagRelation(video_id, tag_id, conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected static void addVideoTagRelation(int video_id, int tag_id, Connection conn) {
        String sql = "INSERT OR IGNORE INTO videos_tags(video_id, tag_id) VALUES(?, ?)";
        try (java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, video_id);
            pstmt.setInt(2, tag_id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected static Map<Integer, List<Integer>> getAllStoryTagRelations() {
        Map<Integer, List<Integer>> videoTagRelations = new HashMap<>();
        String sql = "SELECT * FROM videos_tags";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                if(videoTagRelations.containsKey(rs.getInt("video_id"))) {
                    videoTagRelations.get(rs.getInt("video_id")).add(rs.getInt("tag_id"));
                } else {
                    List<Integer> tags = new ArrayList<>();
                    tags.add(rs.getInt("tag_id"));
                    videoTagRelations.put(rs.getInt("video_id"), tags);
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return videoTagRelations;
    }

    protected static List<String> getTagsForVideo_Id(int id) {
        String sql = "SELECT name FROM videos_tags tbl "
                + "JOIN tags ON tbl.tag_id = tags.tag_id "
                + "WHERE tbl.videos_id = ?";
        return getTagsForIdHelper(sql, id);
    }

}
