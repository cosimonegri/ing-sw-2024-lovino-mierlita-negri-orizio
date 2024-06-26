package it.polimi.ingsw.model.deck.card.playablecard;

import it.polimi.ingsw.model.deck.card.playablecard.corner.Corner;
import it.polimi.ingsw.model.deck.card.playablecard.corner.Item;
import it.polimi.ingsw.model.deck.card.playablecard.corner.Resource;
import it.polimi.ingsw.model.player.Field;
import java.util.List;
import java.util.Map;

/**
 * Class to represent all item based gold cards
 */

public class ItemGoldCard extends GoldCard{

    /**
     * Attribute representing the item needed for the score gain
     */
    private final Item item;

    /**
     * @param points added if the card is played
     * @param id identifier number of the card
     * @param corners list of 8 corners
     * @param backResources list of resources
     * @param resourcesNeeded map of type and amount of the cost's resources
     * @throws IllegalArgumentException if points is a negative number
     */
    public ItemGoldCard(int points,
                        int id,
                        List<Corner> corners,
                        List<Resource> backResources,
                        Map<Resource, Integer> resourcesNeeded,
                        Item item
    ) {
        super(points, id, corners, backResources, resourcesNeeded);
        this.item = item;
    }

    /**
     *
     * @return the item associated with the gold card.
     */
    public Item getItem(){
        return this.item;
    }

    /**
     * @param field field of the calling player
     * @return the total points gained by playing the card
     */
    @Override
    public int getTotalPoints(Field field){
        return field.getSymbolCount(this.item) * this.getPoints();
    }
}
