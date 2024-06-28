package it.polimi.ingsw.modelView;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.deck.card.playablecard.GoldCard;
import it.polimi.ingsw.model.deck.card.playablecard.PlayableCard;

import java.io.Serializable;

/**
 * Immutable version of the {@link Board} class in the model
 */
public class BoardView implements Serializable {
    private final PlayableCard resourceTopCard;
    private final GoldCard goldTopCard;
    private final PlayableCard[] visibleCards = new PlayableCard[4];

    public BoardView(Board board) {
        for (int i = 0; i < board.getVisibleCards().length; i++) {
            this.visibleCards[i]= board.getVisibleCards()[i];
        }
        this.resourceTopCard = board.getResourceDeck().getTop();
        this.goldTopCard = board.getGoldDeck().getTop();
    }

    public PlayableCard[] getVisibleCards(){
        return this.visibleCards.clone();
    }

    public PlayableCard getResourceTopCard() {
        return resourceTopCard;
    }

    public GoldCard getGoldTopCard() {
        return goldTopCard;
    }
}
