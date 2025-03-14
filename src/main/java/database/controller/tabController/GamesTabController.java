package database.controller.tabController;

import database.controller.MainController;
import database.controller.addController.AddGameController;
import database.logic.Filter;
import database.model.propertyModels.GameDataSet;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import javafx.beans.property.ListProperty;
import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.util.stream.Collectors;

public class GamesTabController extends TabControllerHelper<GameDataSet, GamesTabController, AddGameController> {

    // -----------------------------------
    // ---------- FXML Elements ----------
    // -----------------------------------

    @FXML
    MFXButton btnFilterPlayed;
    @FXML
    MFXButton btnFilterDateComparison;
    @FXML
    MFXDatePicker dpFilterDate;


    public GamesTabController(Stage stage, MainController parentController) {
        super(stage, parentController);
    }


    @Override
    void init() {
        initTable(getFieldsFromClass(GameDataSet.class));
        addCellValueFactoryHelper(tvData);

        filter = new Filter<>(this, logic.getGames());
        btnAddEntry.setOnAction(event -> openAddGameWindow());

        updateTable();
    }

    void openAddGameWindow() {
        Stage stage = new Stage();
        openAddEntryWindow("/fxml/addGame.fxml", new AddGameController(stage), stage);
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
