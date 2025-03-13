package database.controller.tabController;

import database.controller.MainController;
import database.controller.addController.AddStoryController;
import database.model.propertyModels.DataSetBase;
import database.model.propertyModels.StoryDataSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.util.List;

public class StoriesTabController extends  TabControllerHelper<StoryDataSet, StoriesTabController, AddStoryController> {

    public StoriesTabController(Stage stage, MainController parentController) {
        super(stage, parentController);
    }

    @Override
    void init() {
        initTable(getFieldsFromClass(StoryDataSet.class));

        // TODO: filter
        btnAddEntry.setOnAction(event -> openAddEntryWindow("/fxml/addStory.fxml", new AddStoryController(stage)));

        updateTable();
    }

}
