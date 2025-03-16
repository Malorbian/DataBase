package database.controller.addController;

import database.enums.MediaType;
import database.enums.State;
import database.enums.TableNames;
import database.model.ConsumedEntry;
import database.model.propertyModels.GameDataSet;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;

public class AddGameController extends AddControllerHelper {

    // ---------- FXML Elements ----------

    @FXML
    MFXDatePicker dpLastPlayedDate;
    @FXML
    MFXTextField tfVersion;



    public AddGameController(Stage stage) {
        super(stage);
    }


    // ----- Implementing Abstract Methods -----

    @Override
    void initArtistsComboBox() {
        initializeEditableComboBox(cbArtist, logic.getArtistsGames(), TableNames.ARTIST, MediaType.GAMES, lblStatus);
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
            String dateString = null;
            try {
                LocalDate lastPlayedDate = dpLastPlayedDate.getValue();
                dateString = lastPlayedDate.toString();
            } catch (NullPointerException ignored) {}
            ConsumedEntry lastPlayed = new ConsumedEntry(-1, dateString, tfVersion.getText());
            // Tags
            List<String> tags = clvTags.getSelectionModel().getSelectedValues();
            // Ratings
            ObservableMap<String, String> ratingsSelection = ratingsUserDataCast(fcbRatings.getUserData());
            // TODO: Image (Image needs GUI implementation)
            // Add game to database
            logic.addGame(new GameDataSet(-1, title, artist, genre, state, link, null, tags, ratingsSelection, lastPlayed));
            // Close window
            stage.close();
        };
    }

}
