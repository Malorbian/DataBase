package database.model.propertyModels;

import database.enums.State;
import database.model.VideoEntry;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.List;
import java.util.Map;

public class VideoDataSet extends DataSetBase {

    private StringProperty length = new SimpleStringProperty();
    private StringProperty type = new SimpleStringProperty();

    public VideoDataSet(int id,
                        String title,
                        String type,
                        String artist,
                        String genre,
                        State state,
                        String link,
                        String storagePath,
                        List<String> tags,
                        Map<String, String> ratings,
                        Double length) {
        super(id, title, artist, genre, state, link, storagePath, tags, ratings);
        this.length.set(String.valueOf(length));
    }

    public VideoDataSet(VideoEntry video, List<String> tags, Map<String, String> ratings) {
        this(video.getId(), video.getType(), video.getTitle(), video.getArtist(),
                video.getGenre(), State.valueOf(video.getState()), video.getLink(),
                null, tags, ratings, video.getLength());
    }


    // ----- Getters & Setters -----

    public String getType() { return type.get(); }
    public String getLength() { return length.get(); }

    public StringProperty typeProperty() { return type; }
    public StringProperty lengthProperty() { return length; }

    public void setType(String type) { this.type.set(type); }
    public void setLength(String length) { this.length.set(length); }
}
