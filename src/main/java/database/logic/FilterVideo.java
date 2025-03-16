package database.logic;

import database.controller.tabController.VideosTabController;
import database.model.propertyModels.VideoDataSet;
import javafx.collections.ObservableList;
import javafx.scene.control.TextField;

public class FilterVideo extends Filter<VideosTabController, VideoDataSet> {

    TextField filterLengthMin;
    TextField filterLengthMax;

    public FilterVideo(VideosTabController controller, ObservableList<VideoDataSet> data) {
        super(controller, data);
    }

    @Override
    void initFilterObjects(VideosTabController controller) {
        super.initFilterObjects(controller);
        filterLengthMin = controller.getFilterObjectLengthMin();
        filterLengthMax = controller.getFilterObjectLengthMax();
    }

    @Override
    void initListener(VideosTabController controller) {
        super.initListener(controller);
        filterLengthMin.textProperty().addListener((observable, oldValue, newValue) -> setPredicates(false));
        filterLengthMax.textProperty().addListener((observable, oldValue, newValue) -> setPredicates(false));
    }

    @Override
    boolean extraFilter(VideoDataSet entry) {
        double length;
        try {
            length = Double.parseDouble(entry.getLength());
        } catch (NumberFormatException e) {
            return true;
        }
        double min;
        double max;
        try {
            min = Double.parseDouble(filterLengthMin.getText());
        } catch (NumberFormatException e) {
            min = 0;
        }
        try {
            max = Double.parseDouble(filterLengthMax.getText());
        } catch (NumberFormatException e) {
            max = Double.MAX_VALUE;
        }

        return length >= min && length <= max;
    }
}
