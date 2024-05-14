package it.polimi.ingsw.modelView.cardView.ObjectiveCardView;

import it.polimi.ingsw.model.deck.card.objectivecard.DiagonalPatternObjectiveCard;
import it.polimi.ingsw.model.deck.card.playablecard.corner.Resource;

import java.io.Serializable;

public class DiagonalPatternObjectiveCardView extends ObjectiveCardView implements Serializable {
    private Resource color;
    private boolean mainDiagonal;
    public DiagonalPatternObjectiveCardView(DiagonalPatternObjectiveCard card){
        super(card);
        this.color = card.getColor();
        this.mainDiagonal = card.getMainDiagonal();
    }

    public Resource getColor() {
        return color;
    }

    public boolean isMainDiagonal() {
        return mainDiagonal;
    }
}
