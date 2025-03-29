package database.model;

import database.database_manager.DBManager;
import database.enums.MediaType;
import database.logic.Logic;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.io.File;
import java.nio.file.Paths;

public class LogicDataClass {

    Logic logic = Logic.getInstance();
    // Media Data
    private ObservableMap<MediaType, ObservableList<DataSet>> mediaEntries;
    // Data for GUI elements
    private ObservableMap<MediaType, ObservableList<String>> entryTypes;
    private ObservableMap<String, ObservableList<String>> franchises;
    private ObservableMap<MediaType, ObservableList<String>> genres;
    private ObservableMap<MediaType, ObservableList<String>> tags;
    private ObservableMap<MediaType, ObservableList<String>> artists;
    private ObservableMap<MediaType, ObservableList<String>> ratingPlatforms;


    public LogicDataClass() {
        this.mediaEntries = FXCollections.observableHashMap();
        this.entryTypes = FXCollections.observableHashMap();
        this.franchises = FXCollections.observableHashMap();
        this.genres = FXCollections.observableHashMap();
        this.tags = FXCollections.observableHashMap();
        this.artists = FXCollections.observableHashMap();
        this.ratingPlatforms = FXCollections.observableHashMap();
        initSubLists();
    }

    public void loadData() {
        File file = new File(Paths.get(logic.getDBPath(), logic.getDBName()).toString());
        if (!file.exists()) {
            System.out.println("No database found");
            return;
        }
        DBManager.loadDB(this);
    }


    public ObservableMap<MediaType, ObservableList<DataSet>> getMediaEntries() { return mediaEntries; }
    public ObservableMap<MediaType, ObservableList<String>> getEntryTypes() { return entryTypes; }
    public ObservableMap<String, ObservableList<String>> getFranchises() { return franchises; }
    public ObservableMap<MediaType, ObservableList<String>> getGenres() { return genres; }
    public ObservableMap<MediaType, ObservableList<String>> getTags() { return tags; }
    public ObservableMap<MediaType, ObservableList<String>> getArtists() { return artists; }
    public ObservableMap<MediaType, ObservableList<String>> getRatingPlatforms() { return ratingPlatforms; }




    public void addEntryType(String entryType, MediaType mediaType) {
        entryTypes.get(mediaType).add(entryType);
        DBManager.addEntryType(entryType, mediaType);
    }

    public void addFranchise(String franchise, String entryType, MediaType mediaType) {
        if (!franchises.containsKey(entryType)) {
            franchises.put(entryType, FXCollections.observableArrayList());
        }
        franchises.get(entryType).add(franchise);
        DBManager.addFranchise(franchise, entryType, mediaType);
    }

    public void addArtist(ArtistEntry artist) {
        if (artist.getMediaType() == null) return;
        artists.get(artist.getMediaType()).add(artist.getName());
        DBManager.addArtist(artist);
    }

    public void addGenre(String genre, MediaType mediaType) {
        genres.get(mediaType).add(genre);
        DBManager.addGenre(genre, mediaType);
    }

    public void addTag(String tag, MediaType mediaType) {
        tags.get(mediaType).add(tag);
        DBManager.addTag(tag, mediaType);
    }

    public void addRatingPlatform(String ratingPlatform, MediaType mediaType) {
        ratingPlatforms.get(mediaType).add(ratingPlatform);
        DBManager.addRatingPlatform(ratingPlatform, mediaType);
    }

    public void addMediaEntry(DataSet entry, MediaType mediaType) {
        int dataSetId = DBManager.addMediaEntry(entry, mediaType);
        entry.setId(String.valueOf(dataSetId));
        mediaEntries.get(mediaType).add(entry);

    }



    public void clearLists() {
        for (MediaType mediaType : MediaType.values()) {
            mediaEntries.get(mediaType).clear();
            entryTypes.get(mediaType).clear();
            genres.get(mediaType).clear();
            tags.get(mediaType).clear();
            artists.get(mediaType).clear();
            ratingPlatforms.get(mediaType).clear();
        }
        for (String franchise : franchises.keySet()) {
            franchises.get(franchise).clear();
        }
    }

    private void initSubLists() {
        for (MediaType mediaType : MediaType.values()) {
            mediaEntries.put(mediaType, FXCollections.observableArrayList());
            entryTypes.put(mediaType, FXCollections.observableArrayList());
            genres.put(mediaType, FXCollections.observableArrayList());
            tags.put(mediaType, FXCollections.observableArrayList());
            artists.put(mediaType, FXCollections.observableArrayList());
            ratingPlatforms.put(mediaType, FXCollections.observableArrayList());
        }
    }

}
