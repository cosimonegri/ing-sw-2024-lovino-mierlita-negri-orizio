package it.polimi.ingsw.model.deck.card.playablecard;

import it.polimi.ingsw.model.deck.card.playablecard.corner.Resource;
import it.polimi.ingsw.model.player.Field;
import it.polimi.ingsw.model.deck.card.playablecard.corner.Corner;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GoldCard abstract class to represent all the common attributes and methods of all gold cards
 */
public abstract class GoldCard extends PlayableCard{
    /**
     * attribute defining types and amounts of the card's resource cost
     */
    private final Map<Resource, Integer> resourcesNeeded;

    /**
     * Constructor of the class
     *
     * @param points added if the card is played
     * @param id identifier number of the card
     * @param corners list of 8 corners
     * @param backResources list of resources
     * @param resourcesNeeded map of type and amount of the cost's resources
     * @throws IllegalArgumentException if value of points is negative
     */

    public GoldCard(int points, int id, List<Corner> corners, List<Resource> backResources, Map<Resource, Integer> resourcesNeeded) {
        super(points, id, corners, backResources);
        this.resourcesNeeded = new HashMap<>(resourcesNeeded);  // TODO: check resourcesNeeded
    }

    /**
     * @param field field of the calling player
     * @return the possibility to satisfy the card's cost
     */
    public boolean hasResourcesNeeded(Field field){
        for(Resource key : this.resourcesNeeded.keySet()) {
            if(field.getSymbolCount(key) < this.resourcesNeeded.get(key)) return false;
        }
        return true;
    }

    public Map<Resource, Integer> getResourcesNeeded() {
        return Collections.unmodifiableMap(this.resourcesNeeded);
    }

    /**
     * abstract method which calculates all the points gained by playing the card
     * @param field field of the calling player
     */
    public abstract int getTotalPoints(Field field);
}
