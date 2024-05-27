package it.polimi.ingsw.model.deck.card.playablecard.corner;

import java.io.Serializable;

/**
 * This class holds all the information about a single corner
 *
 */
public record Corner(CornerType type, Symbol symbol) implements Serializable {

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Corner corner)) {
            return false;
        }
        return this.type == corner.type && this.symbol == corner.symbol;
    }
}