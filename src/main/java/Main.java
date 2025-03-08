import database.controller.MainController;
import database.database_manager.DBManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static javafx.application.Application.launch;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("FXML/main.fxml"));
        fxmlLoader.setControllerFactory(t -> new MainController(stage));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add("css/DefaultTheme.css");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        DBManager.getInstance();
        launch(args);

    }
}
