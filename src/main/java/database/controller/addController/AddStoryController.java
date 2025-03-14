package database.controller.addController;

import database.enums.Discipline;
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
        initializeEditableComboBox(cbArtist, logic.getArtistsStories(), TableNames.ARTIST, Discipline.STORIES, lblStatus);
    }

    @Override
    BooleanBinding getSaveButtonRequirements() {
        return cbArtist.valueProperty().isNotNull().and(tfName.textProperty().isNotEmpty());
    }

    @Override
    EventHandler<ActionEvent> getSaveEntryHandler() {
        return event -> {
            logic.addStory(new StoryDataSet(-1,
                    tfName.getText(),
                    cbArtist.getValue(),
                    cbGenre.getValue(),
                    State.valueOf(cbState.getValue()),
                    tfLink.getText(),
                    clvTags.getSelectionModel().getSelectedValues()));
            stage.close();
        };
    }

}
