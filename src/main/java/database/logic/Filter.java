package database.logic;

import database.controller.tabController.TabController;
import database.controller.tabController.VideosTabController;
import database.enums.TriState;
import database.model.propertyModels.DataSet;
import io.github.palexdev.materialfx.controls.MFXCheckListView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.TextField;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public class Filter<T extends TabController, U extends DataSet> {

    // Filter Setter Objects
    MFXTextField filterTitle;
    MFXTextField filterArtist;
    MFXCheckListView<String> filterState;
    ObservableMap<String, TriState> filterMapTags;
    ObservableMap<String, TriState> filterMapGenres;

    // Filter Lists
    ObservableList<U> unfilteredData;
    FilteredList<U> preFilteredData;
    FilteredList<U> filteredData;


    public Filter(T controller, ObservableList<U> data) {

        initDataLists(data);
        initFilterObjects(controller);
        initListener(controller);

        setPredicates(false);
    }


    // ----- Initialize Objects -----

    void initDataLists(ObservableList<U> data) {
        unfilteredData = data;
        preFilteredData = new FilteredList<>(unfilteredData, game -> true);
        filteredData = new FilteredList<>(preFilteredData, game -> true);
    }

    void initFilterObjects(T controller) {
        filterTitle = controller.getFilterObjectTitle();
        filterArtist = controller.getFilterObjectArtist();
        filterMapGenres = castToTriStateMap(controller.getFilterObjectGenres().getUserData());
        filterState = castToMFXCheckListView(controller.getFilterObjectStates().getUserData());
        filterMapTags = castToTriStateMap(controller.getFilterObjectTags().getUserData());
    }

    void initListener(T controller) {
        // Add listeners to the text fields
        controller.getFilterObjectTitle().textProperty().addListener((observable, oldValue, newValue) -> setPredicates(false));
        controller.getFilterObjectArtist().textProperty().addListener((observable, oldValue, newValue) -> setPredicates(false));

        // Add listeners to the checklist views
        unfilteredData.addListener((ListChangeListener<? super U>) c -> setPredicates(false));
        filterMapGenres.addListener((MapChangeListener<? super String, ? super TriState>) change -> setPredicates(false));
        filterState.getSelectionModel().getSelection().addListener((MapChangeListener<? super Integer, ? super String>) change -> setPredicates(false));
        filterMapTags.addListener((MapChangeListener<? super String, ? super TriState>) change -> setPredicates(true));
    }



    // ----- Filter methods -----

    void setPredicates(boolean tagFilter) {
        Set<String> selectedStates = new HashSet<>(filterState.getSelectionModel().getSelectedValues());

        if (tagFilter) {
            filteredData.setPredicate(getTagFilter());
        } else {
            preFilteredData.setPredicate(getPreFilter(selectedStates));
            filteredData.setPredicate(getTagFilter());
        }

    }

    Predicate<U> getPreFilter(Set<String> selectedStates) {
        return entry -> {
            boolean genreMatch = checkSingleTriStateMatch(entry.getGenre(), filterMapGenres);
            boolean stateMatch =  selectedStates.isEmpty() || (entry.getState() != null && selectedStates.contains(entry.getState()));
            boolean extraFilter = extraFilter(entry);
            return (filterTitle.getText().isEmpty() || entry.getTitle().toLowerCase().contains(filterTitle.getText().toLowerCase()))
                    && (filterArtist.getText().isEmpty() || entry.getArtist().toLowerCase().contains(filterArtist.getText().toLowerCase()))
                    && genreMatch
                    && stateMatch
                    && extraFilter;
        };
    }

    Predicate<U> getTagFilter() {
        return entry -> checkTriStateMatch(entry.getTags(), filterMapTags);
    }


    // ----- Getter -----

    public FilteredList<U> getFilteredData() {
        return filteredData;
    }



    // ----- Helper Methods -----

    boolean extraFilter(U entry) {
        return true;
    }

    MFXCheckListView<String> castToMFXCheckListView(Object object) {
        if (object instanceof MFXCheckListView<?>) {
            return (MFXCheckListView<String>) object;
        } else {
            throw new IllegalArgumentException("MFXCheckListView is not a MFXCheckListView");
        }
    }

    ObservableMap<String, TriState> castToTriStateMap(Object object) {
        if (object instanceof Map<?, ?>) {
            return (ObservableMap<String, TriState>) object;
        } else {
            throw new IllegalArgumentException("Map is not a Map");
        }
    }

    boolean checkTriStateMatch(List<String> tagList, ObservableMap<String, TriState> filterMap) {
        // if no filter is set, return true
        if(filterMap.isEmpty()) {
            return true;
        }
        // if the parent has no value in the field, return false if any positive state is found
        if (tagList == null || tagList.isEmpty()) {
            for (ObservableMap.Entry<String, TriState> entry : filterMap.entrySet()) {
                TriState state = entry.getValue();
                if (state == TriState.POSITIVE) {
                    return false;
                }
            }
        }
        // check if the parent should be filtered out
        Set<String> tags = new HashSet<>(tagList);
        for (ObservableMap.Entry<String, TriState> entry : filterMap.entrySet()) {
            TriState state = entry.getValue();
            // return false if an entry has a negative state
            if (state == TriState.NEGATIVE) {
                if (tags.contains(entry.getKey())) {
                    return false;
                }
            // return false if a positive flagged tag is not in entry set
            } else if (state == TriState.POSITIVE) {
                if (!tags.contains(entry.getKey())) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Check if the parent of the entry should be filtered out.
     * If the entry is null, the parent has no value in the field.
     * WARNING: This method only is used for genres. Only as long as there is only one genre per game. After that, use checkTriStateMatch
     * @param entryName the name of the entry to check in the filterMap
     * @param filterMap the map containing the filter states
     * @return false if the parent should be filtered out
     */
    boolean checkSingleTriStateMatch(String entryName, ObservableMap<String, TriState> filterMap) {
        // if entry is null (parent has no value in field) return false if any positive state is found
        if (entryName == null) {
            for (ObservableMap.Entry<String, TriState> entry : filterMap.entrySet()) {
                if (entry.getValue() == TriState.POSITIVE) {
                    return false;
                }
            }
            return true;
        }
        TriState state = filterMap.get(entryName);
        // return false if the checked Entry has a negative state
        return state != TriState.NEGATIVE;
    }

}
