package database.model.propertyModels;

import javafx.beans.property.ListProperty;
import javafx.beans.property.StringProperty;

import java.util.List;

public interface DataSet {

    // ----- Getter -----

    String getId();
    String getTitle();
    String getArtist();
    String getGenre();
    String getState();
    String getLink() ;
    List<String> getTags();

    // ----- Property Getter -----

    StringProperty idProperty();
    StringProperty titleProperty();
    StringProperty artistProperty();
    StringProperty genreProperty();
    StringProperty stateProperty();
    StringProperty linkProperty();
    ListProperty<StringProperty> tagsProperty();

    // ----- Setter -----
    void setId(String id);
    void setTitle(String title);
    void setArtist(String artist);
    void setGenre(String genre);
    void setState(String state);
    void setLink(String link);
    void setTags(List<String> tags);
}
