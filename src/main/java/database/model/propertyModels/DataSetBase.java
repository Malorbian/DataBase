package database.model.propertyModels;

import database.enums.State;
import javafx.beans.property.*;
import javafx.collections.FXCollections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

abstract public class DataSetBase implements DataSet {
    protected StringProperty id = new SimpleStringProperty();
    protected StringProperty type = new SimpleStringProperty();
    protected StringProperty title = new SimpleStringProperty();
    protected StringProperty artist = new SimpleStringProperty();
    protected StringProperty genre = new SimpleStringProperty();
    protected StringProperty state = new SimpleStringProperty();
    protected StringProperty link = new SimpleStringProperty();
    protected StringProperty storagePath = new SimpleStringProperty();
    protected StringProperty length = new SimpleStringProperty();
    protected ListProperty<StringProperty> tags;
    protected MapProperty<String, StringProperty> ratings;

    public DataSetBase(int id,
                       String type,
                       String title,
                       String artist,
                       String genre,
                       State state,
                       String link,
                       String storagePath,
                       Double length,
                       List<String> tags,
                       Map<String, String> ratings) {
        this.id.set(String.valueOf(id));
        this.type.set(type);
        this.title.set(title);
        this.artist.set(artist);
        this.genre.set(genre);
        this.state.set(state.toString());
        this.link.set(link);
        this.storagePath.set(storagePath);
        this.length.set(String.valueOf(length));
        this.ratings = mapToMapProperty(ratings);
        this.tags = listToListProperty(tags);
    }


    protected MapProperty<String, StringProperty> mapToMapProperty(Map<String, String> map) {
        MapProperty<String, StringProperty> mapPropertyMap = new SimpleMapProperty<>(FXCollections.observableHashMap());
        for (Map.Entry<String, String> entry : map.entrySet()) {
            mapPropertyMap.put(entry.getKey(), new SimpleStringProperty(entry.getValue()));
        }
        return mapPropertyMap;
    }

    protected ListProperty<StringProperty> listToListProperty(List<String> list) {
        ListProperty<StringProperty> listPropertyList = new SimpleListProperty<>(FXCollections.observableArrayList());
        for (String entry : list) {
            listPropertyList.add(new SimpleStringProperty(entry));
        }
        return listPropertyList;
    }

    protected List<String> listPropertyToList(ListProperty<StringProperty> listProperty) {
        List<String> list = new ArrayList<>();
        for (StringProperty entry : listProperty) {
            list.add(entry.get());
        }
        return list;
    }

    protected Map<String, String> mapPropertyToMap(MapProperty<String, StringProperty> mapProperty) {
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
    public String getType() { return type.get(); }
    public String getTitle() { return title.get(); }
    public String getArtist() { return artist.get(); }
    public String getGenre() { return genre.get(); }
    public String getState() { return state.get().toString(); }
    public String getLink() { return link.get(); }
    public String getStoragePath() { return storagePath.get(); }
    public String getLength() { return length.get(); }
    public List<String> getTags() { return listPropertyToList(tags); }
    public Map<String, String> getRatings() { return mapPropertyToMap(ratings); }

    // ----- Property Getter -----

    public StringProperty idProperty() { return id; }
    public StringProperty typeProperty() { return type; }
    public StringProperty titleProperty() { return title; }
    public StringProperty artistProperty() { return artist; }
    public StringProperty genreProperty() { return genre; }
    public StringProperty stateProperty() { return state; }
    public StringProperty linkProperty() { return link; }
    public StringProperty storagePathProperty() { return storagePath; }
    public StringProperty lengthProperty() { return length; }
    public ListProperty<StringProperty> tagsProperty() { return tags; }
    public MapProperty<String, StringProperty> ratingsProperty() { return ratings; }

    // ----- Setter -----
    public void setId(String id) { this.id.set(id); };
    public void setType(String type) { this.type.set(type); }
    public void setTitle(String title) { this.title.set(title); }
    public void setArtist(String artist) { this.artist.set(artist); }
    public void setGenre(String genre) { this.genre.set(genre); }
    public void setState(String state) { this.state.set(state); }
    public void setLink(String link) { this.link.set(link); }
    public void setStoragePath(String storagePath) { this.storagePath.set(storagePath); }
    public void setLength(String length) { this.length.set(length); }
    public void setTags(List<String> tags) { this.tags = listToListProperty(tags); }
    public void setRatings(Map<String, String> ratings) { this.ratings = mapToMapProperty(ratings); }

}
