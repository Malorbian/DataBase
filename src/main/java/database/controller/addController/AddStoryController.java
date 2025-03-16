package database.controller.addController;

import database.enums.MediaType;
import database.enums.State;
import database.enums.TableNames;
import database.model.propertyModels.StoryDataSet;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;

public class AddStoryController extends AddControllerHelper {


    public AddStoryController(Stage stage) {
        super(stage);
    }


    @Override
    void initArtistsComboBox() {
        initializeEditableComboBox(cbArtist, logic.getArtistsStories(), TableNames.ARTIST, MediaType.LITERATURE, lblStatus);
    }

    @Override
    BooleanBinding getSaveButtonRequirements() {
        return cbArtist.valueProperty().isNotNull().and(tfName.textProperty().isNotEmpty());
    }

    @Override
    EventHandler<ActionEvent> getSaveEntryHandler() {
        return event -> {
            logic.addStory(new StoryDataSet(-1,
                    null,
                    tfName.getText(),
                    cbArtist.getValue(),
                    cbGenre.getValue(),
                    State.valueOf(cbState.getValue()),
                    tfLink.getText(),
                    null,
                    clvTags.getSelectionModel().getSelectedValues(),
                    null));
            stage.close();
        };
    }

}
