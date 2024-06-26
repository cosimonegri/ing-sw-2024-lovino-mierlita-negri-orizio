package it.polimi.ingsw.model.deck.card.objectivecard;

import it.polimi.ingsw.model.deck.card.playablecard.corner.Position;
import it.polimi.ingsw.model.deck.card.playablecard.corner.Resource;
import it.polimi.ingsw.model.player.Field;

/**
 * Class that represents the objective cards that need a J-shaped pattern of cards to be completed
 */
public class VerticalPatternObjectiveCard extends ObjectiveCard {
    /**
     * Color of the 2 vertical cards
     */
    private final Resource mainColor;
    /**
     * Color of the third card
     */
    private final Resource thirdCardColor;
    /**
     * The position of the third card of the pattern, relatively to the other 2 cards
     */
    private final Position thirdCardPos;


    /**
     * Constructor of the class
     *
     * @param points the number of points obtained by completing the objective once
     * @param id the card identifier
     * @param mainColor color of the 2 vertical cards
     * @param thirdCardColor color of the third card
     * @param thirdCardPos position of the third card of the pattern, relatively to the other 2 cards
     * @throws IllegalArgumentException if points is a negative number
     */
    public VerticalPatternObjectiveCard(
            int points,
            int id,
            Resource mainColor,
            Resource thirdCardColor,
            Position thirdCardPos
    ) {
        super(points, id);
        this.mainColor = mainColor;
        this.thirdCardColor = thirdCardColor;
        this.thirdCardPos = thirdCardPos;
    }

    @Override
    public int getTotalPoints(Field field) {
        return field.numOfVerticalPatterns(mainColor, thirdCardColor, thirdCardPos) * this.getPoints();
    }

    public Resource getMainColor() { return this.mainColor; }

    public Resource getThirdCardColor() { return this.thirdCardColor; }

    public Position getThirdCardPos() { return this.thirdCardPos; }
}
