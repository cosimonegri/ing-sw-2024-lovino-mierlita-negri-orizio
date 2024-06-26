package it.polimi.ingsw.model.deck.card.objectivecard;

import it.polimi.ingsw.model.deck.card.Card;
import it.polimi.ingsw.model.player.Field;

public abstract class ObjectiveCard extends Card {

    /**
     * Constructor of the class
     *
     * @param points the number of points obtained by completing the objective once
     * @param id the card identifier
     * @throws IllegalArgumentException if points is a negative number
     */
    public ObjectiveCard(int points, int id) {
        super(points, id);
    }

    /**
     * @param field the field to be analyzed
     * @return the points of the card multiplied by the times the objective has been completed
     */
    public abstract int getTotalPoints(Field field);
}
