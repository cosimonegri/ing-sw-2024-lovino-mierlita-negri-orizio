package it.polimi.ingsw.modelView;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.modelView.cardView.PlacedCardView;
import it.polimi.ingsw.modelView.cardView.PlayableCardView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BoardView implements Serializable {
    private final PlayableCardView resourceTopCard;
    private final PlayableCardView goldTopCard;
    private final PlayableCardView[] visibleCards = new PlayableCardView[4];

    public BoardView(Board board){
        for(int i = 0; i < board.getVisibleCards().length; i++)
            this.visibleCards[i]= board.getVisibleCards()[i].getView();
        this.resourceTopCard = board.getResourceDeck().getCards().getFirst().getView();
        this.goldTopCard = board.getGoldDeck().getCards().getFirst().getView();
    }

    public PlayableCardView[] getVisibleCards(){
        return this.visibleCards.clone();
    }

    public PlayableCardView getResourceTopCard() {
        return resourceTopCard;
    }

    public PlayableCardView getGoldTopCard() {
        return goldTopCard;
    }
}
