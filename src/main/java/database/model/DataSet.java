package database.model;

import database.enums.MediaType;
import database.enums.State;
import javafx.beans.property.*;
import javafx.collections.FXCollections;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataSet {
    private StringProperty id = new SimpleStringProperty();
    private StringProperty mediaType = new SimpleStringProperty();
    private StringProperty entryType = new SimpleStringProperty();
    private StringProperty franchise = new SimpleStringProperty();
    private StringProperty title = new SimpleStringProperty();
    private StringProperty artist = new SimpleStringProperty();
    private StringProperty genre = new SimpleStringProperty();
    private StringProperty state = new SimpleStringProperty();
    private StringProperty link = new SimpleStringProperty();
    private StringProperty storagePath = new SimpleStringProperty();
    private StringProperty length = new SimpleStringProperty();
    private StringProperty consumedDate = new SimpleStringProperty();
    private StringProperty consumedVersion = new SimpleStringProperty();
    private ListProperty<StringProperty> tags;
    private MapProperty<String, StringProperty> ratings;

    public DataSet(int id,
                   MediaType mediaType,
                   String entryType,
                   String franchise,
                   String title,
                   String artist,
                   String genre,
                   State state,
                   String link,
                   String storagePath,
                   Double length,
                   LocalDate consumedDate,
                   String consumedVersion,
                   List<String> tags,
                   Map<String, String> ratings) {
        this.id.set(String.valueOf(id));
        this.mediaType.set(mediaType.toString());
        this.entryType.set(entryType);
        this.franchise.set(franchise);
        this.title.set(title);
        this.artist.set(artist);
        this.genre.set(genre);
        this.state.set(state.toString());
        this.link.set(link);
        this.storagePath.set(storagePath);
        this.length.set(String.valueOf(length));
        if (consumedDate != null) {
            this.consumedDate.set(consumedDate.format(DateTimeFormatter.ofPattern("dd.MM.yy")));
        }
        this.consumedVersion.set(consumedVersion);
        this.ratings = mapToMapProperty(ratings);
        this.tags = listToListProperty(tags);
    }


    private MapProperty<String, StringProperty> mapToMapProperty(Map<String, String> map) {
        MapProperty<String, StringProperty> mapPropertyMap = new SimpleMapProperty<>(FXCollections.observableHashMap());
        for (Map.Entry<String, String> entry : map.entrySet()) {
            mapPropertyMap.put(entry.getKey(), new SimpleStringProperty(entry.getValue()));
        }
        return mapPropertyMap;
    }

    private ListProperty<StringProperty> listToListProperty(List<String> list) {
        ListProperty<StringProperty> listPropertyList = new SimpleListProperty<>(FXCollections.observableArrayList());
        for (String entry : list) {
            listPropertyList.add(new SimpleStringProperty(entry));
        }
        return listPropertyList;
    }

    private List<String> listPropertyToList(ListProperty<StringProperty> listProperty) {
        List<String> list = new ArrayList<>();
        for (StringProperty entry : listProperty) {
            list.add(entry.get());
        }
        return list;
    }

    private Map<String, String> mapPropertyToMap(MapProperty<String, StringProperty> mapProperty) {
        Map<String, String> map = new HashMap<>();
        for (Map.Entry<String, StringProperty> entry : mapProperty.entrySet()) {
            map.put(entry.getKey(), entry.getValue().get());
        }
        return map;
    }

    // -------------------------------
    // ----- Getter and Setter -----
    // -------------------------------

    // ----- Getter -----

    public String getId() { return id.get(); }
    public String getMediaType() { return mediaType.get(); }
    public String getEntryType() { return entryType.get(); }
    public String getFranchise() { return franchise.get(); }
    public String getTitle() { return title.get(); }
    public String getArtist() { return artist.get(); }
    public String getGenre() { return genre.get(); }
    public String getState() { return state.get(); }
    public String getLink() { return link.get(); }
    public String getStoragePath() { return storagePath.get(); }
    public String getLength() { return length.get(); }
    public String getConsumedDate() { return consumedDate.get(); }
    public String getConsumedVersion() { return consumedVersion.get(); }
    public List<String> getTags() { return listPropertyToList(tags); }
    public Map<String, String> getRatings() { return mapPropertyToMap(ratings); }

    // ----- Property Getter -----

    public StringProperty idProperty() { return id; }
    public StringProperty mediaTypeProperty() { return mediaType; }
    public StringProperty entryTypeProperty() { return entryType; }
    public StringProperty franchiseProperty() { return franchise; }
    public StringProperty titleProperty() { return title; }
    public StringProperty artistProperty() { return artist; }
    public StringProperty genreProperty() { return genre; }
    public StringProperty stateProperty() { return state; }
    public StringProperty linkProperty() { return link; }
    public StringProperty storagePathProperty() { return storagePath; }
    public StringProperty lengthProperty() { return length; }
    public StringProperty consumedDateProperty() { return consumedDate; }
    public StringProperty consumedVersionProperty() { return consumedVersion; }
    public ListProperty<StringProperty> tagsProperty() { return tags; }
    public MapProperty<String, StringProperty> ratingsProperty() { return ratings; }

    // ----- Setter -----
    public void setId(String id) { this.id.set(id); };
    public void setMediaType(String mediaType) { this.mediaType.set(mediaType); }
    public void setEntryType(String entryType) { this.entryType.set(entryType); }
    public void setFranchise(String franchise) { this.franchise.set(franchise); }
    public void setTitle(String title) { this.title.set(title); }
    public void setArtist(String artist) { this.artist.set(artist); }
    public void setGenre(String genre) { this.genre.set(genre); }
    public void setState(String state) { this.state.set(state); }
    public void setLink(String link) { this.link.set(link); }
    public void setStoragePath(String storagePath) { this.storagePath.set(storagePath); }
    public void setLength(String length) { this.length.set(length); }
    public void setConsumedDate(String consumedDate) { this.consumedDate.set(consumedDate); }
    public void setConsumedVersion(String consumedVersion) { this.consumedVersion.set(consumedVersion); }
    public void setTags(List<String> tags) { this.tags = listToListProperty(tags); }
    public void setRatings(Map<String, String> ratings) { this.ratings = mapToMapProperty(ratings); }

}
