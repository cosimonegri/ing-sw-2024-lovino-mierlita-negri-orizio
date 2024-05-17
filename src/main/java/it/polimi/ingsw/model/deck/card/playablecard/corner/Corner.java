package it.polimi.ingsw.model.deck.card.playablecard.corner;

/**
 * This class holds all the information about a single corner
 *
 */
public record Corner(CornerType type, Symbol symbol) {

    public boolean equals(Corner corner) {
        return this.type.equals(corner.type) && (this.symbol.equals(corner.symbol));
    }
}