package database.controller;

import database.database_manager.DBManager;
import database.entry_manager.GameEntry;
import database.logic.Logic;
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

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private TableView<GameEntry> table;
    private DBManager dbManager;
    private Logic logic;

    @FXML
    private TableView<GameEntry> tabViewMain;
    @FXML
    private Button BtnAddGame;



    public MainController() {
        Logic logic = Logic.getInstance();
        dbManager = DBManager.getInstance();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        addListeners();
        showGames();
    }

    private void addListeners() {
        BtnAddGame.setOnAction(event -> addGame());
    }

    private void addGame() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/addGame.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            stage.setOnHidden(event -> showGames());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showGames() {
        tabViewMain.getColumns().clear();
        addColumn("Title", "title");
        addColumn("Artist", "artist");
        addColumn("State", "state");
        addColumn("Link", "link");
        addColumn("Image Path", "imagePath");

        System.out.println("Convert List to ObservableList");
        ObservableList<GameEntry> obsGameList = FXCollections.observableArrayList(DBManager.getAllGames());
        System.out.println("Convertion success. Set items");
        tabViewMain.setItems(obsGameList);
    }

    private void addColumn(String title, String property) {
        TableColumn<GameEntry, String> column = new TableColumn<>(title);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        tabViewMain.getColumns().add(column);
    }

}
