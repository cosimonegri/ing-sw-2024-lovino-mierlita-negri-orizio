package it.polimi.ingsw.model;

import it.polimi.ingsw.model.deck.Deck;
import it.polimi.ingsw.model.deck.card.objectivecard.ObjectiveCard;
import it.polimi.ingsw.model.deck.card.playablecard.GoldCard;
import it.polimi.ingsw.model.deck.card.playablecard.PlayableCard;
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

    //TODO remove card when both decks are empty
    /**
     * the method removes a card from visibleCards and tries to replace it with a new card
     * @param card is the card the player wants to draw from visibleCards
     */
    public void takeVisibleCard(PlayableCard card) {
        //iterates on the array of visible cards
        for (int i = 0; i < 4; i++) {
            if (card.equals(visibleCards[i])) {
                if (card instanceof GoldCard) {
                //treating the case when the replaced card's deck type is empty for both possible instances of a visible card
                    if (!(getGoldDeck().isEmpty()))
                        visibleCards[i] = getGoldDeck().draw();
                    else if (!getResourceDeck().isEmpty())
                        visibleCards[i] = getResourceDeck().draw();
                } else {
                    if ((!getResourceDeck().isEmpty()))
                        visibleCards[i] = getResourceDeck().draw();
                    else if (!getGoldDeck().isEmpty())
                        visibleCards[i] = getGoldDeck().draw();
                }
                //if both decks are empty, it does nothing
                return;
            }
        }
        //if card c is not a goldCard or a resourceDeck
        throw new IllegalStateException("Card requested was not a visible card");
    }

}