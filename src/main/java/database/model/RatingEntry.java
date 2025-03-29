package database.model;

import java.util.Map;

public class RatingEntry {
    private Integer medium_id;
    private Map<String, String> ratings;

    public RatingEntry(Integer medium_id, Map<String, String> ratings) {
        this.medium_id = medium_id;
        this.ratings = ratings;
    }

    public Integer getMediumId() {
        return medium_id;
    }
    public Map<String, String> getRatings() {
        return ratings;
    }
}
