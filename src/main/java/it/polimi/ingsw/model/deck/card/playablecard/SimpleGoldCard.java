package it.polimi.ingsw.model.deck.card.playablecard;

import it.polimi.ingsw.model.deck.card.playablecard.corner.Corner;
import it.polimi.ingsw.model.deck.card.playablecard.corner.Resource;
import it.polimi.ingsw.model.player.Field;
import java.util.List;
import java.util.Map;

/**
 * Class to represent all simple gold card
 */

public class SimpleGoldCard extends GoldCard {

    /**
     * Constructor of the class
     *
     * @param points added if the card is played
     * @param id identifier number of the card
     * @param corners list of 8 corners
     * @param backResources list of resources
     * @param resourcesNeeded map of type and amount of the cost's resources
     * @throws IllegalArgumentException if points is a negative number
     */

    public SimpleGoldCard(int points,
                          int id,
                          List<Corner> corners,
                          List<Resource> backResources,
                          Map<Resource, Integer> resourcesNeeded
    ) {
        super(points, id, corners, backResources, resourcesNeeded);
    }

    /**
     * @param field field of the calling player, needed for the override
     * @return the bonus points of the card
     */
    @Override
    public int getTotalPoints(Field field){
        return this.getPoints();
    }
}
