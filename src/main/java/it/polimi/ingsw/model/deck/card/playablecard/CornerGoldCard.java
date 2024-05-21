package it.polimi.ingsw.model.deck.card.playablecard;

import it.polimi.ingsw.model.deck.card.playablecard.corner.Corner;
import it.polimi.ingsw.model.deck.card.playablecard.corner.Resource;
import it.polimi.ingsw.model.player.Field;

import java.util.List;
import java.util.Map;

/**
 * Class representing all the gold cards that gives points in relation of how many corners it covered
 */

public class CornerGoldCard extends GoldCard {
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
     * @throws IllegalArgumentException if points is a negative number
     */
    public CornerGoldCard(int points,
                          int id,
                          String frontImage,
                          String backImage,
                          List<Corner> corners,
                          List<Resource> backResources,
                          Map<Resource, Integer> resourcesNeeded){
        super(points, id, frontImage, backImage, corners, backResources, resourcesNeeded);
    }

    /**
     * The method relies on two field's methods:
     *      one searches the played card on the field
     *      the other counts the surroundings card of the played card
     * The calculation is done after the card is played
     * and since to be played a card needs to cover 1-4 angles
     * it is already implied that the card only covers angles by playing it,
     * thus it is sufficient to count the neighbors,
     * instead of the covered cards.
     *
     * @param field field of the calling player
     * @return the total number of points gained
     */
    @Override
    public int getTotalPoints(Field field) {
        return field.numOfNeighbors(field.findCard(this)) * this.getPoints();
    }
}
