package it.polimi.ingsw.model.deck.card;

import it.polimi.ingsw.model.deck.card.playablecard.PlayableCard;
import it.polimi.ingsw.model.deck.card.playablecard.corner.Corner;
import it.polimi.ingsw.model.deck.card.playablecard.corner.CornerType;
import it.polimi.ingsw.model.deck.card.playablecard.corner.Position;
import it.polimi.ingsw.model.deck.card.playablecard.corner.Resource;
import it.polimi.ingsw.utilities.CardsConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class PlayableCardTest {
    private final Random rand = new Random();
    private static CardsConfig cardsConfig;

    @BeforeAll
    public static void setup() throws IOException {
        cardsConfig = CardsConfig.getInstance();
    }

    /**
     *  Checks that the constructor doesn't accept a negative
     *  value for the points field
      */
    @Test
    public void CardConstructorTest() {
        assertThrows(IllegalArgumentException.class,
                () -> new PlayableCard(-1, 1,
                                        null, null,
                                        null,null));
    }

    /**
     * Checks that the equals method confronts
     * only the card id
     */
    @Test
    public void equalsTest() {
        int max = 15;
        int id = rand.nextInt(max);
        PlayableCard c1 = new PlayableCard(rand.nextInt(max), id,
                                            null,null,
                                            null, null),
                     c2 = new PlayableCard(rand.nextInt(max), id,
                                            null, null,
                                            null, null),
                     c3 = new PlayableCard(rand.nextInt(max), id + 1,
                                            null, null,
                                            null, null);
        assertEquals(c1, c2);
        assertNotEquals(c1, c3);
        assertNotEquals(c2, c3);
    }

    /**
     * Checks that the method return the correct corner
     */
    @Test
    public void getCornerTest() {
        List<Corner> corners = new ArrayList<>();
        Corner corner;

        List<CornerType> cpList = List.of(CornerType.values());
        CornerType cp;

        List<Resource> rList = List.of(Resource.values());
        Resource r;

        for (int i = 0; i < 8; i++) {
            cp = cpList.get(rand.nextInt(cpList.size()));
            r = rList.get(rand.nextInt(rList.size()));

            corner = new Corner(cp, r);
            corners.add(corner);
        }

        PlayableCard pc = new PlayableCard(1, 1,
                null, null,
                corners, null);

        for (int i = 0; i < 8; i++) {
            assertSame(pc.getCorner(Position.values()[(i < 4) ? i : i - 4], i >= 4),
                       corners.get(i));
        }
    }

    @Test
    public void getColorTest() {
        for (PlayableCard card : cardsConfig.getStarterCards()) {
            assertNull(card.getColor());
        }
        List<PlayableCard> cards = new ArrayList<>();
        cards.addAll(cardsConfig.getResourceCards());
        cards.addAll(cardsConfig.getGoldCards());
        for (PlayableCard card : cards) {
            int cardType = ((card.getId() - 1) / 10) % 4;
            if (cardType == 0) {
                assertEquals(Resource.FUNGI, card.getColor());
            } else if (cardType == 1) {
                assertEquals(Resource.PLANT, card.getColor());
            } else if (cardType == 2) {
                assertEquals(Resource.ANIMAL, card.getColor());
            } else if (cardType == 3) {
                assertEquals(Resource.INSECT, card.getColor());
            }
        }
    }

    @Test
    public void isStarterTest() {
        for (PlayableCard card : cardsConfig.getStarterCards()) {
            assertTrue(card.isStarter());
        }
        for (PlayableCard card : cardsConfig.getResourceCards()) {
            assertFalse(card.isStarter());
        }
        for (PlayableCard card : cardsConfig.getGoldCards()) {
            assertFalse(card.isStarter());
        }
    }
}
