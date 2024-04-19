package it.polimi.ingsw.model;

import it.polimi.ingsw.model.deck.GoldDeck;
import it.polimi.ingsw.model.deck.ObjectiveDeck;
import it.polimi.ingsw.model.deck.ResourceDeck;
import it.polimi.ingsw.model.deck.StarterDeck;
import it.polimi.ingsw.model.deck.card.Card;
import it.polimi.ingsw.model.deck.card.playablecard.GoldCard;
import it.polimi.ingsw.model.deck.card.playablecard.PlayableCard;

import java.io.IOException;

/**
 * Board class contains the four different type of decks and the visible cards that players can take from the terrain
 */
public class Board {
    private final GoldDeck goldDeck;
    private final ResourceDeck resourceDeck;
    private final ObjectiveDeck objectiveDeck;
    private final StarterDeck starterDeck;

    private final PlayableCard[] visibleCards;

    /**
     * constructor of the class
     */
    public Board() throws IOException {
        visibleCards = new PlayableCard[4];
        goldDeck = new GoldDeck();
        resourceDeck = new ResourceDeck();
        objectiveDeck = new ObjectiveDeck();
        starterDeck = new StarterDeck();

        visibleCards[0] =  goldDeck.draw();
        visibleCards[1] =  goldDeck.draw();
        visibleCards[2] =  resourceDeck.draw();
        visibleCards[3] =  resourceDeck.draw();
    }

    public GoldDeck getGoldDeck() {
        return goldDeck;
    }

    public ResourceDeck getResourceDeck() {
        return resourceDeck;
    }

    public ObjectiveDeck getObjectiveDeck() {
        return objectiveDeck;
    }

    public StarterDeck getStarterDeck() {
        return starterDeck;
    }

    /**
     * @return a clone of the array containing the (maximum 4) visible cards
     */
    public PlayableCard[] getVisibleCards() {
        return visibleCards.clone();
    }

    /**
     * the method removes c from visibleCards and tries to replace it with a new card
     * @param c is the card the player wants to draw from visibleCards
     */
    public void takeVisibleCard(Card c) {
        //iterates on the array of visible cards
            for (int i = 0; i < 4; i++) {
                if (c.equals(visibleCards[i])) {
                    if (c instanceof GoldCard)
                    //treating the case when the replaced card's deck type is empty for both possible instances of a visible card
                    {
                        if (!(getGoldDeck().isDeckEmpty()))
                            visibleCards[i] = getGoldDeck().draw();
                          else if (!getResourceDeck().isDeckEmpty())
                             visibleCards[i] = getResourceDeck().draw();
                    } else if (c instanceof PlayableCard)
                    {
                        if ((!getResourceDeck().isDeckEmpty()))
                            visibleCards[i] = getResourceDeck().draw();
                           else if(!getGoldDeck().isDeckEmpty())
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