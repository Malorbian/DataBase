package database.entry_manager;

import database.enums.State;

public class GameEntry {
    private String title;
    private String artist;
    private String genre;
    private State state;
    private String link;
    private String imagePath;

    public GameEntry(String title, String artist, String genre, State state, String link, String imagePath) {
        this.title = title;
        this.artist = artist;
        this.genre = genre;
        this.state = state;
        this.link = link;
        this.imagePath = imagePath;
    }


    // Getter und Setter

    public String getTitle() {
        return title;
    }
    public String getArtist() {
        return artist;
    }
    public String getGenre() {
        return genre;
    }
    public String getState() {
        return state.toString();
    }
    public String getLink() {
        return link;
    }
    public String getImagePath() {
        return imagePath;
    }

}
