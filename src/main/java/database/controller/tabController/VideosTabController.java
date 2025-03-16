package database.controller.tabController;

import database.controller.MainController;
import database.controller.addController.AddVideoController;
import database.logic.FilterVideo;
import database.model.propertyModels.VideoDataSet;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class VideosTabController extends TabControllerHelper<VideoDataSet, VideosTabController, AddVideoController> {

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
        filter = new FilterVideo(this, logic.getVideos());
        btnAddEntry.setOnAction(event -> openAddVideoWindow());

        updateTable();
    }

    void openAddVideoWindow() {
        Stage stage = new Stage();
        openAddEntryWindow("/fxml/addVideo.fxml", new AddVideoController(stage), stage);
    }

    public TextField getFilterObjectLengthMin() {return tfLengthMin;}

    public TextField getFilterObjectLengthMax() {return tfLengthMax;}
}
