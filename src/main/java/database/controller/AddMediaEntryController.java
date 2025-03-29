package database.controller;

import database.enums.AttributeTypes;
import database.enums.MediaType;
import database.enums.State;
import database.model.DataSet;
import io.github.palexdev.materialfx.controls.*;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;

public class AddMediaEntryController extends ControllerHelper{

    @FXML
    BorderPane rootPane;
    @FXML
    MFXToggleButton tBtnPinned;

    // Entry Column
    @FXML
    MFXComboBox<String> cbEntryType;
    @FXML
    MFXComboBox<String> cbFranchise;
    @FXML
    MFXTextField tfName;
    @FXML
    MFXComboBox<String> cbArtist;
    @FXML
    MFXComboBox<String> cbGenre;
    @FXML
    MFXComboBox<String> cbState;
    @FXML
    MFXTextField tfLink;
    @FXML
    MFXTextField tfLength;
    @FXML
    MFXFilterComboBox<String> fcbRatings;
    @FXML
    MFXDatePicker dpConsumedDate;
    @FXML
    MFXTextField tfConsumedVersion;

    // Tag Column
    @FXML
    MFXCheckListView<String> clvTags;
    @FXML
    MFXTextField tfAddTag;

    // Bottom
    @FXML
    Label lblStatus;
    @FXML
    Button btnCancel;
    @FXML
    Button btnSaveEntry;


    MediaType mediaType;

    TabController parentController;


    public AddMediaEntryController(Stage stage, TabController parentController, MediaType mediaType) {
        super(stage);
        this.mediaType = mediaType;
        this.parentController = parentController;
    }


    public void initialize() {
        // Add dragging functionality to the window
        initializeWindowDragging(stage, rootPane);

        // Initialize Cancel/Save buttons
        btnCancel.setOnAction(event -> stage.hide());
        initializeSaveEntryButton();

        // Initialize Pinning ToggleButton
        tBtnPinned.setOnAction(event -> {
            Stage stage = (Stage) tBtnPinned.getScene().getWindow();
            stage.setAlwaysOnTop(tBtnPinned.isSelected());
        });

        // Initialize Tags CheckListViews
        initializeCheckListView(clvTags, logic.getTags(mediaType));

        // Initialize Combo boxes
        initializeEditableComboBox(cbEntryType, logic.getEntryTypes(mediaType), AttributeTypes.ENTRY_TYPE, mediaType, lblStatus);
        initFranchiseComboBox();
        initializeEditableComboBox(cbArtist, logic.getArtists(mediaType), AttributeTypes.ARTIST, mediaType, lblStatus);
        initializeEditableComboBox(cbGenre, logic.getGenres(mediaType), AttributeTypes.GENRE, mediaType, lblStatus);
        initializeComboBox(cbState, State.getValues());
        initTfInputListViewComboBox(fcbRatings, logic.getPlatforms(mediaType));

        // Initialize Length TextField (input restriction)
        initDoubleRestriction(tfLength);

        // Initialize Add Tag Functionality
        // TODO: TF also filter for existing tags
        tfAddTag.setOnAction(event -> {
            // Add tag to database and logic
            logic.addByAttributeType(tfAddTag.getText(), AttributeTypes.TAG, mediaType);
            // Add tag to CheckListView
            clvTags.getItems().add(tfAddTag.getText());
            tfAddTag.clear();
        });
    }

    private void initDoubleRestriction(MFXTextField tf) {
        tf.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                tf.setText(oldValue);
            }
        });
    }

    private void initFranchiseComboBox() {
        ObservableList<String> franchises = FXCollections.observableArrayList();
        for (String entryType : logic.getEntryTypes(mediaType)) {
            franchises.addAll(logic.getFranchises(entryType));
        }
        initializeEditableComboBox(cbFranchise, franchises, AttributeTypes.FRANCHISE, mediaType, lblStatus);

        // Franchise filter
        cbEntryType.setOnCommit(s -> {
            try {
                if (s != null) {
                    if (!logic.getFranchises(s).isEmpty()) {
                        cbFranchise.setItems(logic.getFranchises(s));
                    }

                }
            } catch (Exception ignored) {}
        });
    }

    private void initializeSaveEntryButton() {
        // Disable button until all necessary fields are filled
        btnSaveEntry.setDisable(true);
        BooleanBinding saveEntryBinding = getSaveButtonRequirements();
        // Bind button to text fields and combobox
        btnSaveEntry.disableProperty().bind(saveEntryBinding.not());
        // Add action event to button to add game to database
        btnSaveEntry.setOnAction(getSaveEntryHandler());
    }

    ObservableMap<String, String> ratingsUserDataCast(Object object) {
        if (object instanceof ObservableMap<?, ?>) {
            return (ObservableMap<String, String>) object;
        } else {
            throw new IllegalArgumentException("UserData is not a ObservableMap: " + object.getClass());
        }
    }

    private BooleanBinding getSaveButtonRequirements() {
        return cbArtist.valueProperty().isNotNull().and(
                    tfName.textProperty().isNotEmpty().and(
                    cbEntryType.valueProperty().isNotNull()
                ));
    }

    private EventHandler<ActionEvent> getSaveEntryHandler() {
        return event -> {
            String entryType = cbEntryType.getValue();
            String franchise = cbFranchise.getValue();
            String title = tfName.getText();
            String artist = cbArtist.getValue();
            String genre = cbGenre.getValue();
            State state = State.valueOf(cbState.getValue());
            Double length;
            try {
                length = Double.parseDouble(tfLength.getText());
            } catch (NumberFormatException ignored) {
                length = -1.0;
            }
            String link = tfLink.getText();
            ObservableMap<String, String> ratingsSelection = ratingsUserDataCast(fcbRatings.getUserData());
            LocalDate consumedDate;
            try {
                // TODO: Pattern for date needs to be dd.MM.yy
                consumedDate = dpConsumedDate.getValue();
            } catch (NullPointerException ignored) {
                consumedDate = null;
            }
            String consumedVersion = tfConsumedVersion.getText();
            // Tags
            List<String> tags = clvTags.getSelectionModel().getSelectedValues();
            // Add game to database
            logic.addMediaEntry(new DataSet(-1,
                    mediaType,
                    entryType,
                    franchise,
                    title,
                    artist,
                    genre,
                    state,
                    link,
                    null,
                    length,
                    consumedDate,
                    consumedVersion,
                    tags,
                    ratingsSelection), mediaType);
            // Close window
            stage.close();
        };
    }



}
