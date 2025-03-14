package database.controller.tabController;

import database.controller.MainController;
import database.controller.addController.AddStoryController;
import database.logic.Filter;
import database.model.propertyModels.StoryDataSet;
import javafx.stage.Stage;

public class StoriesTabController extends  TabControllerHelper<StoryDataSet, StoriesTabController, AddStoryController> {

    public StoriesTabController(Stage stage, MainController parentController) {
        super(stage, parentController);
    }

    @Override
    void init() {
        initTable(getFieldsFromClass(StoryDataSet.class));

        // TODO: filter
        filter = new Filter<>(this, logic.getStories());
        Stage stage = new Stage();
        btnAddEntry.setOnAction(event -> openAddLiteratureWindow());

        updateTable();
    }

    void openAddLiteratureWindow() {
        Stage stage = new Stage();
        openAddEntryWindow("/fxml/addStory.fxml", new AddStoryController(stage), stage);
    }

}
