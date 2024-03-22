package it.polimi.ingsw.model.deck.card.objectivecard;

import it.polimi.ingsw.model.deck.card.Card;

public abstract class ObjectiveCard extends Card {

    /**
     * @param points the number of points obtained playing/completing the card
     * @param id the card identifier
     * @param frontImage path to the front image
     * @param backImage path to the back image
     * @throws NullPointerException when a path is null
     * @throws IllegalArgumentException if points is a negative number
     */
    public ObjectiveCard(int points,
                         int id,
                         String frontImage,
                         String backImage) {
        super(points, id, frontImage, backImage);
    }

    public abstract int getTotalPoints();
}
