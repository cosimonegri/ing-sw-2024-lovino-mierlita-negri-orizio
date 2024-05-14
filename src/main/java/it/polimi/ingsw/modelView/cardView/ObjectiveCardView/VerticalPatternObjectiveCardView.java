package it.polimi.ingsw.modelView.cardView.ObjectiveCardView;

import it.polimi.ingsw.model.deck.card.objectivecard.VerticalPatternObjectiveCard;
import it.polimi.ingsw.model.deck.card.playablecard.corner.Position;
import it.polimi.ingsw.model.deck.card.playablecard.corner.Resource;

import java.io.Serializable;

public class VerticalPatternObjectiveCardView extends ObjectiveCardView implements Serializable {
    private final Resource mainColor;
    private final Resource thirdCardColor;
    private final Position thirdCardPosition;
    public VerticalPatternObjectiveCardView(VerticalPatternObjectiveCard card){
        super(card);
        this.mainColor = card.getMainColor();
        this.thirdCardColor = card.getThirdCardColor();
        this.thirdCardPosition = card.getThirdCardPos();
    }

    public Resource getMainColor() {
        return mainColor;
    }

    public Resource getThirdCardColor() {
        return thirdCardColor;
    }

    public Position getThirdCardPosition() {
        return thirdCardPosition;
    }
}
