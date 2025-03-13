package database.controller.addController;

import database.controller.ControllerHelper;
import database.enums.State;
import database.enums.TableNames;
import io.github.palexdev.materialfx.controls.MFXCheckListView;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.MFXToggleButton;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public abstract class AddControllerHelper extends ControllerHelper implements AddEntryController{

    @FXML
    BorderPane rootPane;
    @FXML
    MFXToggleButton tBtnPinned;

    // Entry Column
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


    public AddControllerHelper(Stage stage) { super(stage); }


    public void initialize() {
        // Add dragging functionality to the window
        initializeWindowDragging(stage, rootPane);

        // Initialize Cancel/Save buttons
        btnCancel.setOnAction(event -> btnCancel.getScene().getWindow().hide());
        initializeSaveEntryButton();

        // Initialize Pinning ToggleButton
        tBtnPinned.setOnAction(event -> {
            Stage stage = (Stage) tBtnPinned.getScene().getWindow();
            stage.setAlwaysOnTop(tBtnPinned.isSelected());
        });

        // Initialize Tags CheckListViews
        initializeCheckListView(clvTags, logic.getTags());

        // Initialize Artist/Genre/State Combo boxes
        initArtistsComboBox();
        initializeEditableComboBox(cbGenre, logic.getGenres(), TableNames.GENRE, lblStatus);
        initializeComboBox(cbState, State.getValues());

        // Initialize Add Tag Functionality
        tfAddTag.setOnAction(event -> {
            // Add tag to database and logic
            logic.addStringToTable(tfAddTag.getText(), TableNames.TAG);
            // Add tag to CheckListView
            clvTags.getItems().add(tfAddTag.getText());
            tfAddTag.clear();
        });

    }

    private void initializeSaveEntryButton() {
        // Disable button until all necessary fields are filled
        btnSaveEntry.setDisable(true);
        BooleanBinding saveGameBinding = getSaveButtonRequirements();
        // Bind button to text fields and combobox
        btnSaveEntry.disableProperty().bind(saveGameBinding.not());
        // Add action event to button to add game to database
        btnSaveEntry.setOnAction(getSaveEntryHandler());
    }

    abstract void initArtistsComboBox();

    abstract BooleanBinding getSaveButtonRequirements();

    abstract EventHandler<ActionEvent> getSaveEntryHandler();



}
