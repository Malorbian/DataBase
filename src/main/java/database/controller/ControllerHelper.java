package database.controller;

import database.controller.customFXElements.TfInputListCell;
import database.controller.customFXElements.TriStateListCell;
import database.enums.AttributeTypes;
import database.enums.MediaType;
import database.enums.TriState;
import database.logic.Logic;
import io.github.palexdev.materialfx.controls.MFXCheckListView;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXListView;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ControllerHelper {

    protected final Logic logic;
    protected final Stage stage;

    boolean isDraggable = true;
    private double xOffset;
    private double yOffset;


    public ControllerHelper(Stage stage) {
        logic = Logic.getInstance();
        this.stage = stage;
    }


    // ----- Utility Methods -----


    // Get all fields from a class
    protected List<String> getFieldsFromClass(Class<?> clazz) {
        List<String> fields = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            fields.add(field.getName());
        }
        return fields;
    }

    protected void initializeWindowDragging(Stage stage, BorderPane window) {
        window.setOnMousePressed(event -> {
            xOffset = stage.getX() - event.getScreenX();
            yOffset = stage.getY() - event.getScreenY();
        });
        window.setOnMouseDragged(event -> {
            if (isDraggable) {
                stage.setX(event.getScreenX() + xOffset);
                stage.setY(event.getScreenY() + yOffset);
            }
        });
    }


    // ----- Editable ComboBoxes -----

    protected void initializeComboBox(MFXComboBox<String> comboBox, List<String> items) {
        ObservableList<String> obsItemList = FXCollections.observableArrayList(items);
        comboBox.setItems(obsItemList);
    }

    protected void initializeEditableComboBox(MFXComboBox<String> comboBox, List<String> items, AttributeTypes attributeType, MediaType mediaType, Label statusLabel) {
        ObservableList<String> obsItemList = FXCollections.observableArrayList(items);
        comboBox.setItems(obsItemList);
        comboBox.setOnCancel(s -> comboBox.setText(comboBox.getSelectedItem()));
        comboBox.setOnCommit(s -> {
            try {
                if (!obsItemList.contains(s)) {
                    obsItemList.add(s);
                    logic.addByAttributeType(s, attributeType, mediaType);
                }
                comboBox.selectItem(s);
                statusLabel.setText("Added " + s + " to " + attributeType);
            } catch (Exception e) {
                statusLabel.setText("Error: " + e.getMessage());
            }
        });
    }



    // ----- CheckListView -----

    protected void initializeCheckListView(MFXCheckListView<String> checkListView, List<String> items) {
        ObservableList<String> obsItemList = FXCollections.observableArrayList(items);
        checkListView.setItems(obsItemList);
    }


    // ----- CheckList in ComboBox -----

    /**
     * Initializes a ComboBox with a CheckListView popup. Code for positioning the popup is a patchwork solution and may not work in all cases.
     * TODO: fix popup positioning and height calculation
     * @param comboBox MFXFilterComboBox that will contain the CheckListView
     * @param items List<String of items to be displayed in the CheckListView
     */
    protected void initializeCheckListComboBox(MFXFilterComboBox<String> comboBox, List<String> items) {
        ObservableList<String> obsItemList = FXCollections.observableArrayList(items);

        MFXCheckListView<String> checkListView = new MFXCheckListView<>(obsItemList);
        checkListView.setItems(obsItemList);
        checkListView.getSelectionModel().getSelection().addListener((MapChangeListener<? super Integer, ? super String>) change -> {
            String text = change.getMap().values().stream()
                    .filter(Objects::nonNull)
                    .collect(Collectors.joining(", "));
            comboBox.setText(text);
        });


        Popup popup = new Popup();
        popup.getContent().add(checkListView);
        popup.setAutoHide(true);

        comboBox.setUserData(checkListView);
        comboBox.setOnMousePressed(event -> {
            if (!comboBox.isShowing()) {
                double xPos = comboBox.localToScreen(comboBox.getBoundsInLocal()).getMinX() - 15.0;
                double yPos = comboBox.localToScreen(comboBox.getBoundsInLocal()).getMinY() + comboBox.getHeight();

                checkListView.setMinWidth(comboBox.getWidth());
                checkListView.setMaxWidth(comboBox.getWidth());
                double itemHeight = 32.0;
                double clvHeight = Math.min(itemHeight * obsItemList.size(), 300);
                checkListView.setMaxHeight(clvHeight);

                popup.show(stage, xPos, yPos);
            } else {
                popup.hide();
            };
        });

    }


    // ----- TriStateCheckList ComboBox -----
    protected void initTriStateListViewComboBox(MFXFilterComboBox<String> comboBox, List<String> items) {
        // init TriState Map
        ObservableMap<String, TriState> selectionMap = FXCollections.observableHashMap();
        items.forEach(item -> selectionMap.put(item, TriState.NEUTRAL));

        // TriStateListView
        MFXListView<String> listView = new MFXListView<>();
        listView.getItems().addAll(items);

        // Cell Factory
        listView.setCellFactory(name -> new TriStateListCell(name, selectionMap, listView));

        // Combobox as Dropdown for ListView
        setupCustomComboBoxPopup(comboBox, listView, selectionMap);
    }

    protected void initTfInputListViewComboBox(MFXFilterComboBox<String> comboBox, List<String> items) {
        // init Map
        ObservableMap<String, String> selectionMap = FXCollections.observableHashMap();
        //items.forEach(item -> selectionMap.put(item, ""));

        // ListView
        MFXListView<String> listView = new MFXListView<>();
        listView.getItems().addAll(items);

        // Cell Factory
        listView.setCellFactory(name -> new TfInputListCell(name, selectionMap, listView));

        // Combobox as Dropdown for ListView
        setupCustomComboBoxPopup(comboBox, listView, selectionMap);
    }


    // ----- TableView -----

    protected <T> void initializeTableView(TableView<T> tableView, List<String> columnNames) {
        for (String columnName : columnNames) {
            TableColumn<T, String> column = new TableColumn<>(capitalizeFirstLetter(columnName));
            column.setCellValueFactory(new PropertyValueFactory<>(columnName));
            tableView.getColumns().add(column);
        }
    }

    protected <T> void setColumnSizes(TableView<T> tableView, List<Double> columnSizes) {
        if (columnSizes.size() != tableView.getColumns().size()) {
            throw new IllegalArgumentException("Sizes list must have the same size as the number of columns in the table");
        }
        for (int i = 0; i < columnSizes.size(); i++) {
            tableView.getColumns().get(i).setPrefWidth(columnSizes.get(i) * tableView.getPrefWidth());
        }
    }

    private static String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str; // Falls null oder leer, unverändert zurückgeben
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private void setupCustomComboBoxPopup(MFXComboBox<String> comboBox, MFXListView<String> listView, ObservableMap<?, ?> selectionMap) {
        Popup popup = new Popup();
        popup.getContent().add(listView);
        popup.setAutoHide(true);
        popup.setAutoFix(true);

        comboBox.setUserData(selectionMap);
        comboBox.setOnMousePressed(event -> {
            if (!comboBox.isShowing()) {
                double xPos = comboBox.localToScreen(comboBox.getBoundsInLocal()).getMinX() - 15.0;
                double yPos = comboBox.localToScreen(comboBox.getBoundsInLocal()).getMinY() + comboBox.getHeight();

                listView.setMinWidth(comboBox.getWidth());
                listView.setMaxWidth(comboBox.getWidth());
                double itemHeight = 32.0;
                double lvHeight = Math.min(itemHeight * listView.getItems().size(), 300);
                listView.setMaxHeight(lvHeight);

                popup.show(stage, xPos, yPos);
            } else {
                popup.hide();
            };
        });

    }
}
