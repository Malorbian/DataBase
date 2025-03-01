package database.controller;

import com.sun.tools.javac.Main;
import database.database_manager.DatabaseManagerMain;
import database.entry_manager.GameEntry;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private TableView<GameEntry> table;
    private DatabaseManagerMain dbManager;

    public MainController() {
        dbManager = DatabaseManagerMain.getInstance();
    }

    public void start(Stage stage) throws IOException {
        FXMLLoader mainScreenLoader = new FXMLLoader(Main.class.getResource("FXML/main.fxml"));
        Scene scene = new Scene(mainScreenLoader.load(), 1920, 1080);
        stage.setTitle("SpinFood");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

}
