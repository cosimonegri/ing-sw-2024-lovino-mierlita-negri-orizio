package it.polimi.ingsw.model.deck.card.playablecard.corner;


/**
 * Enum to represent the 4 diagonal positions in a 2D space
 */
public enum Position {
    TOPLEFT(0),
    TOPRIGHT(1),
    BOTTOMLEFT(2),
    BOTTOMRIGHT(3);

    /**
     * Numeric value associated to the position
     */
    private final int val;

    Position(int val) {
        this.val = val;
    }

    public int val() { return this.val; }
}
