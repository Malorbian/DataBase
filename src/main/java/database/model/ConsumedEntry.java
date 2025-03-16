package database.model;

public class ConsumedEntry {
    private Integer id;
    private String date;
    private String version;

    public ConsumedEntry(Integer id, String date, String version) {
        this.id = id;
        this.date = date;
        this.version = version;
    }

    public Integer getId() {
        return id;
    }
    public String getDate() {
        return date;
    }
    public String getVersion() {
        return version;
    }
}
