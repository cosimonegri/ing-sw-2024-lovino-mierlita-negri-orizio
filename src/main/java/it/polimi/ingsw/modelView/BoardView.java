package it.polimi.ingsw.modelView;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.deck.card.playablecard.PlayableCard;

import java.io.Serializable;

public class BoardView implements Serializable {
    private final PlayableCard resourceTopCard;
    private final PlayableCard goldTopCard;
    private final PlayableCard[] visibleCards = new PlayableCard[4];

    public BoardView(Board board) {
        for (int i = 0; i < board.getVisibleCards().length; i++) {
            this.visibleCards[i]= board.getVisibleCards()[i];
        }
        this.resourceTopCard = !board.getResourceDeck().isEmpty() ? board.getResourceDeck().getCards().getLast() : null;
        this.goldTopCard = !board.getGoldDeck().isEmpty() ? board.getGoldDeck().getCards().getLast() : null;
    }

    public PlayableCard[] getVisibleCards(){
        return this.visibleCards.clone();
    }

    public PlayableCard getResourceTopCard() {
        return resourceTopCard;
    }

    public PlayableCard getGoldTopCard() {
        return goldTopCard;
    }
}
