package it.polimi.ingsw.model.deck.card.playablecard;

import it.polimi.ingsw.model.deck.card.Card;
import it.polimi.ingsw.model.deck.card.playablecard.corner.Corner;
import it.polimi.ingsw.model.deck.card.playablecard.corner.Position;
import it.polimi.ingsw.model.deck.card.playablecard.corner.Resource;
import java.util.Collections;
import java.util.List;

/**
 *
 * This card represent a resource, gold or starter card
 */
public class PlayableCard extends Card {
    /**
     * List that contains the 4 front corners followed by the 4 back corners.
     * For each side, the corners follow the order in {@link Position}.
     */
    private final List<Corner> corners;
    /**
     * List of resources given by the card if placed on the back.
     */
    private final List<Resource> backResources;

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
                        List<Resource> backResources) {
        // call the parent constructor
        super(points, id, frontImage, backImage);
        this.corners = corners;
        this.backResources = backResources;
    }

    /**
     *
     * @param cornerPos position of the corner
     * @param flipped tells if the card is on the front or back
     * @return the requested corner
     */
    public Corner getCorner(Position cornerPos, boolean flipped) {
        if (cornerPos == null) return null;
        // Find the corner on the back or front
        return this.corners.get(cornerPos.val() + (flipped ? Position.values().length : 0));
    }

    /**
     *
     * @return the card's list of corners.
     */
    public List<Corner> getCorners() {
        return Collections.unmodifiableList(corners);
    }

    /**
     *
     * @return an unmodifiable list of resources
     * containing the resources on the back of the card.
     */
    public List<Resource> getBackResources() {
        return Collections.unmodifiableList(backResources);
    }
}
