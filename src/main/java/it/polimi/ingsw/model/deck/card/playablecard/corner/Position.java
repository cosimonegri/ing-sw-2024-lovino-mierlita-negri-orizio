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

    public boolean isTop() {
        return this.val == 0 || this.val == 1;
    }

    public boolean isBottom() {
        return this.val == 2 || this.val == 3;
    }

    public boolean isLeft() {
        return this.val == 0 || this.val == 2;
    }

    public boolean isRight() {
        return this.val == 1 || this.val == 3;
    }
}
