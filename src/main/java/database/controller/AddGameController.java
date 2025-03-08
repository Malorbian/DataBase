package database.controller;

import database.enums.Discipline;
import database.enums.TableNames;
import database.model.PlayedEntry;
import database.enums.State;
import database.model.propertyModels.GameDataSet;
import database.model.propertyModels.PlatformEntry;
import io.github.palexdev.materialfx.controls.*;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.*;

public class AddGameController extends ControllerBase {

    // -----------------------------------
    // ---------- FXML Elements ----------
    // -----------------------------------



    @FXML
    BorderPane main;

    // Header
    @FXML
    MFXToggleButton tBtnPinned;

    // Game column
    @FXML
    MFXTextField tfName;
    @FXML
    MFXComboBox<String> cbArtist;
    @FXML
    MFXComboBox<String> cbGenre;
    @FXML
    MFXComboBox<String> cbState;
    @FXML
    MFXTextField tfLink;
    @FXML
    MFXTextField tfImage;
    @FXML
    MFXDatePicker dpLastPlayedDate;
    @FXML
    MFXTextField tfVersion;

    // Tag column
    @FXML
    MFXCheckListView<String> clvTagsLeft;
    @FXML
    MFXTextField tfAddTag;

    // Ratings Column
    @FXML
    TableView<PlatformEntry> tvRatings;
    @FXML
    MFXTextField tfAddPlatform;

    // Bottom row
    @FXML
    Label lblStatus;
    @FXML
    Button btnCancel;
    @FXML
    Button btnSaveGame;



    public AddGameController(Stage stage) {
        super(stage);
    }



    public void initialize() {

        // Initialize GridPane
        GridPane.setHgrow(tvRatings, Priority.NEVER);
        GridPane.setVgrow(tvRatings, Priority.NEVER);
        initializeWindowDragging(stage, main);

        // Initialize Cancel Button
        btnCancel.setOnAction(event -> btnCancel.getScene().getWindow().hide());
        // Initialize Save Game Button
        initializeSaveGameButton();

        // Initialize Artist/Genre Editable ComboBox and State ComboBox
        initializeEditableComboBox(cbArtist, logic.getArtistsNames(), TableNames.ARTIST, Discipline.GAMES);
        initializeEditableComboBox(cbGenre, logic.getGenres(), TableNames.GENRE);
        // Get State enum values as List
        initializeComboBox(cbState, State.getValues());

        // Initialize Tags CheckListViews
        initializeCheckListView(clvTagsLeft, logic.getTags());

        // Initialize Pinning ToggleButton
        tBtnPinned.setOnAction(event -> {
            Stage stage = (Stage) tBtnPinned.getScene().getWindow();
            stage.setAlwaysOnTop(tBtnPinned.isSelected());
        });


        // Initialize Rating TableView

        List<String> ratingsColumnNames = getFieldsFromClass(PlatformEntry.class);
        initializeTableView(tvRatings, ratingsColumnNames);
        tvRatings.setEditable(true);
        setColumnSizes(tvRatings, Arrays.asList(0.7, 0.3));
        // Make Rating column editable
        TableColumn<PlatformEntry, String> ratingColumn = (TableColumn<PlatformEntry, String>) tvRatings.getColumns().get(1);
        ratingColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        ratingColumn.setOnEditCommit(event -> {
            PlatformEntry pe = event.getRowValue();
            pe.setRating(event.getNewValue());
        });
        // Add platforms to TableView
        for (String platform : logic.getPlatforms()) {
            tvRatings.getItems().add(new PlatformEntry(platform));
        }


        // Add Tag/Platform Button setup

        tfAddPlatform.setOnAction(event -> {
            // Add platform to database and logic
            logic.addStringToTable(tfAddPlatform.getText(), TableNames.PLATFORM);
            // Add platform to TableView
            tvRatings.getItems().add(new PlatformEntry(tfAddPlatform.getText()));
            tfAddPlatform.clear();
        });
        tfAddTag.setOnAction(event -> {
            // Add tag to database and logic
            logic.addStringToTable(tfAddTag.getText(), TableNames.TAG);
            // Add tag to CheckListView
            clvTagsLeft.getItems().add(tfAddTag.getText());
            tfAddTag.clear();
        });

    }

    private void initializeSaveGameButton() {
        // Disable button until all necessary fields are filled
        btnSaveGame.setDisable(true);
        // Add listener to check if all necessary fields are filled
        BooleanBinding saveGameBinding = cbArtist.valueProperty().isNotNull().and(
                cbState.valueProperty().isNotNull().and(
                        tfName.textProperty().isNotEmpty()
                ));
        // Bind button to text fields and combobox
        btnSaveGame.disableProperty().bind(saveGameBinding.not());
        // Add action event to button to add game to database
        btnSaveGame.setOnAction(getSaveGameHandler());
    }


    private EventHandler<ActionEvent> getSaveGameHandler() {
        return event -> {
            // Game
            String title = tfName.getText();
            String artist = cbArtist.getValue();
            String genre = cbGenre.getValue();
            State state = State.valueOf(cbState.getValue());
            String link = tfLink.getText();
            String image = tfImage.getText();
            String dateString = null;
            LocalDate lastPlayedDate = dpLastPlayedDate.getValue();
            if (lastPlayedDate != null) {
                dateString = lastPlayedDate.toString();
            }
            PlayedEntry lastPlayed = new PlayedEntry(-1, dateString, tfVersion.getText());
            // Tags
            List<String> tags = clvTagsLeft.getSelectionModel().getSelectedValues();
            //tags.addAll(clvTagsRight.getItems());
            // Ratings
            Map<String, String> ratings = new HashMap<>();
            tvRatings.getItems().forEach(entry -> ratings.put(entry.getPlatform(), entry.getRating()));
            // Add game to database
            logic.addGame(new GameDataSet(-1, title, artist, genre, state, link, image, ratings, tags, lastPlayed));
            // Close window
            btnSaveGame.getScene().getWindow().hide();
        };
    }



}
