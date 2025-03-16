package database.controller.tabController;

import database.controller.ControllerHelper;
import database.controller.MainController;
import database.controller.addController.AddEntryController;
import database.enums.State;
import database.logic.Filter;
import database.model.propertyModels.DataSet;
import database.model.propertyModels.DataSetBase;
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
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public abstract class TabControllerHelper<T extends DataSet, U extends TabController, V extends AddEntryController> extends ControllerHelper implements TabController{

    // -----------------------------------
    // ---------- FXML Elements ----------
    // -----------------------------------

    // Grid Pane for the Game Tab
    @FXML
    GridPane gridPaneRoot;

    // Table View
    @FXML
    TableView<T> tvData;

    // Table Column Selection
    @FXML
    MFXFilterComboBox<String> fcBoxTableColumns;

    // Add Entry Button
    @FXML
    MFXButton btnAddEntry;

    // Filter
    @FXML
    MFXTextField tfFilterName;
    @FXML
    MFXTextField tfFilterArtist;
    @FXML
    MFXFilterComboBox<String> fcBoxFilterGenres;
    @FXML
    MFXFilterComboBox<String> fcBoxFilterStates;
    @FXML
    MFXFilterComboBox<String> fcBoxFilterTags;


    Filter<U, T> filter;

    MainController parentController;


    public TabControllerHelper(Stage stage, MainController parentController) {
        super(stage);
        this.parentController = parentController;
    }


    public void initialize() {
        // Bind Size of GridPane to TabPane
        gridPaneRoot.prefWidthProperty().bind(parentController.getTabPane().widthProperty());
        gridPaneRoot.prefHeightProperty().bind(parentController.getTabPane().heightProperty());
        gridPaneRoot.setPadding(new Insets(0,0,30,0));

        initTriStateListViewComboBox(fcBoxFilterGenres, logic.getGenres());
        initializeCheckListComboBox(fcBoxFilterStates, State.getValues());
        initTriStateListViewComboBox(fcBoxFilterTags, logic.getTags());

        init();
    }

    void initTable(List<String> typeFields) {
        tvData.setPlaceholder(new Label("Load database or change filter settings"));
        tvData.getColumns().clear();
        List<String> fields = getFieldsFromClass(DataSetBase.class);
        fields.addAll(typeFields);
        initializeTableView(tvData, fields);
        initCellFactory(tvData);
    }

    void initCellFactory(TableView<T> tvData) {
        // Tags Column
        TableColumn<T, String> tagsColumn = (TableColumn<T, String>) getColumnByName(tvData, "Tags");
        tagsColumn.setCellValueFactory(data -> {
            ListProperty<StringProperty> tags = data.getValue().tagsProperty();
            String tagsString = tags.stream()
                    .map(StringProperty::get)
                    .collect(Collectors.joining(", "));
            return new SimpleStringProperty(tagsString);
        });
        // Ratings Column
        TableColumn<T, String> ratingColumn = (TableColumn<T, String>) getColumnByName(tvData, "Ratings");
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


    abstract void init();


    void openAddEntryWindow(String fxmlPath, V controller, Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
            fxmlLoader.setController(controller);
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


    public MFXTextField getFilterObjectTitle() { return tfFilterName; }

    public MFXTextField getFilterObjectArtist() { return tfFilterArtist; }

    public MFXFilterComboBox<String> getFilterObjectGenres() { return fcBoxFilterGenres; }

    public MFXFilterComboBox<String> getFilterObjectStates() { return fcBoxFilterStates; }

    public MFXFilterComboBox<String> getFilterObjectTags() { return fcBoxFilterTags; }


}
