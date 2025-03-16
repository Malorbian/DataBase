package database.database_manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class DBEntryTypes extends DBHelper{

    static final String createTableSQL = "CREATE TABLE IF NOT EXISTS entry_types (" +
            "type_id integer PRIMARY KEY AUTOINCREMENT, " +
            "name TEXT NOT NULL, " +
            "CONSTRAINT unique_name UNIQUE (name)" +
            ");";

    static final String addEntrySQL = "INSERT INTO types (name) VALUES (?)";


    static void addType(String typeName) {
        addEntryByString(addEntrySQL, typeName);
    }

    static void addType(List<String> typeNames) {
        addEntriesByList(addEntrySQL, typeNames);
    }

    static int getTypeId(String typeName) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            return getTypeId(typeName, conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    static int getTypeId(String typeName, Connection conn) {
        String sql = "SELECT type_id FROM types WHERE name = ?";
        return getIdByString(sql, typeName, conn);
    }
}
