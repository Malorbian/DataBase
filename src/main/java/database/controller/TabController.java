package database.controller;

import database.enums.MediaType;
import database.enums.State;
import database.logic.Filter;
import database.logic.Logic;
import database.model.DataSet;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.beans.property.ListProperty;
import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class TabController extends ControllerHelper{

    // -----------------------------------
    // ---------- FXML Elements ----------
    // -----------------------------------

    // Grid Pane for the Game Tab
    @FXML
    GridPane gridPaneRoot;

    // Table View
    @FXML
    TableView<DataSet> tvData;

    // Table Column Selection
    @FXML
    MFXFilterComboBox<String> fcBoxTableColumns;

    // Add Entry Button
    @FXML
    MFXButton btnAddEntry;

    // Filter
    @FXML
    MFXFilterComboBox<String> fcBoxFilterType;
    @FXML
    MFXFilterComboBox<String> fcBoxFilterFranchise;
    @FXML
    MFXTextField tfFilterName;
    @FXML
    MFXTextField tfFilterArtist;
    @FXML
    MFXButton btnFilterConsumed;
    @FXML
    MFXButton btnFilterDateComparison;
    @FXML
    MFXTextField tfFilterDate;
    @FXML
    MFXFilterComboBox<String> fcBoxFilterGenres;
    @FXML
    MFXFilterComboBox<String> fcBoxFilterStates;
    @FXML
    MFXFilterComboBox<String> fcBoxFilterTags;
    @FXML
    TextField tfFilterLengthMin;
    @FXML
    TextField tfFilterLengthMax;
    @FXML
    MFXButton btnFilterReset;
    @FXML
    MFXButton btnFilterSaveAsDefault;


    Filter filter;

    MainController parentController;

    MediaType mediaType;


    public TabController(Stage stage, MainController parentController, MediaType mediaType) {
        super(stage);
        this.mediaType = mediaType;
        this.parentController = parentController;
    }


    public void initialize() {
        // Bind Size of GridPane to TabPane
        gridPaneRoot.prefWidthProperty().bind(parentController.getTabPane().widthProperty());
        gridPaneRoot.prefHeightProperty().bind(parentController.getTabPane().heightProperty());
        gridPaneRoot.setPadding(new Insets(0,0,30,0));

        // Initialize Filter Selectors
        initTriStateListViewComboBox(fcBoxFilterGenres, logic.getGenres(mediaType));
        initializeCheckListComboBox(fcBoxFilterStates, State.getValues());
        initTriStateListViewComboBox(fcBoxFilterTags, logic.getTags(mediaType));

        // Initialize Filter Buttons (Reset / Set Default)

        // Initialize Data Table
        initTable();

        // Initialize Filter
        this.filter = new Filter(this, mediaType);


        // Initialize Add Media Entry Button
        btnAddEntry.setOnAction(event -> openAddEntryWindow("/fxml/addEntry.fxml"));
    }

    void initTable() {
        tvData.setPlaceholder(new Label("Load database or change filter settings"));
        tvData.getColumns().clear();
        List<String> fields = getFieldsFromClass(DataSet.class);
        initializeTableView(tvData, fields);
        initCellFactory(tvData);
    }

    void initCellFactory(TableView<DataSet> tvData) {
        // Tags Column
        TableColumn<DataSet, String> tagsColumn = (TableColumn<DataSet, String>) getColumnByName(tvData, "Tags");
        tagsColumn.setCellValueFactory(data -> {
            ListProperty<StringProperty> tags = data.getValue().tagsProperty();
            String tagsString = tags.stream()
                    .map(StringProperty::get)
                    .collect(Collectors.joining(", "));
            return new SimpleStringProperty(tagsString);
        });
        // Ratings Column
        TableColumn<DataSet, String> ratingColumn = (TableColumn<DataSet, String>) getColumnByName(tvData, "Ratings");
        ratingColumn.setCellValueFactory(data -> {
            MapProperty<String, StringProperty> ratings = data.getValue().ratingsProperty();
            String ratingString = ratings.entrySet().stream()
                    .map(entry -> entry.getKey() + ": " + entry.getValue().get())
                    .collect(Collectors.joining(", "));
            return new SimpleStringProperty(ratingString);
        });
    }

    <T> TableColumn<T, ?> getColumnByName(TableView<T> tableView, String columnName) {
        return tableView.getColumns().stream()
                .filter(column -> column.getText().equals(columnName))
                .findFirst()
                .orElse(null); // Falls keine Spalte gefunden wurde, wird null zurückgegeben
    }


    void openAddEntryWindow(String fxmlPath) {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
            fxmlLoader.setController(new AddMediaEntryController(stage, this, mediaType));
            Parent root = fxmlLoader.load();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(this.stage);
            stage.setScene(new Scene(root));
            stage.show();
            stage.setOnHidden(event -> updateTable());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateTable() {
        tvData.setItems(filter.getFilteredData());
    }



    public MFXFilterComboBox<String> getFilterObjectTypes() { return fcBoxFilterType; }

    public MFXFilterComboBox<String> getFilterObjectFranchises() { return fcBoxFilterFranchise; }

    public MFXTextField getFilterObjectTitle() { return tfFilterName; }

    public MFXTextField getFilterObjectArtist() { return tfFilterArtist; }

    public MFXButton getFilterObjectConsumed() { return btnFilterConsumed; }

    public MFXButton getFilterObjectDateComparison() { return btnFilterDateComparison; }

    public MFXTextField getFilterObjectDate() { return tfFilterDate; }

    public MFXFilterComboBox<String> getFilterObjectGenres() { return fcBoxFilterGenres; }

    public MFXFilterComboBox<String> getFilterObjectStates() { return fcBoxFilterStates; }

    public MFXFilterComboBox<String> getFilterObjectTags() { return fcBoxFilterTags; }

    public TextField getFilterObjectLengthMin() { return tfFilterLengthMin; }

    public TextField getFilterObjectLengthMax() { return tfFilterLengthMax; }


    public MediaType getMediaType() { return mediaType; }

    public Filter getFilter() { return filter; }

}
