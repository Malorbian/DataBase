package database.controller.addController;

import database.enums.MediaType;
import database.enums.State;
import database.enums.TableNames;
import database.model.propertyModels.VideoDataSet;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class AddVideoController extends AddControllerHelper {

    @FXML
    MFXTextField tfLength;


    public AddVideoController(Stage stage) {super(stage);}


    @Override
    void initArtistsComboBox() {
        initializeEditableComboBox(cbArtist, logic.getArtistsVideos(), TableNames.ARTIST, MediaType.VIDEO, lblStatus);
    }

    @Override
    BooleanBinding getSaveButtonRequirements() {
        return cbArtist.valueProperty().isNotNull().and(tfName.textProperty().isNotEmpty());
    }

    @Override
    EventHandler<ActionEvent> getSaveEntryHandler() {
        return event -> {
            try {
                State state;
                if (cbState.getValue() != null) {
                    state = State.valueOf(cbState.getValue());
                } else {
                    state = State.UNKNOWN;
                }
                logic.addVideo(new VideoDataSet(-1,
                        tfName.getText(),
                        null,
                        cbArtist.getValue(),
                        cbGenre.getValue(),
                        state,
                        tfLink.getText(),
                        null,
                        clvTags.getSelectionModel().getSelectedValues(),
                        null,
                        Double.parseDouble(tfLength.getText())));
                stage.close();
            } catch (NumberFormatException e) {
                lblStatus.setText("Length must be a Number (Double)");
            }
        };
    }
}
