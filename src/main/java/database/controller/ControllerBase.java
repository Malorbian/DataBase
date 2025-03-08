package database.controller;

import database.enums.Discipline;
import database.enums.TableNames;
import database.logic.Logic;
import io.github.palexdev.materialfx.controls.MFXCheckListView;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ControllerBase {

    protected final Logic logic;
    protected final Stage stage;
    private double xOffset;
    private double yOffset;

    public ControllerBase(Stage stage) {
        logic = Logic.getInstance();
        this.stage = stage;
    }

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
            stage.setX(event.getScreenX() + xOffset);
            stage.setY(event.getScreenY() + yOffset);
        });
    }

    protected void initializeComboBox(MFXComboBox<String> comboBox, List<String> items) {
        ObservableList<String> obsItemList = FXCollections.observableArrayList(items);
        comboBox.setItems(obsItemList);
    }

    protected void initializeEditableComboBox(MFXComboBox<String> comboBox, List<String> items, TableNames tableName, Discipline discipline) {
        ObservableList<String> obsItemList = FXCollections.observableArrayList(items);
        comboBox.setItems(obsItemList);
        comboBox.setOnCancel(s -> comboBox.setText(comboBox.getSelectedItem()));
        comboBox.setOnCommit(s -> {
            if (!obsItemList.contains(s)) {
                obsItemList.add(s);
                logic.addStringToTable(s, tableName, discipline);
            }
            comboBox.selectItem(s);
        });
    }

    protected void initializeEditableComboBox(MFXComboBox<String> comboBox, List<String> items, TableNames tableName) {
        if (tableName == TableNames.ARTIST) {
            throw new IllegalArgumentException("Discipline must be provided for ARTIST table");
        }
        initializeEditableComboBox(comboBox, items, tableName, null);
    }

    protected void initializeCheckListView(MFXCheckListView<String> checkListView, List<String> items) {
        ObservableList<String> obsItemList = FXCollections.observableArrayList(items);
        checkListView.setItems(obsItemList);
    }

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

    protected <T> void initializeMFXTableView(MFXTableView<T> tableView, List<String> columnNames, List<Double> columnSizes) {
        double tableWidth = tableView.getPrefWidth();
        addMFXColumnsToTable(tableView, columnNames, columnSizes, t -> {
            List<StringProperty> properties = new ArrayList<>();
            for (String columnName : columnNames) {
                try {
                    Field field = t.getClass().getDeclaredField(columnName);
                    field.setAccessible(true);
                    properties.add((StringProperty) field.get(t));
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            return properties;
        });
        //setColumnSize(tableView, columnSizes, tableWidth);
    }

    protected <T> void setMFXColumnSizes(MFXTableView<T> tableView, List<Double> sizes, double tableWidth) {
        if (sizes.size() != tableView.getTableColumns().size()) {
            throw new IllegalArgumentException("Sizes list must have the same size as the number of columns in the table");
        }
        for (int i = 0; i < sizes.size(); i++) {
            tableView.getTableColumns().get(i).setPrefWidth(90);
            tableView.getTableColumns().get(i).setMaxWidth(90);
        }
    }

    private <T> void addMFXColumnsToTable(MFXTableView<T> tableView, List<String> columnNames, List<Double> columnSizes, Function<T, List<StringProperty>> propertyExtractor) {
        for (int i = 0; i < columnNames.size(); i++) {
            final int columnIndex = i; // Muss final oder effectively final sein für Lambda
            MFXTableColumn<T> column = new MFXTableColumn<>(columnNames.get(i), true);

            column.setRowCellFactory(item -> new MFXTableRowCell<>(t -> propertyExtractor.apply(t).get(columnIndex).get()));
            column.columnResizableProperty().set(false);
            column.setMaxWidth(90);
            column.setPrefWidth(90);
            tableView.getTableColumns().add(column);
        }
    }

    private static String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str; // Falls null oder leer, unverändert zurückgeben
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
