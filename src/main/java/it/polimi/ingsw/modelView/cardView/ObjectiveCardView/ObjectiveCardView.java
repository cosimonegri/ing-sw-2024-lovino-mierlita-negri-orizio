package it.polimi.ingsw.modelView.cardView.ObjectiveCardView;

import it.polimi.ingsw.model.deck.card.objectivecard.ObjectiveCard;
import it.polimi.ingsw.modelView.cardView.CardView;

public class ObjectiveCardView extends CardView {
    private int id;
    public ObjectiveCardView(ObjectiveCard card){
        this.id = card.getId();
    }
    public int getId() {
        return this.id;
    }

}
