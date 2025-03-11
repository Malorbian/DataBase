package database.logic.filter;

import database.controller.tabController.GamesTabController;
import database.enums.State;
import database.enums.TriState;
import database.model.propertyModels.GameDataSet;
import javafx.collections.*;
import javafx.collections.transformation.FilteredList;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public class GameFilter extends FilterHelper {

    GamesTabController controller;

    ObservableList<GameDataSet> unfilteredGames;
    FilteredList<GameDataSet> filteredGames;
    FilteredList<GameDataSet> preFilteredGames;

    public GameFilter(GamesTabController controller) {
        super();

        unfilteredGames = FXCollections.observableArrayList(logic.getGames());
        preFilteredGames = new FilteredList<>(unfilteredGames, game -> true);
        filteredGames = new FilteredList<>(preFilteredGames, game -> true);

        this.controller = controller;

        initFilterObjects();


        // Add listeners to the text fields
        controller.getFilterObjectTitle().textProperty().addListener((observable, oldValue, newValue) -> setPredicates(false));
        controller.getFilterObjectArtist().textProperty().addListener((observable, oldValue, newValue) -> setPredicates(false));

        // Add listeners to the checklist views
        filterMapGenres.addListener((MapChangeListener<? super String, ? super TriState>) change -> setPredicates(false));
        filterState.getSelectionModel().getSelection().addListener((MapChangeListener<? super Integer, ? super String>) change -> setPredicates(false));
        filterMapTags.addListener((MapChangeListener<? super String, ? super TriState>) change -> setPredicates(true));

        setPredicates(false);
    }

    void initFilterObjects() {
        filterTitle = controller.getFilterObjectTitle();
        filterArtist = controller.getFilterObjectArtist();
        filterMapGenres = castToTriStateMap(controller.getFilterObjectGenres().getUserData());
        filterState = castToMFXCheckListView(controller.getFilterObjectStates().getUserData());
        filterMapTags = castToTriStateMap(controller.getFilterObjectTags().getUserData());
    }

    void setPredicates(boolean tagFilter) {
        Set<String> selectedStates = new HashSet<>(filterState.getSelectionModel().getSelectedValues());

        if (tagFilter) {
            filteredGames.setPredicate(setPredicatesHelper(selectedStates));
        } else {
            preFilteredGames.setPredicate(setPredicatesHelper(selectedStates));
            filteredGames.setPredicate(setPredicatesHelper(selectedStates));
        }

    }

    Predicate<GameDataSet> setPredicatesHelper (Set<String> selectedStates) {
        return game -> {
            boolean genreMatch = checkSingleTriStateMatch(game.getGenre(), filterMapGenres);
            boolean stateMatch =  game.getState() != null && selectedStates.contains(game.getState());
            boolean tagMatch = checkTriStateMatch(game.getTags(), filterMapTags);

            return (filterTitle.getText().isEmpty() || game.getTitle().toLowerCase().contains(filterTitle.getText().toLowerCase()))
                    && (filterArtist.getText().isEmpty() || game.getArtist().toLowerCase().contains(filterArtist.getText().toLowerCase()))
                    && genreMatch
                    && (selectedStates.isEmpty() || stateMatch)
                    && tagMatch;
        };
    }


    // ----- Getter -----

    public FilteredList<GameDataSet> getFilteredGames() { return filteredGames; }

}
