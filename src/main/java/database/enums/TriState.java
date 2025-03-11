package database.enums;

public enum TriState {

    POSITIVE("✔"), NEGATIVE("❌"), NEUTRAL("➖");

    private final String symbol;

    TriState(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }

    // Tri State Logic (Cycle: NEUTRAL -> POSITIVE -> NEGATIVE -> NEUTRAL)
    public static TriState next(TriState current) {
        return switch (current) {
            case NEUTRAL -> POSITIVE;
            case POSITIVE -> NEGATIVE;
            case NEGATIVE -> NEUTRAL;
        };
    }
}
