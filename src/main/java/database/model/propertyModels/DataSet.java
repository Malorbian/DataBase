package database.model.propertyModels;

import javafx.beans.property.ListProperty;
import javafx.beans.property.MapProperty;
import javafx.beans.property.StringProperty;

import java.util.List;
import java.util.Map;

public interface DataSet {

    // ----- Getter -----

    String getId();
    String getTitle();
    String getArtist();
    String getGenre();
    String getState();
    String getLink() ;
    String getStoragePath();
    List<String> getTags();
    Map<String, String> getRatings();

    // ----- Property Getter -----

    StringProperty idProperty();
    StringProperty titleProperty();
    StringProperty artistProperty();
    StringProperty genreProperty();
    StringProperty stateProperty();
    StringProperty linkProperty();
    StringProperty storagePathProperty();
    ListProperty<StringProperty> tagsProperty();
    MapProperty<String, StringProperty> ratingsProperty();

    // ----- Setter -----
    void setId(String id);
    void setTitle(String title);
    void setArtist(String artist);
    void setGenre(String genre);
    void setState(String state);
    void setLink(String link);
    void setStoragePath(String storagePath);
    void setTags(List<String> tags);
    void setRatings(Map<String, String> ratings);
}
