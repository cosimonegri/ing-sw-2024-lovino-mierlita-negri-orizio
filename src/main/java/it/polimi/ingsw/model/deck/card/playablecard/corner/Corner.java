package it.polimi.ingsw.model.deck.card.playablecard.corner;

import java.util.Objects;

/**
 * This class holds all the information about a single corner
 *
 */
public record Corner(CornerType type, Symbol symbol) {
    /**
     * @param type   the type of angle: HIDDEN, VISIBLE
     * @param symbol the symbol, if present, on the corner
     * @throws NullPointerException the type cant be null
     */
    public Corner {
        if (type == null) throw new NullPointerException("CornerType cannot be null");
    }

    @Override
    public String toString() {
        return "Corner[" +
                "type=" + type + ", " +
                "symbol=" + symbol + ']';
    }
}
