package module.deck.card.playablecard.corner;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Position of the card's corner (both back and front)
 */
public enum CornerPos {
    TOPLEFT,
    TOPRIGHT,
    BOTTOMLEFT,
    BOTTOMRIGHT;

    /**
     *
     * @return an ArrayList containing all the enum constants
     */
    public static List<CornerPos> getValues() {
        return new ArrayList<>(Arrays.asList(CornerPos.values()));
    }
}
