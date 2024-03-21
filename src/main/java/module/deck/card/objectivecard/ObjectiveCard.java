package module.deck.card.objectivecard;

import module.deck.card.Card;

public abstract class ObjectiveCard extends Card {

    /**
     * @param points the number of points obtained playing/completing the card
     * @param frontImage path to the front image
     * @param backImage path to the back image
     * @throws NullPointerException when a path is null
     * @throws IllegalArgumentException if points is a negative number
     */
    public ObjectiveCard(int points,
                         String frontImage,
                         String backImage) throws NullPointerException, IllegalArgumentException {
        super(points, frontImage, backImage);
    }

    public abstract int getTotalPoints();
}
