package it.polimi.ingsw.model.deck.card.objectivecard;

import it.polimi.ingsw.model.deck.card.Card;
import it.polimi.ingsw.model.player.Field;

public abstract class ObjectiveCard extends Card {

    /**
     * Constructor of the class
     *
     * @param points the number of points obtained by completing the objective once
     * @param id the card identifier
     * @param frontImage path to the front image
     * @param backImage path to the back image
     * @throws IllegalArgumentException if points is a negative number
     */
    public ObjectiveCard(int points, int id, String frontImage, String backImage) {
        super(points, id, frontImage, backImage);
    }

    /**
     * @param field the field to be analyzed
     * @return the points of the card multiplied by the times the objective has been completed
     */
    public abstract int getTotalPoints(Field field);
}
