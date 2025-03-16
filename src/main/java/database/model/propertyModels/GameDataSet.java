package database.model.propertyModels;

import database.enums.State;
import database.model.PlayedEntry;
import database.model.mediaModels.GameEntry;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.List;
import java.util.Map;

public class GameDataSet extends DataSetBase {
    private StringProperty lastPlayedDate = new SimpleStringProperty();
    private StringProperty lastPlayedVersion = new SimpleStringProperty();

    public GameDataSet(int id,
                       String title,
                       String artist,
                       String genre,
                       State state,
                       String link,
                       String storagePath,
                       List<String> tags,
                       Map<String, String> ratings,
                       PlayedEntry lastPlayed) {
        super(id, title, artist, genre, state, link, storagePath, tags, ratings);
        this.lastPlayedDate.set(lastPlayed.getDate());
        this.lastPlayedVersion.set(lastPlayed.getVersion());
    }

    public GameDataSet(GameEntry game, List<String> tags, Map<String, String> ratings) {
        this(game.getId(), game.getTitle(), game.getArtist(),
                game.getGenre(), State.valueOf(game.getState()), game.getLink(),
                game.getStoragePath(), tags, ratings,
                new PlayedEntry(-1, game.getLastPlayedDate(), game.getLastPlayedVersion()));
    }


    // ----- Getters & Setters -----

    public String getLastPlayedDate() { return lastPlayedDate.get(); }
    public String getLastPlayedVersion() { return lastPlayedVersion.get(); }

    public StringProperty lastPlayedDateProperty() { return lastPlayedDate; }
    public StringProperty lastPlayedVersionProperty() { return lastPlayedVersion; }

    public void setLastPlayedDate(String lastPlayedDate) { this.lastPlayedDate.set(lastPlayedDate); }
    public void setLastPlayedVersion(String lastPlayedVersion) { this.lastPlayedVersion.set(lastPlayedVersion); }
}
