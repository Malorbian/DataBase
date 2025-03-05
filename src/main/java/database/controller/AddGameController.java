package database.controller;

import database.database_manager.DBManager;
import database.entry_manager.GameEntry;
import database.enums.State;
import io.github.palexdev.materialfx.controls.*;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javax.swing.event.ChangeListener;

public class AddGameController {

    // -----------------------------------
    // ---------- FXML Elements ----------
    // -----------------------------------

    // Game column
    @FXML
    MFXTextField tfName;
    @FXML
    MFXTextField tfLink;
    @FXML
    MFXTextField tfArtist;
    @FXML
    MFXTextField tfImage;
    @FXML
    MFXComboBox<String> cbState;

    // Tag column
    @FXML
    MFXCheckListView<String> clvTagsRight;
    @FXML
    MFXCheckListView<String> clvTagsLeft;
    @FXML
    MFXTextField tfAddTag;
    @FXML
    MFXButton btnAddTag;

    // Ratings Column
    @FXML
    MFXTableView<String> tvRatings;
    @FXML
    MFXTextField tfAddPlatform;
    @FXML
    MFXButton btnAddPlatform;

    // Bottom row
    @FXML
    Label lblStatus;
    @FXML
    Button btnAddGame;





    public void initialize() {

        // AddGame Button setup

        // Disable button until all necessary fields are filled
        btnAddGame.setDisable(true);
        // Add listener to check if all necessary fields are filled
        BooleanBinding tfBinding = tfName.textProperty().isNotEmpty().and(tfArtist.textProperty().isNotEmpty());
        BooleanBinding cbBinding = cbState.valueProperty().isNotNull();
        // Bind button to text fields and combobox
        btnAddGame.disableProperty().bind(tfBinding.and(cbBinding).not());
        // Add action event to button to add game to database
        btnAddGame.setOnAction(event -> {
            String name = tfName.getText();
            String link = tfLink.getText();
            String artist = tfArtist.getText();
            String genre = "TODO";
            String image = tfImage.getText();
            State status = State.valueOf(cbState.getValue());
            // Add game to database
            DBManager.addGame(new GameEntry(name, artist, genre, status, link, image));
            // Close window
            btnAddGame.getScene().getWindow().hide();
        });


        // Add Tag/Platform Button setup

        // Disable buttons until text fields is filled
        btnAddPlatform.setDisable(true);
        btnAddTag.setDisable(true);
        // Add listener to check if text field is filled
        BooleanBinding tfBinding2 = tfAddPlatform.textProperty().isNotEmpty();
        BooleanBinding tfBinding3 = tfAddTag.textProperty().isNotEmpty();
        // Bind buttons to text field
        btnAddPlatform.disableProperty().bind(tfBinding2.not());
        btnAddTag.disableProperty().bind(tfBinding3.not());
        // Add action event to button to add Tag/Platform to database
        btnAddPlatform.setOnAction(event -> {
            // Add platform to database
            // TODO
        });
        btnAddTag.setOnAction(event -> {
            // Add tag to database
            // TODO
        });
    }

    // TODO
    // Add Dropdown for artists to select from existing artists in the database or add new artist if not existing

}
