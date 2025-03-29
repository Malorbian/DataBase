package database.database_manager;

import database.enums.MediaType;

import java.sql.*;

public class DBFranchises extends DBHelper {

    static final String createFranchisesTableSQL = "CREATE TABLE IF NOT EXISTS franchises (" +
            "franchise_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "name TEXT, " +
            "CONSTRAINT unique_name UNIQUE (name)" +
            ");";

    static final String createFranchises_EntryTypeTableSQL = "CREATE TABLE IF NOT EXISTS franchises_entryTypes (" +
            "franchise_id INTEGER, " +
            "entryType_id INTEGER, " +
            "CONSTRAINT fk_entryType_id FOREIGN KEY (entryType_id) REFERENCES entry_types(entryType_id), " +
            "CONSTRAINT fk_franchise_id FOREIGN KEY (franchise_id) REFERENCES franchises(franchise_id), " +
            "CONSTRAINT pk_franchise_id_entryType_id PRIMARY KEY (entryType_id, franchise_id)" +
            ");";


    static void addFranchise(String franchise, String entryType, MediaType mediaType) {
        String addEntrySQL = "INSERT OR IGNORE INTO franchises (name) VALUES (?)";
        String entryTypeRelationSql = "INSERT OR IGNORE INTO franchises_entryTypes(franchise_id, entryType_id) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = conn.prepareStatement(addEntrySQL, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement ps2 = conn.prepareStatement(entryTypeRelationSql);) {

            // Add entry to genres table
            ps.setString(1, franchise);
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
            int entryTypeId = DBEntryTypes.getEntryTypeId(entryType, mediaTypeId, conn);

            // Add entry to genres_mediaTypes table
            ps2.setInt(1, entryId);
            ps2.setInt(2, entryTypeId);
            ps2.executeUpdate();

        } catch (SQLException e) {
            System.out.println("SQLException: " + franchise);
            e.printStackTrace();

        }
    }

    static int getFranchiseId(String franchise, int entryType_Id, Connection conn) {
        String sql = "SELECT franchise_id FROM franchises " +
                "LEFT JOIN entryTypes_franchises ON entryTypes_franchises.franchise_id = franchises.franchise_id " +
                "WHERE LOWER(name) = LOWER(?) AND entryType_id = " + entryType_Id;
        return getIdByString(sql, franchise, conn);
    }
    
}
