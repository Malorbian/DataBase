package database.controller.tabController;

import database.controller.MainController;
import database.controller.addController.AddGameController;
import database.logic.filter.GameFilter;
import database.model.propertyModels.DataSetBase;
import database.model.propertyModels.GameDataSet;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import javafx.beans.property.ListProperty;
import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;

public class GamesTabController extends TabControllerHelper {

    // -----------------------------------
    // ---------- FXML Elements ----------
    // -----------------------------------

    @FXML
    TableView<GameDataSet> tvData;

    @FXML
    MFXButton btnFilterPlayed;
    @FXML
    MFXButton btnFilterDateComparison;
    @FXML
    MFXDatePicker dpFilterDate;

    GameFilter filter;


    public GamesTabController(Stage stage, MainController parentController) {
        super(stage, parentController);
    }


    @Override
    void init() {
        initGameTable(tvData);
        filter = new GameFilter(this);
        showGames(filter.getFilteredGames());
        btnAddEntry.setOnAction(event -> openAddGameWindow());


    }


    public void showGames(ObservableList<GameDataSet> obsGameList) {
        tvData.setItems(obsGameList);
    }


    private void initGameTable(TableView<GameDataSet> tableView) {
        tableView.getColumns().clear();
        List<String> fields = getFieldsFromClass(DataSetBase.class);
        fields.addAll(getFieldsFromClass(GameDataSet.class));
        initializeTableView(tableView, fields);
        addCellValueFactoryHelper(tableView);
    }


    // --- Setup AddGame Window ---

    // Implement these methods so openAddGameWindow() can be called
    @Override
    void setOnHiddenEvent() {
        showGames(FXCollections.observableArrayList(logic.getGames()));
    }

    @Override
    void setAddEntryController(FXMLLoader fxmlLoader, Stage stage) {
         fxmlLoader.setController(new AddGameController(stage));
    }

    private void openAddGameWindow() {
        openAddEntryWindow("/fxml/addGame.fxml");
    }


    // --- Table View Helper ---

    private void addCellValueFactoryHelper(TableView<GameDataSet> tableView) {
        TableColumn<GameDataSet, String> tagsColumn = (TableColumn<GameDataSet, String>) getColumnByName(tableView, "Tags");
        tagsColumn.setCellValueFactory(data -> {
            ListProperty<StringProperty> tags = data.getValue().tagsProperty();
            String tagsString = tags.stream()
                    .map(StringProperty::get)
                    .collect(Collectors.joining(", "));
            return new SimpleStringProperty(tagsString);
        });
        TableColumn<GameDataSet, String> ratingColumn = (TableColumn<GameDataSet, String>) getColumnByName(tableView, "Ratings");
        ratingColumn.setCellValueFactory(data -> {
            MapProperty<String, StringProperty> ratings = data.getValue().ratingsProperty();
            String ratingString = ratings.entrySet().stream()
                    .map(entry -> entry.getKey() + ": " + entry.getValue().get())
                    .collect(Collectors.joining(", "));
            return new SimpleStringProperty(ratingString);
        });
    }

    private <T> TableColumn<T, ?> getColumnByName(TableView<T> tableView, String columnName) {
        return tableView.getColumns().stream()
                .filter(column -> column.getText().equals(columnName))
                .findFirst()
                .orElse(null); // Falls keine Spalte gefunden wurde, wird null zurückgegeben
    }

}
