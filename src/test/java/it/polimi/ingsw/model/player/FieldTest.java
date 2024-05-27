package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.deck.card.playablecard.PlayableCard;
import it.polimi.ingsw.model.deck.card.playablecard.corner.Item;
import it.polimi.ingsw.model.deck.card.playablecard.corner.Resource;
import it.polimi.ingsw.exceptions.CoordinatesNotValidException;
import it.polimi.ingsw.utilities.CardsConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class FieldTest {
    private Field field;
    private static CardsConfig cardsConfig;

    @BeforeAll
    public static void setupCardsConfig() throws IOException {
        cardsConfig = CardsConfig.getInstance();
    }

    @BeforeEach
    public void setupField() {
        field = new Field();
    }

    @Test
    @DisplayName("Test Field initial state")
    public void testInitialState() {
        assertEquals(0, field.getCardsCount());
        assertSymbolsCount(0, 0, 0, 0, 0, 0, 0);
    }

    @Test
    @DisplayName("Test Field add, get and find card")
    public void testAddGetFindCard() {
        PlayableCard card86 = (PlayableCard) cardsConfig.getCard(86);
        Coordinates coords86 = new Coordinates(40, 40);
        PlayableCard card24 = (PlayableCard) cardsConfig.getCard(24);
        Coordinates coords24 = new Coordinates(39, 39);
        PlayableCard card32 = (PlayableCard) cardsConfig.getCard(32);
        Coordinates coords32 = new Coordinates(38, 38);
        assertThrows(CoordinatesNotValidException.class, () -> field.addCard(card86, false, coords24));
        assertDoesNotThrow(() -> field.addCard(card86, false, coords86));
        assertDoesNotThrow(() -> field.addCard(card24, true, coords24));
        assertDoesNotThrow(() -> field.addCard(card32, false, coords32));
        assertEquals(3, field.getCardsCount());
        for (int x = 0; x < field.size(); x++) {
            for (int y = 0; y < field.size(); y++) {
                if (x == 40 && y == 40) {
                    PlacedCard placedCard = new PlacedCard(card86, false, 0);
                    assertEquals(placedCard, field.getPlacedCard(coords86));
                }
                else if (x == 39 && y == 39) {
                    PlacedCard placedCard = new PlacedCard(card24, true, 1);
                    assertEquals(placedCard, field.getPlacedCard(coords24));
                }
                else if (x == 38 && y == 38) {
                    PlacedCard placedCard = new PlacedCard(card32, false, 2);
                    assertEquals(placedCard, field.getPlacedCard(coords32));
                }
                else {
                    assertNull(field.getPlacedCard(new Coordinates(x, y)));
                }
            }
        }
        assertThrows(CoordinatesNotValidException.class, () -> field.addCard(card32, false, coords86));
    }

    @Test
    @DisplayName("Test Field add gold card")
    public void testAddGoldCard() {

    }

    // controllare
    @Test
    @DisplayName("Test Field valid coordinates")
    public void testValidCoordinates() {
        List<Coordinates> coords = field.getAllValidCoords();
        assertEquals(1, coords.size());
        assertEquals(new Coordinates(40, 40), coords.getFirst());
        assertDoesNotThrow(() -> field.addCard((PlayableCard) cardsConfig.getCard(81), false, new Coordinates(40, 40)));
        assertDoesNotThrow(() -> field.addCard((PlayableCard) cardsConfig.getCard(8), false, new Coordinates(39, 39)));
        assertDoesNotThrow(() -> field.addCard((PlayableCard) cardsConfig.getCard(11), false, new Coordinates(38, 40)));
        assertDoesNotThrow(() -> field.addCard((PlayableCard) cardsConfig.getCard(26), true, new Coordinates(39, 41)));
        // assertEquals(7, field.getAllValidCoords().size());
    }

    @Test
    @DisplayName("Test Field invalid coordinates")
    public void testInvalidCoordinates() {
        List<Coordinates> coordsList = new ArrayList<>();
        coordsList.add(new Coordinates(0, 0));
        coordsList.add(new Coordinates(41, 40));
        coordsList.add(new Coordinates(81, 80));
        PlayableCard card = Mockito.mock(PlayableCard.class);
        for (Coordinates coords : coordsList) {
            assertThrows(CoordinatesNotValidException.class, () -> {
                field.addCard(card, true, coords);
            });
        }
    }

    @Test
    @DisplayName("Test Field find card")
    public void testFindCard() {
        PlayableCard card7 = (PlayableCard) cardsConfig.getCard(7);
        Coordinates coords7 = new Coordinates(41, 41);
        PlayableCard card84 = (PlayableCard) cardsConfig.getCard(84);
        Coordinates coords84 = new Coordinates(40, 40);
        assertNull(field.findCard(card7));
        assertNull(field.findCard(card84));
        assertDoesNotThrow(() -> field.addCard(card84, true, coords84));
        assertDoesNotThrow(() -> field.addCard(card7, false, coords7));
        assertEquals(coords7, field.findCard(card7));
        assertEquals(coords84, field.findCard(card84));
    }

    //todo finire
    @Test
    @DisplayName("Test Field num of neighbors")
    public void testNumOfNeighbors() {
    }

    //todo finire
    @Test
    @DisplayName("Test Field intermediate states")
    public void testIntermediateStates() {
    }

    @Test
    @DisplayName("Test diagonal pattern on a full field")
    public void testDiagonalPatternFull() {
        PlayableCard card = (PlayableCard) cardsConfig.getCard(1);
        List<Coordinates> coordsList = field.getAllValidCoords();
        while (!coordsList.isEmpty()) {
            for (Coordinates coords : coordsList) {
                assertDoesNotThrow(() -> field.addCard(card, true, coords));
            }
            coordsList = field.getAllValidCoords();
        }
        assertEquals(1067, field.numOfDiagonalPatterns(card.getColor(), true));
        assertEquals(1067, field.numOfDiagonalPatterns(card.getColor(), false));
        assertEquals(3281, field.getCardsCount());
        assertEquals(0, field.getAllValidCoords().size());
    }

    private void assertSymbolsCount(
            int animal,
            int fungi,
            int insect,
            int plant,
            int inkwell,
            int manuscript,
            int quill
    ) {
        assertEquals(animal, field.getSymbolCount(Resource.ANIMAL));
        assertEquals(fungi, field.getSymbolCount(Resource.FUNGI));
        assertEquals(insect, field.getSymbolCount(Resource.INSECT));
        assertEquals(plant, field.getSymbolCount(Resource.PLANT));
        assertEquals(inkwell, field.getSymbolCount(Item.INKWELL));
        assertEquals(manuscript, field.getSymbolCount(Item.MANUSCRIPT));
        assertEquals(quill, field.getSymbolCount(Item.QUILL));
    }
}
