package database.model.mediaModels;

import database.enums.State;
import database.model.propertyModels.DataSet;

abstract public class MediaEntry<T extends DataSet> implements MediaEntryInterface {
    private int id;
    private String title;
    private String artist;
    private String genre;
    private State state;
    private String link;
    private String storagePath;

    public MediaEntry(int id, String title, String artist,
                      String genre, State state, String link,
                      String storagePath) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.genre = genre;
        this.state = state;
        this.link = link;
        this.storagePath = storagePath;
    }

    public MediaEntry(T entry) {
        this(Integer.parseInt(entry.getId()), entry.getTitle(), entry.getArtist(),
                entry.getGenre(), State.valueOf(entry.getState()), entry.getLink(),
                entry.getStoragePath());

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
    public String getStoragePath() {
        return storagePath;
    }

}
