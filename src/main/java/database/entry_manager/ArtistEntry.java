package database.entry_manager;

public class ArtistEntry {
    private  String name;
    private  String discipline;

    public ArtistEntry(String name, String discipline) {
        this.name = name;
        this.discipline = discipline;
    }

    public String getName() {
        return name;
    }
    public String getDiscipline() {
        return discipline;
    }
}
