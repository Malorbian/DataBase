package database.controller.tabController;

import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;

public interface TabController {

    MFXTextField getFilterObjectTitle();
    MFXTextField getFilterObjectArtist();

    MFXFilterComboBox<String> getFilterObjectGenres();
    MFXFilterComboBox<String> getFilterObjectStates();
    MFXFilterComboBox<String> getFilterObjectTags();

}
