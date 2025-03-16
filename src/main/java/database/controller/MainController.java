package database.controller;


import database.controller.tabController.GamesTabController;
import database.controller.tabController.StoriesTabController;
import database.controller.tabController.VideosTabController;
import database.model.propertyModels.GameDataSet;
import database.model.propertyModels.LiteratureDataSet;
import database.model.propertyModels.VideoDataSet;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainController extends ControllerHelper implements Initializable {

    // ---------- FXML ----------


    @FXML
    BorderPane rootPane;

    // --- Header ---

    // Menu Items
    @FXML
    MenuItem miOpen;
    @FXML
    MenuItem miNew;
    @FXML
    MenuItem miAddDataSet;
    // Buttons
    @FXML
    MFXButton btnMinimizeWindow;
    @FXML
    MFXButton btnChangeWindowMode;
    @FXML
    MFXButton btnCloseWindow;

    // --- Tab Pane ---
    @FXML
    TabPane tabPane;
    @FXML
    Tab tabGames;
    @FXML
    Tab tabStories;
    @FXML
    Tab tabVideos;

    // --- Footer ---
    @FXML
    Label lblStatus;
    @FXML
    Label lblCurrentPath;


    // ---------- Non-FXML ----------

    // Controllers
    GamesTabController gamesTabController;
    StoriesTabController storiesTabController;
    VideosTabController videosTabController;


    public MainController (Stage stage) {
        super(stage);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initGamesTab();
        initStoriesTab();
        initVideosTab();
        initMenuItems();
        initWindowButtons();
        initializeWindowDragging(stage, rootPane);
        setStatusCurrentMediaCount(logic.getGames(), logic.getStories(), logic.getVideos());
        updateCurrentPathLabel();
    }


    private void initGamesTab() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/gamesTab.fxml"));
            gamesTabController = new GamesTabController(stage, this);
            fxmlLoader.setController(gamesTabController);
            Parent content = fxmlLoader.load();
            tabGames.setContent(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initStoriesTab() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/storiesTab.fxml"));
            storiesTabController = new StoriesTabController(stage, this);
            fxmlLoader.setController(storiesTabController);
            Parent content = fxmlLoader.load();
            tabStories.setContent(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initVideosTab() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/videosTab.fxml"));
            videosTabController = new VideosTabController(stage, this);
            fxmlLoader.setController(videosTabController);
            Parent content = fxmlLoader.load();
            tabVideos.setContent(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initMenuItems() {
        miNew.setOnAction(createNewDatabase());
        miOpen.setOnAction(openDatabase());
        //miAddDataSet.setOnAction(addDataSet(10000, 200, 15));
    }


    private void initWindowButtons() {
        btnMinimizeWindow.setOnAction(event -> stage.setIconified(true));
        btnChangeWindowMode.setOnAction(event -> {
            if (stage.isFullScreen()) {
                stage.setFullScreen(false);
                isDraggable = true;
            } else {
                stage.setFullScreen(true);
                isDraggable = false;
            }
        });
        btnCloseWindow.setOnAction(event -> stage.close());
    }


    // ----- Helper Methods -----

    private EventHandler<ActionEvent> openDatabase() {
        return event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooserHelper(fileChooser, "Open Database");
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                logic.openDatabase(file.getPath());
                updateData();
                updateCurrentPathLabel();
            }
        };
    }

    private EventHandler<ActionEvent> createNewDatabase() {
        return event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooserHelper(fileChooser, "Create New Database");
            File file = fileChooser.showSaveDialog(stage);
            if (file != null) {
                String path = file.getAbsolutePath();
                // Add .db extension if not already present
                if (!path.endsWith(".db")) {
                    path += ".db";
                    file = new File(path);
                }
                logic.createNewDatabase(file.getPath());
                updateData();
                updateCurrentPathLabel();
            }
        };
    }

    /*
    private EventHandler<ActionEvent> addDataSet(int entryCount, int tagCount, int tagsPerEntryCount) {
        List<String> tags = new ArrayList<>();
        for (int i = 0; i < tagCount; i++) {
            String tag = "Tag" + i;
            tags.add(tag);
            logic.addTag(tag);
        }
        String artist = "Artist 0";
        String genre = "Genre 0";
        logic.addStringToTable(artist, TableNames.ARTIST, MediaType.GAMES);
        logic.addStringToTable(genre, TableNames.GENRE);
        return event -> {
            for (int i = 0; i < entryCount; i++) {
                List<String> entryTags = new ArrayList<>();
                for (int j = 0; j < tagsPerEntryCount; j++) {
                    entryTags.add(tags.get((i + j) % tags.size()));
                }
                String name = "Game " + i;
                logic.addGame(new GameDataSet(-1,
                        name,
                        artist,
                        genre,
                        State.DEV,
                        null,
                        null,
                        new HashMap<>(),
                        entryTags,
                        new ConsumedEntry(-1, "?", "?")));
            }
        };
    }

     */

    private void fileChooserHelper(FileChooser fileChooser, String title) {
        fileChooser.setTitle(title);
        fileChooser.setInitialDirectory(new File(logic.getDBPath()));
        // Set extension filter for .db files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("SQLite Files (*.db)", "*.db");
        fileChooser.getExtensionFilters().add(extFilter);
    }

    private void updateData() {
        gamesTabController.updateTable();
        //storiesTabController.updateTable();
        //videosTabController.updateTable();
        setStatusCurrentMediaCount(logic.getGames(), logic.getStories(), logic.getVideos());
    }

    private int getListSize(List<?> list) {
        return list == null ? 0 : list.size();
    }



    // Getter

    public TabPane getTabPane() { return tabPane; }


    // Setter

    public void setStatusCurrentMediaCount(List<GameDataSet> games, List<LiteratureDataSet> stories, List<VideoDataSet> videos) {
        String status = "Games: " + getListSize(games) + "/" + getListSize(logic.getGames()) +
                "  |  Stories: " + getListSize(stories) + "/" + getListSize(logic.getStories()) +
                "  |  Videos: " + getListSize(videos) + "/" + getListSize(logic.getVideos());
        lblStatus.setText(status);
    }

    public void updateCurrentPathLabel() {
        lblCurrentPath.setText("Current Database: " + logic.getDBName());
    }


}
