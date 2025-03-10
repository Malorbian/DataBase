package database.model;

import database.enums.Discipline;

public class ArtistEntry {
    private Integer id;
    private  String name;
    private Discipline discipline;

    public ArtistEntry(Integer id, String name, Discipline discipline) {
        this.id = id;
        this.name = name;
        this.discipline = discipline;
    }

    public Integer getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getDiscipline() { return discipline.toString(); }

}
