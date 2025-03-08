package database.model;

import database.enums.State;

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
        this.link = link;
        this.state = state;
        this.genre = genre;
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
