package database.model.propertyModels;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PlatformEntry {
    private final SimpleStringProperty platform;
    private final SimpleStringProperty rating;

    public PlatformEntry(String platform) {
        this.platform = new SimpleStringProperty(platform);
        this.rating = new SimpleStringProperty(""); // Standardmäßig leer
    }

    public PlatformEntry(String platform, String rating) {
        this.platform = new SimpleStringProperty(platform);
        this.rating = new SimpleStringProperty(rating);
    }


    public String getPlatform() {
        return platform.get();
    }

    public void setPlatform(String platform) {
        this.platform.set(platform);
    }

    public StringProperty platformProperty() {
        return platform;
    }

    public String getRating() {
        return rating.get();
    }

    public void setRating(String rating) {
        this.rating.set(rating);
    }

    public StringProperty ratingProperty() {
        return rating;
    }
}
