package it.polimi.ingsw.model;

import it.polimi.ingsw.model.deck.Deck;
import it.polimi.ingsw.model.deck.card.objectivecard.ObjectiveCard;
import it.polimi.ingsw.model.deck.card.playablecard.GoldCard;
import it.polimi.ingsw.model.deck.card.playablecard.PlayableCard;
import it.polimi.ingsw.modelView.BoardView;
import it.polimi.ingsw.utilities.CardsConfig;

import java.io.IOException;

/**
 * Board class contains the four different type of decks and the visible cards that players can take from the terrain
 */
public class Board {
    private final Deck<GoldCard> goldDeck;
    private final Deck<PlayableCard> resourceDeck;
    private final Deck<ObjectiveCard> objectiveDeck;
    private final Deck<PlayableCard> starterDeck;
    private final PlayableCard[] visibleCards;

    /**
     * constructor of the class
     */
    public Board() throws IOException {
        CardsConfig cardsConfig = CardsConfig.getInstance();
        goldDeck = new Deck<>(cardsConfig.getGoldCards());
        resourceDeck = new Deck<>(cardsConfig.getResourceCards());
        objectiveDeck = new Deck<>(cardsConfig.getObjectiveCards());
        starterDeck = new Deck<>(cardsConfig.getStarterCards());

        visibleCards = new PlayableCard[4];
        visibleCards[0] =  goldDeck.draw();
        visibleCards[1] =  goldDeck.draw();
        visibleCards[2] =  resourceDeck.draw();
        visibleCards[3] =  resourceDeck.draw();
    }

    public Deck<GoldCard> getGoldDeck() {
        return goldDeck;
    }

    public Deck<PlayableCard> getResourceDeck() {
        return resourceDeck;
    }

    public Deck<ObjectiveCard> getObjectiveDeck() {
        return objectiveDeck;
    }

    public Deck<PlayableCard> getStarterDeck() {
        return starterDeck;
    }

    /**
     * @return a clone of the array containing the (maximum 4) visible cards
     */
    public PlayableCard[] getVisibleCards() {
        return visibleCards.clone();
    }

    /**
     * the method removes a card from visibleCards and tries to replace it with a new card
     * @param card is the card the player wants to draw from visibleCards
     */
    public void replaceVisibleCard(PlayableCard card) {
        for (int i = 0; i < this.visibleCards.length; i++) {
            // skip to the next index if the card doesn't match woth the given one
            if (!this.visibleCards[i].equals(card)) {
                continue;
            }
            // just remove the card if both decks are empty
            if (this.resourceDeck.isEmpty() && this.goldDeck.isEmpty()) {
                visibleCards[i] = null;
            }
            // replace the card with an available card when one deck is empty
            else if (this.resourceDeck.isEmpty()) {
                visibleCards[i] = this.goldDeck.draw();
            }
            else if (this.goldDeck.isEmpty()) {
                visibleCards[i] = this.resourceDeck.draw();
            }
            // replace the card with a card of the same type when no decks are empty
            else if (card instanceof GoldCard) {
                visibleCards[i] = this.goldDeck.draw();
            }
            else {
                visibleCards[i] = this.resourceDeck.draw();
            }
        }
    }

    public BoardView getView(Board board){
        return new BoardView(board);
    }

}