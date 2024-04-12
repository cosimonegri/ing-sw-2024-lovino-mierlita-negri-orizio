package it.polimi.ingsw.model;

import it.polimi.ingsw.model.deck.GoldDeck;
import it.polimi.ingsw.model.deck.ObjectiveDeck;
import it.polimi.ingsw.model.deck.ResourceDeck;
import it.polimi.ingsw.model.deck.StarterDeck;
import it.polimi.ingsw.model.deck.card.Card;
import it.polimi.ingsw.model.deck.card.playablecard.GoldCard;
import it.polimi.ingsw.model.deck.card.playablecard.PlayableCard;

import java.io.IOException;

public class Board {
    private final GoldDeck goldDeck;
    private final ResourceDeck resourceDeck;
    private final ObjectiveDeck objectiveDeck;
    private final StarterDeck starterDeck;

    private final PlayableCard[] visibleCards;

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

    public PlayableCard[] getVisibleCards() {
        return visibleCards.clone();
    }

    public void takeVisibleCard(Card c) {
            for (int i = 0; i < 4; i++) {
                if (c.equals(visibleCards[i])) {
                    if (c instanceof GoldCard)
                    {
                        if (!getGoldDeck().isEmpty())
                            visibleCards[i] = getGoldDeck().draw();
                          else if (!getResourceDeck().isEmpty())
                             visibleCards[i] = getResourceDeck().draw();
                    } else if (c instanceof PlayableCard)
                    {
                        if (!getResourceDeck().isEmpty())
                            visibleCards[i] = getResourceDeck().draw();
                           else if(!getGoldDeck().isEmpty())
                               visibleCards[i] = getGoldDeck().draw();
                    }
                    return;
                }
            }
        throw new IllegalStateException("Card requested was not a visible card");
    }

}