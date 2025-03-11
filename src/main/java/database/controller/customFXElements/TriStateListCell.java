package database.controller.customFXElements;

import database.enums.TriState;
import io.github.palexdev.materialfx.controls.MFXListView;
import io.github.palexdev.materialfx.controls.cell.MFXListCell;
import javafx.collections.ObservableMap;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.util.Map;

public class TriStateListCell extends MFXListCell<String> {

    private final Label label;
    private final Button toggleButton;
    private final ObservableMap<String, TriState> selectionMap;

    public TriStateListCell(String name, ObservableMap<String, TriState> selectionMap, MFXListView<String> cellContainer) {
        super(cellContainer, name);
        this.selectionMap = selectionMap;

        this.setMaxHeight(30);

        label = new Label(name);
        label.setMaxHeight(30);
        toggleButton = new Button(selectionMap.get(name).toString());
        toggleButton.setMaxSize(20, 20);
        toggleButton.setMinSize(20, 20);
        toggleButton.setPrefSize(20, 20);

        // Event: Zustand beim Klick wechseln
        toggleButton.setOnAction(event -> {
            TriState currentState = selectionMap.get(name);
            TriState newState = TriState.next(currentState);
            selectionMap.put(name, newState);
            toggleButton.setText(newState.toString());
        });

        toggleButton.setStyle("-fx-background-color: transparent; -fx-border-width: 2; -fx-border-color: lightgrey; -fx-border-radius: 5; -fx-text-fill: black; -fx-padding: 0 0 0 0;");


        HBox item = new HBox(10, toggleButton, label);
        getChildren().clear();
        getChildren().add(item);
    }
}
