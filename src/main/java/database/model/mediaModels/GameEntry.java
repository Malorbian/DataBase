package database.model.mediaModels;

import database.enums.State;
import database.model.propertyModels.GameDataSet;

public class GameEntry extends MediaEntry<GameDataSet> {

    private String lastPlayedDate;
    private String lastPlayedVersion;

    public GameEntry(int id, String title, String artist,
                     String genre, State state, String link,
                     String storagePath, String lastPlayedDate, String lastPlayedVersion) {
        super(id, title, artist, genre, state, link, storagePath);
        this.lastPlayedDate = lastPlayedDate;
        this.lastPlayedVersion = lastPlayedVersion;
    }

    public GameEntry(GameDataSet game) {
        this(Integer.parseInt(game.getId()), game.getTitle(), game.getArtist(),
                game.getGenre(), State.valueOf(game.getState()), game.getLink(),
                game.getStoragePath(), game.getLastPlayedDate(), game.getLastPlayedVersion());
    }


    // Getter und Setter

    public String getLastPlayedDate() {
        return lastPlayedDate;
    }
    public String getLastPlayedVersion() {
        return lastPlayedVersion;
    }

}
