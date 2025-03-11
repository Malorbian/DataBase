package database.controller;


import database.controller.tabController.GamesTabController;
import database.controller.tabController.StoriesTabController;
import database.controller.tabController.VideosTabController;
import database.model.propertyModels.GameDataSet;
import database.model.propertyModels.StoryDataSet;
import database.model.propertyModels.VideoDataSet;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.collections.FXCollections;
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

    private void fileChooserHelper(FileChooser fileChooser, String title) {
        fileChooser.setTitle(title);
        fileChooser.setInitialDirectory(new File(logic.getDBPath()));
        // Set extension filter for .db files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("SQLite Files (*.db)", "*.db");
        fileChooser.getExtensionFilters().add(extFilter);
    }

    private void updateData() {
        //gamesTabController.showGames(FXCollections.observableArrayList(logic.getGames()));
        storiesTabController.showStories(FXCollections.observableArrayList(logic.getStories()));
        videosTabController.showVideos(FXCollections.observableArrayList(logic.getVideos()));
        setStatusCurrentMediaCount(logic.getGames(), logic.getStories(), logic.getVideos());
    }

    private int getListSize(List<?> list) {
        return list == null ? 0 : list.size();
    }



    // Getter

    public TabPane getTabPane() { return tabPane; }


    // Setter

    public void setStatusCurrentMediaCount(List<GameDataSet> games, List<StoryDataSet> stories, List<VideoDataSet> videos) {
        String status = "Games: " + getListSize(games) + "/" + getListSize(logic.getGames()) +
                "  |  Stories: " + getListSize(stories) + "/" + getListSize(logic.getStories()) +
                "  |  Videos: " + getListSize(videos) + "/" + getListSize(logic.getVideos());
        lblStatus.setText(status);
    }

    public void updateCurrentPathLabel() {
        lblCurrentPath.setText("Current Database: " + logic.getDBName());
    }


}
