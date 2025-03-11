package database.logic.filter;

import database.logic.Logic;
import database.model.propertyModels.GameDataSet;
import io.github.palexdev.materialfx.controls.MFXCheckListView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

abstract class FilterHelper {

    Logic logic;

    ObservableList<GameDataSet> unfilteredGames;
    FilteredList<GameDataSet> filteredGames;

    MFXTextField filterTitle;
    MFXTextField filterArtist;

    MFXCheckListView<String> filterGenre;
    MFXCheckListView<String> filterState;
    MFXCheckListView<String> filterTags;

    public FilterHelper() {
        logic = Logic.getInstance();
        unfilteredGames = FXCollections.observableArrayList(logic.getGames());
        filteredGames = new FilteredList<>(unfilteredGames, game -> true);
    }


    abstract void initFilterObjects();

    abstract void setPredicates();


    MFXCheckListView<String> castToMFXCheckListView(Object object) {
        if (object instanceof MFXCheckListView<?>) {
            return (MFXCheckListView<String>) object;
        } else {
            throw new IllegalArgumentException("MFXCheckListView is not a MFXCheckListView");
        }
    }

    public FilteredList<GameDataSet> getFilteredGames() { return filteredGames; }
}
