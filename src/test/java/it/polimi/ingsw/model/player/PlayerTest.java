package it.polimi.ingsw.model.player;

import it.polimi.ingsw.exceptions.CardNotInHandException;
import it.polimi.ingsw.model.deck.card.playablecard.PlayableCard;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.NoSuchElementException;
import java.util.Random;

public class PlayerTest {
    private Player p;

    /**
     * Before every test create a new player
     */
    @BeforeEach
    public void playerSetUp() {
        p = new Player("dolby");
    }

    /**
     * - the score value is 0 after the class creation.
     * - the value is added correctly.
     * - the exception is thrown when the parameter is negative.
     */
    @Test
    public void increaseScoreTest() {
        assertEquals(0, p.getScore());
        Random r = new Random();
        int tmp, exp;
        int max = 10;
        for (int i = 0; i < max; i++) {
            tmp = r.nextInt(max);
            exp = tmp + p.getScore();
            p.increaseScore(tmp); // 0 < tmp < 9
            assertEquals(exp, p.getScore());
        }
        for (int i = 0; i < max; i++) {
            int t = -1 - r.nextInt(max); // -11 < t < -1
            assertThrows(IllegalArgumentException.class, () -> p.increaseScore(t));
        }
    }

    /**
     * - the method adds correctly the card to the list
     * - if the hand is already full throws an exception
     */
    @Test
    public void addToHandTest() {
        for (int i = 0; i < 3; i++) {
            PlayableCard card = Mockito.mock(PlayableCard.class);
            assertFalse(p.getHand().contains(card));
            p.addToHand(card);
            assertTrue(p.getHand().contains(card));
        }
        assertThrows(IllegalStateException.class, () -> p.addToHand(Mockito.mock(PlayableCard.class)));
    }

    /**
     * - a card is removed correctly if present
     * - an exception is thrown if the card is not found
     */
    @Test
    public void removeFromHandTest() {
        for (int i = 0; i < 3; i++) {
            PlayableCard card = Mockito.mock(PlayableCard.class);
            assertFalse(p.getHand().contains(card));
            p.addToHand(card);
            assertTrue(p.getHand().contains(card));
            assertDoesNotThrow(() -> p.removeFromHand(card));
            assertFalse(p.getHand().contains(card));
        }
        assertThrows(CardNotInHandException.class, () -> p.removeFromHand(Mockito.mock(PlayableCard.class)));
    }
}
