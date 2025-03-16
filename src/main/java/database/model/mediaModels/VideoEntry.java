package database.model.mediaModels;

import database.enums.MediaType;
import database.enums.State;
import database.model.propertyModels.VideoDataSet;

public class VideoEntry extends MediaEntry<VideoDataSet> {

    private MediaType type;
    private Double length;

    public VideoEntry(Integer id,
                      MediaType type,
                      String name,
                      String artist,
                      String genre,
                      State state,
                      String link,
                      String storagePath,
                      double length) {
        super(id, name, artist, genre, state, link, storagePath);
        this.type = type;
        this.length = length;
    }

    public VideoEntry (VideoDataSet video) {
        this(Integer.parseInt(video.getId()), MediaType.valueOf(video.getType()), video.getTitle(),
                video.getArtist(), video.getGenre(), State.valueOf(video.getState()),
                video.getLink(), video.getStoragePath(), Double.parseDouble(video.getLength()));
    }

    public String getType() { return type.toString(); }
    public double getLength() {
        return length;
    }
}
