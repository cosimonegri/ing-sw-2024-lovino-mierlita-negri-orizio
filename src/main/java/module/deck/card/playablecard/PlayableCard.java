package module.deck.card.playablecard;

import module.deck.card.Card;
import module.deck.card.playablecard.corner.Corner;
import module.deck.card.playablecard.corner.CornerPos;
import module.deck.card.playablecard.corner.Resource;
import module.player.Player;

import java.util.List;

/**
 * @author Orizio Davide
 *
 * This card represent a resource, gold or starter card
 */
public class PlayableCard extends Card {
    private final List<Corner> corners;
    private final List<Resource> backResources;

    /**
     *
     * @param points added if the card is played
     * @param frontImage path to front image
     * @param backImage path to back image
     * @param corners list of 8 corners
     * @param backResources list of resources
     * @throws NullPointerException if a parameter is null
     */
    public PlayableCard(int points,
                        String frontImage,
                        String backImage,
                        List<Corner> corners,
                        List<Resource> backResources) throws NullPointerException, IllegalArgumentException {
        // call the parent constructor
        super(points, frontImage, backImage);
        if (corners == null) throw new NullPointerException("List of corners cannot be null");
        this.corners = corners;
        if (backResources == null) throw new NullPointerException("List of back resources cannot be null");
        this.backResources = backResources;
    }

    /**
     *
     * @param cornerPos position of the corner
     * @param flipped tells if the card is on the front or back
     * @return the requested corner
     */
    public Corner getCorner(CornerPos cornerPos, boolean flipped) {
        if (cornerPos == null) throw new NullPointerException("CornerPos cannot be null");
        // Find the corner position
        for (CornerPos cp: CornerPos.values()) {
            if (cp.val == cornerPos.val) {
                // Find the corner on the back or front
                if (flipped)
                    return this.corners.get(cp.val + CornerPos.values().length); // back
                else
                    return this.corners.get(cp.val);    // front
            }
        }
        throw new IllegalStateException("The requested corner was not found");
    }

    public List<Corner> getCorners() {
        return this.corners;
    }

    public List<Resource> getBackResources() {
        return this.backResources;
    }
}
