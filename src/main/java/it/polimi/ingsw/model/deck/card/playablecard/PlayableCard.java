package it.polimi.ingsw.model.deck.card.playablecard;

import it.polimi.ingsw.model.deck.card.Card;
import it.polimi.ingsw.model.deck.card.playablecard.corner.Corner;
import it.polimi.ingsw.model.deck.card.playablecard.corner.CornerPos;
import it.polimi.ingsw.model.deck.card.playablecard.corner.Symbol;

import java.util.Collections;
import java.util.List;

/**
 *
 * This card represent a resource, gold or starter card
 */
public class PlayableCard extends Card {

    private final List<Corner> corners;
    private final List<Symbol> backResources;

    /**
     *
     * @param points added if the card is played
     * @param id the card identifier
     * @param frontImage path to front image
     * @param backImage path to back image
     * @param corners list of 8 corners
     * @param backResources list of resources
     * @throws NullPointerException if a parameter is null
     */
    public PlayableCard(int points,
                        int id,
                        String frontImage,
                        String backImage,
                        List<Corner> corners,
                        List<Symbol> backResources) {
        // call the parent constructor
        super(points, id, frontImage, backImage);
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
        return Collections.unmodifiableList(corners);
    }

    public List<Symbol> getBackResources() {
        return Collections.unmodifiableList(backResources);
    }
    @Override
    public String toString() {
        return "PlayableCard{" +
                "corners=" + corners +
                ", backResources=" + backResources +
                '}';
    }
}