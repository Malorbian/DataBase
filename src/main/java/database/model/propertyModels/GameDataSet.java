package database.model.propertyModels;

import database.enums.State;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.List;
import java.util.Map;

public class GameDataSet extends DataSetBase {
    private StringProperty lastPlayedDate = new SimpleStringProperty();
    private StringProperty lastPlayedVersion = new SimpleStringProperty();

    public GameDataSet(int id,
                       String type,
                       String title,
                       String artist,
                       String genre,
                       State state,
                       String link,
                       String storagePath,
                       Double length,
                       List<String> tags,
                       Map<String, String> ratings,
                       String lastPlayedDate,
                       String lastPlayedVersion) {
        super(id, type, title, artist, genre, state, link, storagePath, length, tags, ratings);
        this.lastPlayedDate.set(lastPlayedDate);
        this.lastPlayedVersion.set(lastPlayedVersion);
    }


    // ----- Getters & Setters -----

    public String getLastPlayedDate() { return lastPlayedDate.get(); }
    public String getLastPlayedVersion() { return lastPlayedVersion.get(); }

    public StringProperty lastPlayedDateProperty() { return lastPlayedDate; }
    public StringProperty lastPlayedVersionProperty() { return lastPlayedVersion; }

    public void setLastPlayedDate(String lastPlayedDate) { this.lastPlayedDate.set(lastPlayedDate); }
    public void setLastPlayedVersion(String lastPlayedVersion) { this.lastPlayedVersion.set(lastPlayedVersion); }
}
