package database.controller.customFXElements;

import io.github.palexdev.materialfx.controls.MFXListView;
import io.github.palexdev.materialfx.controls.cell.MFXListCell;
import javafx.collections.ObservableMap;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.util.Map;

public class TfInputListCell extends MFXListCell<String> {

    private final Label label;
    private final TextField textField;

    private final ObservableMap<String, String> selectionMap;

    public TfInputListCell(String name, ObservableMap<String, String> selectionMap, MFXListView<String> cellContainer) {
        super(cellContainer, name);
        this.selectionMap = selectionMap;

        label = new Label(name);
        textField = new TextField();

        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isBlank()) {
                selectionMap.remove(name);
            } else {
                if (!newValue.isBlank()) {
                    selectionMap.put(name, newValue);
                }
            }
        });


        label.setMaxSize(107, 38);
        label.setMinSize(107, 38);
        label.setPrefSize(107, 38);
        label.setPadding(new Insets(0, 0, 0, 5));
        textField.setPrefSize(38, 25);
        textField.setMinSize(38, 25);
        textField.setMaxSize(38, 25);

        HBox cell = new HBox(label, textField);
        cell.setAlignment(Pos.CENTER_LEFT);

        getChildren().clear();
        getChildren().add(cell);
    }
}
