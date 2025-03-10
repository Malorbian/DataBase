package database.model;

import database.enums.State;
import database.model.propertyModels.GameDataSet;

public class GameEntry {
    private int id;
    private String title;
    private String artist;
    private String genre;
    private State state;
    private String link;
    private String imagePath;
    private String lastPlayedDate;
    private String lastPlayedVersion;

    public GameEntry(int id, String title, String artist,
                     String genre, State state, String link,
                     String imagePath, String lastPlayedDate, String lastPlayedVersion) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.genre = genre;
        this.state = state;
        this.link = link;
        this.imagePath = imagePath;
        this.lastPlayedDate = lastPlayedDate;
        this.lastPlayedVersion = lastPlayedVersion;
    }

    public GameEntry(GameDataSet game) {
        this(Integer.valueOf(game.getId()), game.getTitle(), game.getArtist(),
                game.getGenre(), State.valueOf(game.getState()), game.getLink(),
                game.getImage(), game.getLastPlayedDate(), game.getLastPlayedVersion());
    }


    // Getter und Setter

    public int getId() {
        return id;
    }
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
    public String getLastPlayedDate() {
        return lastPlayedDate;
    }
    public String getLastPlayedVersion() {
        return lastPlayedVersion;
    }

}
