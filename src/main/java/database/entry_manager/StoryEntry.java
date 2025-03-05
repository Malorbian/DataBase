package database.entry_manager;

import database.enums.State;

public class StoryEntry {
    private String title;
    private String author;
    private String link;
    private String genre;
    private State state;

    public StoryEntry(String name, String author, String genre, State state, String link) {
        this.title = name;
        this.author = author;
        this.link = link;
        this.state = state;
        this.genre = genre;
    }

    public String getTitle() {
        return title;
    }
    public String getAuthor() {
        return author;
    }
    public String getLink() {
        return link;
    }
    public String getState() {
        return state.toString();
    }
    public String getGenre() {
        return genre;
    }
}
