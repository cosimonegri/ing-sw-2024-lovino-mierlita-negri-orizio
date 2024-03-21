package it.polimi.ingsw.model.deck.card.playablecard.corner;


/**
 * Position of the card's corner (both back and front)
 */
public enum CornerPos {
    TOPLEFT(0),
    TOPRIGHT(1),
    BOTTOMLEFT(2),
    BOTTOMRIGHT(3);

    public final int val;

    CornerPos(int val) {
        this.val = val;
    }
}
