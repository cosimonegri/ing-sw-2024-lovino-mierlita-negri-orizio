package it.polimi.ingsw.model.deck.card.playablecard;

import it.polimi.ingsw.model.player.Field;
import it.polimi.ingsw.model.deck.card.playablecard.corner.Corner;
import it.polimi.ingsw.model.deck.card.playablecard.corner.Resource;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GoldCard abstract class to represent all the common attributes and methods of all gold cards
 * @author Alexandru Cezar Mierlita
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
     * @param frontImage path to front image
     * @param backImage path to back image
     * @param corners list of 8 corners
     * @param backResources list of resources
     * @param resourcesNeeded map of type and amount of the cost's resources
     * @throws NullPointerException if one or more parameters are null
     * @throws IllegalArgumentException if value of points is negative
     */

    public GoldCard(int points,
                    String frontImage,
                    String backImage,
                    List<Corner> corners,
                    List<Resource> backResources,
                    Map<Resource, Integer> resourcesNeeded){
        super(points, frontImage, backImage, corners, backResources);
        if(frontImage == null ||
            backImage == null ||
            corners == null ||
            resourcesNeeded == null) throw new NullPointerException("Attributes cannot be null");
        if(points < 0 ) throw new IllegalArgumentException("Value of points cannot be negative");
        this.resourcesNeeded = new HashMap<>(resourcesNeeded);
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
