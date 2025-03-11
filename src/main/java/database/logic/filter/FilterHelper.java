package database.logic.filter;

import database.enums.TriState;
import database.logic.Logic;
import database.model.propertyModels.GameDataSet;
import io.github.palexdev.materialfx.controls.MFXCheckListView;
import io.github.palexdev.materialfx.controls.MFXListView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.transformation.FilteredList;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

abstract class FilterHelper {

    Logic logic;

    MFXTextField filterTitle;
    MFXTextField filterArtist;

    MFXCheckListView<String> filterState;
    ObservableMap<String, TriState> filterMapTags;
    ObservableMap<String, TriState> filterMapGenres;


    public FilterHelper() {
        logic = Logic.getInstance();
    }


    abstract void initFilterObjects();

    abstract void setPredicates(boolean tagFilter);


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
