package database.controller.addController;

import database.enums.Discipline;
import database.enums.TableNames;
import database.model.PlayedEntry;
import database.enums.State;
import database.model.propertyModels.GameDataSet;
import database.model.propertyModels.PlatformEntry;
import io.github.palexdev.materialfx.controls.*;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.*;

public class AddGameController extends AddControllerHelper {

    // ---------- FXML Elements ----------

    @FXML
    MFXTextField tfImage;
    @FXML
    MFXDatePicker dpLastPlayedDate;
    @FXML
    MFXTextField tfVersion;

    // Ratings Column
    @FXML
    TableView<PlatformEntry> tvRatings;
    @FXML
    MFXTextField tfAddPlatform;


    public AddGameController(Stage stage) {
        super(stage);
    }


    @Override
    public void initialize() {

        super.initialize();

        // Initialize Rating TableView
        GridPane.setHgrow(tvRatings, Priority.NEVER);
        GridPane.setVgrow(tvRatings, Priority.NEVER);
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

        // Add Platform Button Functionality

        tfAddPlatform.setOnAction(event -> {
            // Add platform to database and logic
            logic.addStringToTable(tfAddPlatform.getText(), TableNames.PLATFORM);
            // Add platform to TableView
            tvRatings.getItems().add(new PlatformEntry(tfAddPlatform.getText()));
            tfAddPlatform.clear();
        });
    }


    // ----- Implementing Abstract Methods -----

    @Override
    void initArtistsComboBox() {
        initializeEditableComboBox(cbArtist, logic.getArtistsGames(), TableNames.ARTIST, Discipline.GAMES, lblStatus);
    }

    @Override
    BooleanBinding getSaveButtonRequirements() {
        return cbArtist.valueProperty().isNotNull().and(
                cbState.valueProperty().isNotNull().and(
                        tfName.textProperty().isNotEmpty()
                ));
    }

    @Override
    EventHandler<ActionEvent> getSaveEntryHandler() {
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
            List<String> tags = clvTags.getSelectionModel().getSelectedValues();
            // Ratings
            Map<String, String> ratings = new HashMap<>();
            tvRatings.getItems().forEach(entry -> ratings.put(entry.getPlatform(), entry.getRating()));
            // Add game to database
            logic.addGame(new GameDataSet(-1, title, artist, genre, state, link, image, ratings, tags, lastPlayed));
            // Close window
            stage.close();
        };
    }

}
