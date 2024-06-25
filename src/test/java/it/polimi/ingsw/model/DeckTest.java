package it.polimi.ingsw.model;

import it.polimi.ingsw.model.deck.Deck;
import it.polimi.ingsw.model.deck.card.playablecard.PlayableCard;
import it.polimi.ingsw.utilities.CardsConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class DeckTest {
    private Deck<PlayableCard> deck;
    private static CardsConfig cardsConfig;

    @BeforeAll
    public static void setupCardsConfig() throws IOException {
        cardsConfig = CardsConfig.getInstance();
    }

    @BeforeEach
    public void setupDeck() {
        deck = new Deck<>(cardsConfig.getResourceCards().subList(0, 2));
    }

    @Test
    @DisplayName("Test Deck draw, getTop and isEmpty")
    public void deckTest() {
        assertFalse(deck.isEmpty());
        PlayableCard card1 = deck.getTop();
        assertEquals(card1, deck.draw());

        assertFalse(deck.isEmpty());
        PlayableCard card2 = deck.getTop();
        assertEquals(card2, deck.draw());

        assertTrue(deck.isEmpty());
        assertNull(deck.getTop());
        assertThrows(NoSuchElementException.class, () -> deck.draw());
        assertTrue(deck.isEmpty());
    }
}
