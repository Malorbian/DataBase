package database.model;

import database.enums.State;
import database.model.propertyModels.VideoDataSet;

public class VideoEntry {
    private Integer id;
    private String title;
    private String artist;
    private String genre;
    private State state;
    private String link;
    private double length;

    public VideoEntry(Integer id, String name, String artist, String genre, State state, String link, double length) {
        this.id = id;
        this.title = name;
        this.artist = artist;
        this.genre = genre;
        this.state = state;
        this.link = link;
        this.length = length;
    }

    public VideoEntry (VideoDataSet video) {
        this(Integer.parseInt(video.getId()), video.getTitle(), video.getArtist(), video.getGenre(), State.valueOf(video.getState()), video.getLink(), Double.parseDouble(video.getLength()));
    }

    public Integer getId() { return id; }
    public String getTitle() {
        return title;
    }
    public String getArtist() {
        return artist;
    }
    public String getGenre() { return genre; }
    public String getState() { return state.toString(); }
    public String getLink() {
        return link;
    }
    public double getLength() {
        return length;
    }
}
