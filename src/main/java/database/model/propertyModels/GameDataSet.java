package database.model.propertyModels;

import database.enums.State;
import database.model.GameEntry;
import database.model.PlayedEntry;
import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.List;
import java.util.Map;

public class GameDataSet extends DataSetBase {
    private StringProperty image = new SimpleStringProperty();
    private StringProperty lastPlayedDate = new SimpleStringProperty();
    private StringProperty lastPlayedVersion = new SimpleStringProperty();
    private MapProperty<String, StringProperty> ratings;

    public GameDataSet(int id,
                       String title,
                       String artist,
                       String genre,
                       State state,
                       String link,
                       String image,
                       Map<String, String> ratings,
                       List<String> tags,
                       PlayedEntry lastPlayed) {
        super(id, title, artist, genre, state, link, tags);
        this.image.set(image);
        this.lastPlayedDate.set(lastPlayed.getDate());
        this.lastPlayedVersion.set(lastPlayed.getVersion());
        this.ratings = mapToMapProperty(ratings);
    }

    public GameDataSet(GameEntry game, Map<String, String> ratings, List<String> tags) {
        this(game.getId(), game.getTitle(), game.getArtist(),
                game.getGenre(), State.valueOf(game.getState()), game.getLink(),
                game.getImagePath(), ratings, tags,
                new PlayedEntry(-1, game.getLastPlayedDate(), game.getLastPlayedVersion()));
    }


    // ----- Getters & Setters -----

    public String getImage() { return image.get(); }
    public String getLastPlayedDate() { return lastPlayedDate.get(); }
    public String getLastPlayedVersion() { return lastPlayedVersion.get(); }
    public Map<String, String> getRatings() { return mapPropertyToMap(ratings); }

    public StringProperty imageProperty() { return image; }
    public StringProperty lastPlayedDateProperty() { return lastPlayedDate; }
    public StringProperty lastPlayedVersionProperty() { return lastPlayedVersion; }
    public MapProperty<String, StringProperty> ratingsProperty() { return ratings; }

    public void setImage(String image) { this.image.set(image); }
    public void setLastPlayedDate(String lastPlayedDate) { this.lastPlayedDate.set(lastPlayedDate); }
    public void setLastPlayedVersion(String lastPlayedVersion) { this.lastPlayedVersion.set(lastPlayedVersion); }
    public void setRatings(Map<String, String> ratings) { this.ratings = mapToMapProperty(ratings); }
}
