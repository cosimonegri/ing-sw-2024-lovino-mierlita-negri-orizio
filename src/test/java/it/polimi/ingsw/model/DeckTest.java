package it.polimi.ingsw.model;

import it.polimi.ingsw.model.deck.Deck;
import it.polimi.ingsw.model.deck.card.Card;
import it.polimi.ingsw.model.deck.card.playablecard.PlayableCard;
import it.polimi.ingsw.utilities.CardsConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DeckTest {
    private Deck<PlayableCard> deck;

    @BeforeEach
    public void DeckSetUp() throws IOException {
        CardsConfig cardsConfig = CardsConfig.getInstance();
        deck = new Deck<>(cardsConfig.getResourceCards());
    }
    @Test
    public void DrawTest(){
        PlayableCard testCard = deck.getCards().getLast();
        assertEquals(deck.draw(), testCard);
        List<PlayableCard> emptyList = new ArrayList<>();
        Deck<PlayableCard> emptyDeck = new Deck<>(emptyList);
        assertThrows(IllegalStateException.class, emptyDeck::draw);
    }
}
