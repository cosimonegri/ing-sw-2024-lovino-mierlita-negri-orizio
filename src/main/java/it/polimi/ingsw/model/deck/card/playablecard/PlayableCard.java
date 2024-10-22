package it.polimi.ingsw.model.deck.card.playablecard;

import it.polimi.ingsw.model.deck.card.Card;
import it.polimi.ingsw.model.deck.card.playablecard.corner.Corner;
import it.polimi.ingsw.model.deck.card.playablecard.corner.CornerType;
import it.polimi.ingsw.model.deck.card.playablecard.corner.Position;
import it.polimi.ingsw.model.deck.card.playablecard.corner.Resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
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
     * @param corners list of 8 corners
     * @param backResources list of resources
     */
    public PlayableCard(int points, int id, List<Corner> corners, List<Resource> backResources) {
        // call the parent constructor
        super(points, id);
        this.corners = (corners != null) ? new ArrayList<>(corners) : new ArrayList<>();
        this.backResources = (backResources != null) ? new ArrayList<>(backResources) : new ArrayList<>();
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
     * @return an unmodifiable list of resources
     * containing the resources on the back of the card.
     */
    public List<Resource> getBackResources() {
        return Collections.unmodifiableList(backResources);
    }

    /**
     * @return true if the playable card is a starter card
     */
    public boolean isStarter() {
        if (this.backResources.size() > 1) {
            return true;
        }
        // iterate over the back corners
        for (int i = Position.values().length; i < this.corners.size(); i++) {
            Corner corner = this.corners.get(i);
            if (corner.type() != CornerType.VISIBLE || corner.symbol() != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return the color (main resource) of the card if it is not a starter card, otherwise return null
     */
    public Resource getColor() {
        return isStarter() ? null : this.backResources.getFirst();
    }
}
