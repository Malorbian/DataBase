package database.controller;


import database.enums.MediaType;
import database.model.DataSet;
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
import java.util.*;

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
    Label lblMediaCount;
    @FXML
    Label lblCurrentPath;


    // ---------- Non-FXML ----------

    // Controllers
    Map<MediaType, TabController> tabControllers = new HashMap<>();

    TabController currentTab;



    public MainController (Stage stage) {
        super(stage);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initTabs();
        initMenuItems();
        initWindowButtons();
        initializeWindowDragging(stage, rootPane);
        updateCurrentPathLabel();
        updateMediaCountLabel();
    }

    private void initTabs() {
        tabPane.getTabs().clear();
        for (MediaType mediaType : MediaType.values()) {
            TabController tabController = new TabController(stage, this, mediaType);
            tabControllers.put(mediaType, tabController);
            // Create new Tab for each MediaType
            Tab tab = new Tab(mediaType.toString());
            tab.setOnSelectionChanged(event -> {tabController.updateTable();});

            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/tab.fxml"));
                fxmlLoader.setController(tabController);
                Parent content = fxmlLoader.load();
                tab.setContent(content);
                tabPane.getTabs().add(tab);
            } catch (IOException e) {
                e.printStackTrace();
            }

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
                currentTab = tabControllers.get(MediaType.GAMES);
                currentTab.updateTable();
                updateCurrentPathLabel();
                updateMediaCountLabel();
                lblStatus.setText("Database opened: " + file.getPath());
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
                lblStatus.setText("Database created at " + file.getPath());
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


    private int getListSize(List<?> list) {
        return list == null ? 0 : list.size();
    }



    // Getter

    public TabPane getTabPane() { return tabPane; }


    // Setter


    public void updateCurrentPathLabel() {
        lblCurrentPath.setText("Current Database: " + logic.getDBName());
    }

    private void updateMediaCountLabel() {
        StringBuilder sb = new StringBuilder();
        for (MediaType mediaType : MediaType.values()) {
            sb.append(mediaType.toString()).append(": ")
                    .append(tabControllers.get(mediaType).getFilter().getFilteredData().size()).append("|")
                    .append(logic.getMediaEntries(mediaType).size())
                    .append(")   ");
        }
        lblMediaCount.setText(sb.substring(0, sb.length() - 2));
    }


}
