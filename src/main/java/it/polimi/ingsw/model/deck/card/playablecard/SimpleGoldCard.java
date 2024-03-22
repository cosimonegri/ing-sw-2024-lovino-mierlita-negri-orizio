package it.polimi.ingsw.model.deck.card.playablecard;

import it.polimi.ingsw.model.deck.card.playablecard.corner.Corner;
import it.polimi.ingsw.model.deck.card.playablecard.corner.Resource;
import it.polimi.ingsw.model.player.Field;
import java.util.List;
import java.util.Map;

/**
 * Class to represent all simple gold card
 * @author Alexandru Cezar Mierlita
 */

public class SimpleGoldCard extends GoldCard {

    public SimpleGoldCard(int points,
                          String frontImage,
                          String backImage,
                          List<Corner> corners,
                          List<Resource> backResources,
                          Map<Resource, Integer> resourcesNeeded){super(points, frontImage, backImage, corners, backResources, resourcesNeeded);}

    /**
     * @param field field of the calling player
     * @return the bonus points of the card
     */
    @Override
    public int getTotalPoints(Field field){
        return this.getPoints();
    }
}
