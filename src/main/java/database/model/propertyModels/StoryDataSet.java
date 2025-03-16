package database.model.propertyModels;

import database.enums.State;
import database.model.StoryEntry;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.List;
import java.util.Map;

public class StoryDataSet extends DataSetBase {

    private StringProperty type = new SimpleStringProperty();

    public StoryDataSet(int id,
                        String type,
                        String title,
                        String artist,
                        String genre,
                        State state,
                        String link,
                        String storagePath,
                        List<String> tags,
                        Map<String, String> ratings) {
        super(id, title, artist, genre, state, link, storagePath, tags, ratings);
        this.type.set(type);
    }

    public StoryDataSet(StoryEntry story, List<String> tags, Map<String, String> ratings) {
        this(story.getId(), story.getType(), story.getTitle(), story.getArtist(),
                story.getGenre(), State.valueOf(story.getState()), story.getLink(), null, tags, ratings);
    }

    public String getType() {
        return type.get();
    }
    public StringProperty typeProperty() {
        return type;
    }
    public void setType(String type) {
        this.type.set(type);
    }
}
