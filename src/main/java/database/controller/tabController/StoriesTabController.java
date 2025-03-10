package database.controller.tabController;

import database.controller.MainController;
import database.controller.addController.AddGameController;
import database.controller.addController.AddStoryController;
import database.model.propertyModels.DataSetBase;
import database.model.propertyModels.StoryDataSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.List;

public class StoriesTabController extends  TabControllerHelper {

    @FXML
    TableView<StoryDataSet> tvData;

    public StoriesTabController(Stage stage, MainController parentController) {
        super(stage, parentController);
    }

    @Override
    void init() {
        initStoryTable(tvData);
        showStories(FXCollections.observableArrayList(logic.getStories()));
        btnAddEntry.setOnAction(event -> openAddStoryWindow());
    }

    public void showStories(ObservableList<StoryDataSet> obsStoryList) {
        tvData.setItems(obsStoryList);
    }

    private void initStoryTable (TableView<StoryDataSet> tableView) {
        tableView.getColumns().clear();
        List<String> fields = getFieldsFromClass(DataSetBase.class);
        initializeTableView(tableView, fields);
    }


    // --- Setup Add Story Window ---

    @Override
    void setOnHiddenEvent() {
        showStories(FXCollections.observableArrayList(logic.getStories()));
    }

    @Override
    void setAddEntryController(FXMLLoader fxmlLoader, Stage stage) {
        fxmlLoader.setController(new AddStoryController(stage));
    }

    private void openAddStoryWindow() {
        openAddEntryWindow("/fxml/addStory.fxml");
    }


}
