package database.model.propertyModels;

import database.enums.State;
import database.model.VideoEntry;
import javafx.beans.property.StringProperty;

import java.util.List;

public class VideoDataSet extends DataSetBase {

    private StringProperty length;

    public VideoDataSet(int id,
                        String title,
                        String artist,
                        String genre,
                        State state,
                        String link,
                        List<String> tags,
                        Double length) {
        super(id, title, artist, genre, state, link, tags);
        this.length.set(String.valueOf(length));
    }

    public VideoDataSet(VideoEntry video, List<String> tags) {
        this(video.getId(), video.getTitle(), video.getArtist(),
                video.getGenre(), State.valueOf(video.getState()), video.getLink(), tags, video.getLength());
    }


    // ----- Getters & Setters -----

    public String getLength() { return length.get(); }

    public StringProperty lengthProperty() { return length; }

    public void setLength(String length) { this.length.set(length); }
}
