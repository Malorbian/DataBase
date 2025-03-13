package database.controller.tabController;

import database.controller.MainController;
import database.controller.addController.AddVideoController;
import database.model.propertyModels.DataSetBase;
import database.model.propertyModels.VideoDataSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.List;

public class VideosTabController extends TabControllerHelper<VideoDataSet, VideosTabController, AddVideoController> {

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
        initTable(getFieldsFromClass(VideoDataSet.class));

        // TODO: filter
        btnAddEntry.setOnAction(event -> openAddEntryWindow("/fxml/addVideo.fxml", new AddVideoController(stage)));

        updateTable();
    }

    public void showVideos(ObservableList<VideoDataSet> obsVideoList) {
        tvData.setItems(obsVideoList);
    }

}
