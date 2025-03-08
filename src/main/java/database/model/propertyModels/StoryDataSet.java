package database.model.propertyModels;

import database.enums.State;
import database.model.StoryEntry;

import java.util.List;

public class StoryDataSet extends DataSetBase {



    public StoryDataSet(int id,
                        String title,
                        String artist,
                        String genre,
                        State state,
                        String link,
                        List<String> tags) {
        super(id, title, artist, genre, state, link, tags);
    }

    public StoryDataSet(StoryEntry story, List<String> tags) {
        this(story.getId(), story.getTitle(), story.getArtist(),
                story.getGenre(), State.valueOf(story.getState()), story.getLink(), tags);
    }
}
