package it.polimi.ingsw.model.deck.card.playablecard;

import it.polimi.ingsw.model.deck.card.playablecard.corner.Corner;
import it.polimi.ingsw.model.deck.card.playablecard.corner.Symbol;
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
    private final Symbol item;

    /**
     * @param points added if the card is played
     * @param id identifier number of the card
     * @param frontImage path to front image
     * @param backImage path to back image
     * @param corners list of 8 corners
     * @param backResources list of resources
     * @param resourcesNeeded map of type and amount of the cost's resources
     * @throws NullPointerException if the item is null
     */
    public ItemGoldCard(int points,
                        int id,
                        String frontImage,
                        String backImage,
                        List<Corner> corners,
                        List<Symbol> backResources,
                        Map<Symbol, Integer> resourcesNeeded,
                        Symbol item){super(points, id, frontImage, backImage, corners, backResources, resourcesNeeded);
        if(item == null) throw new NullPointerException("Item cannot be null");
        this.item = item;
    }

    public Symbol getItem(){
        return this.item;
    }

    /**
     * @param field field of the calling player
     * @return the total points gained by playing the card
     */
    @Override
    public int getTotalPoints(Field field){
        return field.getSymbolCount(this.item);
    }
}
