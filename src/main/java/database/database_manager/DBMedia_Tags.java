package database.database_manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

class DBMedia_Tags extends DBHelper {

    static final String createTableSQL = "CREATE TABLE IF NOT EXISTS media_tags (" +
            "medium_id INTEGER, " +
            "tag_id INTEGER, " +
            "CONSTRAINT fk_medium_id FOREIGN KEY (medium_id) REFERENCES media_entries(entry_id), " +
            "CONSTRAINT fk_tag_id FOREIGN KEY (tag_id) REFERENCES tags(tag_id), " +
            "CONSTRAINT pk_medium_id_tag_id PRIMARY KEY (medium_id, tag_id)" +
            ");";


    static void addMediumTagRelations(int medium_id, List<String> tags, Connection conn) {
        String insertRelationSQL = "INSERT OR IGNORE INTO media_tags(medium_id, tag_id) VALUES (?, ?)";
        String selectTagIdSQL = "SELECT tag_id FROM tags WHERE name = ?";

        try (PreparedStatement selectTagStmt = conn.prepareStatement(selectTagIdSQL);
             PreparedStatement insertRelationStmt = conn.prepareStatement(insertRelationSQL)) {

            conn.setAutoCommit(false); // Beginne eine Transaktion

            for (String tag : tags) {
                selectTagStmt.setString(1, tag);
                try (ResultSet rs = selectTagStmt.executeQuery()) {
                    if (rs.next()) {
                        int tag_id = rs.getInt("tag_id");

                        insertRelationStmt.setInt(1, medium_id);
                        insertRelationStmt.setInt(2, tag_id);
                        insertRelationStmt.addBatch(); // Batch-Insert vorbereiten
                    }
                }
            }

            insertRelationStmt.executeBatch(); // Führt alle gesammelten Inserts auf einmal aus
            conn.commit(); // Transaktion abschließen

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
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

     */
}
