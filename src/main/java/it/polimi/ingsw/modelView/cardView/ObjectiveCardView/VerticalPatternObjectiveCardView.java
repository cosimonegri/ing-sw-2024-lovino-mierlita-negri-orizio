package it.polimi.ingsw.modelView.cardView.ObjectiveCardView;

import it.polimi.ingsw.model.deck.card.objectivecard.VerticalPatternObjectiveCard;
import it.polimi.ingsw.model.deck.card.playablecard.corner.Position;
import it.polimi.ingsw.model.deck.card.playablecard.corner.Resource;

public class VerticalPatternObjectiveCardView extends ObjectiveCardView {
    private Resource mainColor;
    private Resource thirdCardColor;
    private Position thirdCardPosition;
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
