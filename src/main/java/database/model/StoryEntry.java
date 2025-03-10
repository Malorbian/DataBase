package database.model;

import database.enums.State;
import database.model.propertyModels.StoryDataSet;

public class StoryEntry {
    private Integer id;
    private String title;
    private String artist;
    private String link;
    private String genre;
    private State state;

    public StoryEntry(Integer id, String name, String artist, String genre, State state, String link) {
        this.id = id;
        this.title = name;
        this.artist = artist;
        this.genre = genre;
        this.state = state;
        this.link = link;
    }

    public StoryEntry(StoryDataSet story) {
        this(Integer.valueOf(story.getId()), story.getTitle(), story.getArtist(),
                story.getGenre(), State.valueOf(story.getState()), story.getLink());
    }

    public Integer getId() { return id; }
    public String getTitle() {
        return title;
    }
    public String getArtist() {
        return artist;
    }
    public String getLink() {
        return link;
    }
    public String getState() {
        return state.toString();
    }
    public String getGenre() { return genre; }
}
