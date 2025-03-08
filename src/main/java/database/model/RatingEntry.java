package database.model;

import database.model.propertyModels.GameDataSet;

import java.util.Map;

public class RatingEntry {
    private Integer game_id;
    private Map<String, String> ratings;

    public RatingEntry(Integer game_id, Map<String, String> ratings) {
        this.game_id = game_id;
        this.ratings = ratings;
    }

    public RatingEntry(GameDataSet game) {
        this(Integer.valueOf(game.getId()), game.getRatings());
    }



    public Integer getGameId() {
        return game_id;
    }
    public Map<String, String> getRatings() {
        return ratings;
    }
}
