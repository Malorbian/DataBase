package database.entry_manager;

public class VideoEntry {
    private String title;
    private String artist;
    private String genre;
    private String link;
    private double length;

    public VideoEntry(String name, String artist, String genre, String link, double length) {
        this.title = name;
        this.artist = artist;
        this.genre = genre;
        this.link = link;
        this.length = length;
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
    public String getLink() {
        return link;
    }
    public double getLength() {
        return length;
    }
}
