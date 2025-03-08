package database.enums;

import java.util.ArrayList;
import java.util.List;

public enum State {
    FIN,
    DEV,
    UNKNOWN;

    public static List<String> getValues() {
        List<String> states = new ArrayList<>();
        for (State state : State.values()) {
            states.add(state.toString());
        }
        return states;
    }
}
