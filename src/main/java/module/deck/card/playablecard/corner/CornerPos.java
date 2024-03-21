package module.deck.card.playablecard.corner;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
