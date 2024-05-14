package it.polimi.ingsw.model.deck.card.objectivecard;

import it.polimi.ingsw.model.deck.card.playablecard.corner.Resource;
import it.polimi.ingsw.model.player.Field;
import it.polimi.ingsw.modelView.cardView.ObjectiveCardView.DiagonalPatternObjectiveCardView;
import it.polimi.ingsw.modelView.cardView.ObjectiveCardView.ObjectiveCardView;

/**
 * Class that represents the objective cards that need a diagonal pattern of cards to be completed
 */
public class DiagonalPatternObjectiveCard extends ObjectiveCard {
    /**
     * Color of the cards of the pattern
     */
    private final Resource color;
    /**
     * True if the direction of the diagonals is the one in which the longer diagonal is made of the elements
     * in the positions (0, 0), (1, 1), (2, 2) etc., false otherwise
     */
    private final boolean mainDiagonal;

    /**
     * Constructor of the class
     *
     * @param points the number of points obtained by completing the objective once
     * @param id the card identifier
     * @param frontImage path to front image
     * @param backImage path to back image
     * @param color color of the cards of the pattern
     * @param mainDiagonal true if the direction of the diagonals is the one in which the longer diagonal
     * is made of the elements in the positions (0, 0), (1, 1), (2, 2) etc., false otherwise
     * @throws IllegalArgumentException if points is a negative number
     */
    public DiagonalPatternObjectiveCard(
            int points,
            int id,
            String frontImage,
            String backImage,
            Resource color,
            boolean mainDiagonal
    ) {
        super(points, id, frontImage, backImage);
        this.color = color;
        this.mainDiagonal = mainDiagonal;
    }

    @Override
    public int getTotalPoints(Field field) {
        return field.numOfDiagonalPatterns(color, mainDiagonal) * this.getPoints();
    }

    public Resource getColor() { return this.color; }

    public boolean getMainDiagonal() { return this.mainDiagonal; }

    @Override
    public DiagonalPatternObjectiveCardView getView() {
        return new DiagonalPatternObjectiveCardView(this);
    }




}
