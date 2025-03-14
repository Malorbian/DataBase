package database.controller.addController;

import database.enums.Discipline;
import database.enums.State;
import database.enums.TableNames;
import database.model.propertyModels.VideoDataSet;
import io.github.palexdev.materialfx.controls.MFXComboBox;
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
        initializeEditableComboBox(cbArtist, logic.getArtistsVideos(), TableNames.ARTIST, Discipline.VIDEOS, lblStatus);
    }

    @Override
    BooleanBinding getSaveButtonRequirements() {
        return cbArtist.valueProperty().isNotNull().and(tfName.textProperty().isNotEmpty());
    }

    @Override
    EventHandler<ActionEvent> getSaveEntryHandler() {
        return event -> {
            try {
                logic.addVideo(new VideoDataSet(-1,
                        tfName.getText(),
                        cbArtist.getValue(),
                        cbGenre.getValue(),
                        State.valueOf(cbState.getValue()),
                        tfLink.getText(),
                        clvTags.getSelectionModel().getSelectedValues(),
                        Double.parseDouble(tfLength.getText())));
                stage.close();
            } catch (NumberFormatException e) {
                lblStatus.setText("Length must be a Number (Double)");
            }
        };
    }
}
