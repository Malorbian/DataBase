package database.entry_manager;

public class GameEntry {
    private int id;
    private String title;
    private String tags;
    private String imagePath;

    public GameEntry(int id, String title, String tags, String imagePath) {
        this.id = id;
        this.title = title;
        this.tags = tags;
        this.imagePath = imagePath;
    }

    // Konstruktor ohne id (für neue Einträge)
    public GameEntry(String title, String tags, String imagePath) {
        this(-1, title, tags, imagePath);
    }

    // Getter und Setter
    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getTags() {
        return tags;
    }
    public String getImagePath() {
        return imagePath;
    }

    public void setId(int id) {
        this.id = id;
    }
}
