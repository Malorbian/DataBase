package database.controller;


import database.model.propertyModels.DataSetBase;
import database.model.propertyModels.GameDataSet;
import database.logic.Logic;
import javafx.beans.property.ListProperty;
import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MainController extends ControllerBase implements Initializable {

    @FXML
    private TableView<GameDataSet> tabViewMain;
    @FXML
    private Button BtnAddGame;



    public MainController (Stage stage) {
        super(stage);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        addListeners();
        showGames();
    }

    private void addListeners() {
        BtnAddGame.setOnAction(event -> openAddGameWindow());
    }

    private void openAddGameWindow() {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/addGame.fxml"));
            fxmlLoader.setController(new AddGameController(stage));
            Parent root = fxmlLoader.load();
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();

            stage.setOnHidden(event -> showGames());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showGames() {
        tabViewMain.getColumns().clear();
        List<String> fields = getFieldsFromClass(DataSetBase.class);
        fields.addAll(getFieldsFromClass(GameDataSet.class));
        initializeTableView(tabViewMain, fields);
        addCellValueFactoryHelper();
        ObservableList<GameDataSet> obsGameList = FXCollections.observableArrayList(logic.getGames());
        tabViewMain.setItems(obsGameList);
    }

    private void addCellValueFactoryHelper() {
        TableColumn<GameDataSet, String> tagsColumn = (TableColumn<GameDataSet, String>) getColumnByName(tabViewMain, "Tags");
        tagsColumn.setCellValueFactory(data -> {
            ListProperty<StringProperty> tags = data.getValue().tagsProperty();
            String tagsString = tags.stream()
                    .map(StringProperty::get)
                    .collect(Collectors.joining(", "));
            return new SimpleStringProperty(tagsString);
        });
        TableColumn<GameDataSet, String> ratingColumn = (TableColumn<GameDataSet, String>) getColumnByName(tabViewMain, "Ratings");
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
