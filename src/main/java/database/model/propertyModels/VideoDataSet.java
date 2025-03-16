package database.model.propertyModels;

import database.enums.State;

import java.util.List;
import java.util.Map;

public class VideoDataSet extends DataSetBase {

    public VideoDataSet(int id,
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
        super(id, type, title, artist, genre, state, link, storagePath, length, tags, ratings);
    }
}
