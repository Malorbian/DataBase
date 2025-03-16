package database.model;

import database.model.propertyModels.GameDataSet;

import java.util.Map;

public class RatingEntry {
    private Integer medium_id;
    private Map<String, String> ratings;

    public RatingEntry(Integer medium_id, Map<String, String> ratings) {
        this.medium_id = medium_id;
        this.ratings = ratings;
    }

    public RatingEntry(GameDataSet game) {
        this(Integer.valueOf(game.getId()), game.getRatings());
    }



    public Integer getMediumId() {
        return medium_id;
    }
    public Map<String, String> getRatings() {
        return ratings;
    }
}
