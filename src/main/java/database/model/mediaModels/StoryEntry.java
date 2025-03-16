package database.model.mediaModels;

import database.enums.MediaType;
import database.enums.State;
import database.model.propertyModels.StoryDataSet;

public class StoryEntry extends MediaEntry<StoryDataSet> {

    MediaType type = MediaType.LITERATURE;

    public StoryEntry(Integer id,
                      MediaType type,
                      String name,
                      String artist,
                      String genre,
                      State state,
                      String link,
                      String storagePath){
        super(id, name, artist, genre, state, link, storagePath);
    }

    public StoryEntry(StoryDataSet story) {
        this(Integer.valueOf(story.getId()), MediaType.valueOf(story.getType()), story.getTitle(), story.getArtist(),
                story.getGenre(), State.valueOf(story.getState()), story.getLink(), story.getStoragePath());
    }

    public String getType() { return type.toString(); }

}
