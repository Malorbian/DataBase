package database.logic;

import database.database_manager.DBManager;
import database.enums.Discipline;
import database.enums.TableNames;
import database.model.ArtistEntry;
import database.model.GameEntry;
import database.model.propertyModels.GameDataSet;
import database.model.propertyModels.StoryDataSet;
import database.model.propertyModels.VideoDataSet;

import java.util.ArrayList;
import java.util.List;

public class Logic {
    private DBManager dbManager;
    private static Logic instance;

    private List<GameDataSet> games;
    private List<StoryDataSet> stories;
    private List<VideoDataSet> videos;
    private List<String> genres;
    private List<String> tags;
    private List<String> artistsNames;
    private List<String> platforms;

    private Logic() {
        dbManager = DBManager.getInstance();
        games = dbManager.getGameDataSetCollection();
        stories = dbManager.getStoryDataSetCollection();
        videos = dbManager.getVideoDataSetCollection();
        genres = dbManager.getGenres();
        tags = dbManager.getTags();
        artistsNames = new ArrayList<>();
        for (ArtistEntry artist : dbManager.getArtists()) {
            artistsNames.add(artist.getName());
        }
        platforms = dbManager.getPlatforms();
    }

    public static Logic getInstance() {
        if (instance == null) {
            instance = new Logic();
        }
        return instance;
    }


    public void addGame(GameDataSet game) {
        game.setId(String.valueOf(DBManager.addGame(game)));
        games.add(game);
    }

    public void addPlatform(String platformName) {
        if (platforms.contains(platformName)) { return; }
        platforms.add(platformName);
        dbManager.addPlatform(platformName);
    }

    public void addTag(String tag) {
        if (tags.contains(tag)) { return; }
        tags.add(tag);
        dbManager.addTag(tag);
    }

    public void addGenre(String genre) {
        if (genres.contains(genre)) { return; }
        genres.add(genre);
        dbManager.addGenre(genre);
    }

    public void addArtistName(ArtistEntry artist) {
        if (artistsNames.contains(artist)) { return; }
        artistsNames.add(artist.getName());
        dbManager.addArtist(artist);
    }

    public void addStringToTable(String string, TableNames tableName, Discipline discipline) {
        switch (tableName) {
            case GENRE:
                addGenre(string);
                break;
            case TAG:
                addTag(string);
                break;
            case PLATFORM:
                addPlatform(string);
                break;
            case ARTIST:
                addArtistName(new ArtistEntry(-1, string, discipline));
                break;
            default:
                break;
        }
    }

    public void addStringToTable(String string, TableNames tableName) {
        if (tableName == TableNames.ARTIST) {
            throw new IllegalArgumentException("Discipline must be provided for ARTIST table");
        }
        addStringToTable(string, tableName, null);
    }



    public List<String> getArtistsNames() {
        return artistsNames;
    }

    public List<String> getGenres() {
        return genres;
    }

    public List<String> getTags() {
        return tags;
    }

    public List<String> getPlatforms() {
        return platforms;
    }




    public List<GameDataSet> getGames() {
        return games;
    }

    public List<StoryDataSet> getStories() {
        return stories;
    }
    public List<VideoDataSet> getVideos() {
        return videos;
    }

}
