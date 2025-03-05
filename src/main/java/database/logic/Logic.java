package database.logic;

import database.database_manager.DBManager;

public class Logic {
    private DBManager dbManager;
    private static Logic instance;

    private Logic() {
        dbManager = DBManager.getInstance();
    }

    public static Logic getInstance() {
        if (instance == null) {
            instance = new Logic();
        }
        return instance;
    }

}
