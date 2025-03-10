package database.controller.tabController;

import database.controller.MainController;
import database.controller.addController.AddGameController;
import database.controller.addController.AddVideoController;
import database.model.propertyModels.DataSetBase;
import database.model.propertyModels.VideoDataSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.List;

public class VideosTabController extends TabControllerHelper{

    @FXML
    TableView<VideoDataSet> tvData;

    @FXML
    TextField tfLengthMin;
    @FXML
    TextField tfLengthMax;

    public VideosTabController(Stage stage, MainController parentController) {
        super(stage, parentController);
    }

    @Override
    void init() {
        initVideoTable(tvData);
        showVideos(FXCollections.observableArrayList(logic.getVideos()));
        btnAddEntry.setOnAction(event -> openAddVideoWindow());
    }

    public void showVideos(ObservableList<VideoDataSet> obsVideoList) {
        tvData.setItems(obsVideoList);
    }

    private void initVideoTable(TableView<VideoDataSet> tableView) {
        tableView.getColumns().clear();
        List<String> fields = getFieldsFromClass(DataSetBase.class);
        fields.addAll(getFieldsFromClass(VideoDataSet.class));
        initializeTableView(tableView, fields);
    }


    // --- Setup Add Video Window ---

    @Override
    void setOnHiddenEvent() {
        showVideos(FXCollections.observableArrayList(logic.getVideos()));
    }

    @Override
    void setAddEntryController(FXMLLoader fxmlLoader, Stage stage) {
        fxmlLoader.setController(new AddVideoController(stage));
    }

    private void openAddVideoWindow() {
        openAddEntryWindow("/fxml/addVideo.fxml");
    }
}
