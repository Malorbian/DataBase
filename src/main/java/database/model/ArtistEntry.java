package database.model;

import database.enums.MediaType;

public class ArtistEntry {
    private Integer id;
    private  String name;
    private MediaType mediaType;

    public ArtistEntry(Integer id, String name, MediaType mediaType) {
        this.id = id;
        this.name = name;
        this.mediaType = mediaType;
    }

    public Integer getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getMediaType() { return mediaType.toString(); }

}
