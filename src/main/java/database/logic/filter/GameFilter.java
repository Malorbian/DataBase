package database.logic.filter;

import database.controller.tabController.GamesTabController;
import io.github.palexdev.materialfx.controls.MFXCheckListView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.MapChangeListener;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GameFilter extends FilterHelper {

    GamesTabController controller;


    public GameFilter(GamesTabController controller) {
        super();

        this.controller = controller;

        initFilterObjects();


        // Add listeners to the text fields
        controller.getFilterObjectTitle().textProperty().addListener((observable, oldValue, newValue) -> setPredicates());
        controller.getFilterObjectArtist().textProperty().addListener((observable, oldValue, newValue) -> setPredicates());

        // Add listeners to the checklist views
        filterGenre.getSelectionModel().getSelection().addListener((MapChangeListener<? super Integer, ? super String>) change -> setPredicates());
        filterState.getSelectionModel().getSelection().addListener((MapChangeListener<? super Integer, ? super String>) change -> setPredicates());
        filterTags.getSelectionModel().getSelection().addListener((MapChangeListener<? super Integer, ? super String>) change -> setPredicates());

        setPredicates();
    }

    void initFilterObjects() {
        filterTitle = controller.getFilterObjectTitle();
        filterArtist = controller.getFilterObjectArtist();
        filterGenre = castToMFXCheckListView(controller.getFilterObjectGenres().getUserData());
        filterState = castToMFXCheckListView(controller.getFilterObjectStates().getUserData());
        filterTags = castToMFXCheckListView(controller.getFilterObjectTags().getUserData());
    }

    void setPredicates() {
        List<String > selectedGenres = filterGenre.getSelectionModel().getSelectedValues();
        List<String > selectedStates = filterState.getSelectionModel().getSelectedValues();
        List<String > selectedTags = filterTags.getSelectionModel().getSelectedValues();

        filteredGames.setPredicate(game -> {
            // Check for null values in game
            boolean genreMatch = game.getGenre() != null && selectedGenres.contains(game.getGenre());
            // state cannot be null (because not null in sql). But we check it anyway for safety
            boolean stateMatch = game.getState() != null && selectedStates.contains(game.getState());
            boolean tagMatch = game.getTags() != null && game.getTags().stream().anyMatch(selectedTags::contains);

            return (filterTitle.getText().isEmpty() || game.getTitle().toLowerCase().contains(filterTitle.getText().toLowerCase()))
                    && (filterArtist.getText().isEmpty() || game.getArtist().toLowerCase().contains(filterArtist.getText().toLowerCase()))
                    && (selectedGenres.isEmpty() || genreMatch)
                    && (selectedStates.isEmpty() || stateMatch)
                    && (selectedTags.isEmpty() || tagMatch);
        }
        );
    }
}
