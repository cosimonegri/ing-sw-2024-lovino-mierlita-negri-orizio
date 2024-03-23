package it.polimi.ingsw.model.deck.card.playablecard;

import it.polimi.ingsw.model.deck.card.playablecard.corner.Corner;
import it.polimi.ingsw.model.deck.card.playablecard.corner.Symbol;
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
     * @param frontImage path to front image
     * @param backImage path to back image
     * @param corners list of 8 corners
     * @param backResources list of resources
     * @param resourcesNeeded map of type and amount of the cost's resources
     */

    public SimpleGoldCard(int points,
                          int id,
                          String frontImage,
                          String backImage,
                          List<Corner> corners,
                          List<Symbol> backResources,
                          Map<Symbol, Integer> resourcesNeeded){
        super(points, id, frontImage, backImage, corners, backResources, resourcesNeeded);}

    /**
     * @param field field of the calling player
     * @return the bonus points of the card
     */
    @Override
    public int getTotalPoints(Field field){
        return this.getPoints();
    }
}
