package database.controller.tabController;

import database.controller.MainController;
import database.controller.addController.AddGameController;
import database.logic.Filter;
import database.model.propertyModels.GameDataSet;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import javafx.fxml.FXML;
import javafx.stage.Stage;

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

        filter = new Filter<>(this, logic.getGames());
        btnAddEntry.setOnAction(event -> openAddGameWindow());

        updateTable();
    }

    void openAddGameWindow() {
        Stage stage = new Stage();
        openAddEntryWindow("/fxml/addGame.fxml", new AddGameController(stage), stage);
    }
}
