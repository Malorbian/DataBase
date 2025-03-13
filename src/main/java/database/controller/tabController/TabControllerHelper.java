package database.controller.tabController;

import database.controller.ControllerHelper;
import database.controller.MainController;
import database.enums.State;
import database.logic.Filter;
import database.model.propertyModels.DataSet;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public abstract class TabControllerHelper<T extends DataSet, U extends TabController> extends ControllerHelper implements TabController{

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

        initTriStateListViewComboBox(fcBoxFilterGenres, logic.getGenres());
        initializeCheckListComboBox(fcBoxFilterStates, State.getValues());
        initTriStateListViewComboBox(fcBoxFilterTags, logic.getTags());

        init();
    }

    abstract void init();

    abstract void setOnHiddenEvent();

    abstract void setAddEntryController(FXMLLoader fxmlLoader, Stage stage);

    void openAddEntryWindow(String fxmlPath) {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
            setAddEntryController(fxmlLoader, stage);
            Parent root = fxmlLoader.load();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(this.stage);
            stage.setScene(new Scene(root));
            stage.show();
            stage.setOnHidden(event -> setOnHiddenEvent());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MFXTextField getFilterObjectTitle() { return tfFilterName; }

    public MFXTextField getFilterObjectArtist() { return tfFilterArtist; }

    public MFXFilterComboBox<String> getFilterObjectGenres() { return fcBoxFilterGenres; }

    public MFXFilterComboBox<String> getFilterObjectStates() { return fcBoxFilterStates; }

    public MFXFilterComboBox<String> getFilterObjectTags() { return fcBoxFilterTags; }


}
