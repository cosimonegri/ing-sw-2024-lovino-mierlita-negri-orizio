package it.polimi.ingsw.model.player;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.exceptions.CoordinatesNotValidException;
import it.polimi.ingsw.exceptions.NotEnoughResourcesException;
import it.polimi.ingsw.model.deck.card.playablecard.GoldCard;
import it.polimi.ingsw.model.deck.card.playablecard.PlayableCard;
import it.polimi.ingsw.model.deck.card.playablecard.corner.Item;
import it.polimi.ingsw.model.deck.card.playablecard.corner.Position;
import it.polimi.ingsw.model.deck.card.playablecard.corner.Resource;
import it.polimi.ingsw.utilities.CardsConfig;
import it.polimi.ingsw.utilities.Move;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


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
    @DisplayName("Test Field valid coordinates")
    public void testValidCoordinates() {
        List<Coordinates> coords = field.getAllValidCoords();
        assertEquals(1, coords.size());
        assertEquals(new Coordinates(40, 40), coords.getFirst());
        assertDoesNotThrow(() -> field.addCard((PlayableCard) cardsConfig.getCard(81), false, new Coordinates(40, 40)));
        assertDoesNotThrow(() -> field.addCard((PlayableCard) cardsConfig.getCard(8), false, new Coordinates(39, 39)));
        assertDoesNotThrow(() -> field.addCard((PlayableCard) cardsConfig.getCard(11), false, new Coordinates(38, 40)));
        assertDoesNotThrow(() -> field.addCard((PlayableCard) cardsConfig.getCard(26), true, new Coordinates(39, 41)));
        assertEquals(7, field.getAllValidCoords().size());
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
            assertThrows(CoordinatesNotValidException.class, () -> field.addCard(card, true, coords));
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

    @Test
    @DisplayName("Test vertical pattern on a specific instance of field")
    public void testVerticalPattern() throws IOException, CoordinatesNotValidException, NotEnoughResourcesException {
       addMovesToField("src/test/resources/VerticalPatternTestMoves.json", field);

       assertEquals(field.numOfVerticalPatterns(Resource.ANIMAL,Resource.FUNGI, Position.TOPRIGHT),20);
       assertEquals(field.numOfVerticalPatterns(Resource.FUNGI,Resource.PLANT, Position.BOTTOMRIGHT),20);
       assertEquals(field.numOfVerticalPatterns(Resource.PLANT,Resource.INSECT, Position.BOTTOMLEFT),20);
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

    @Test
    @DisplayName("Test of a complex instance of field in an advanced state ot the game")
    public void randomFieldTest() throws IOException, ParseException, CoordinatesNotValidException, NotEnoughResourcesException {
        addMovesToField("src/test/resources/FieldTest.json", this.field);
        //checking the total symbolCount in this instance of field
        assertSymbolsCount(6, 6, 4, 4, 0, 0, 1);

        //checking diagonalPatterns of different resources
        assertEquals(field.numOfDiagonalPatterns(Resource.INSECT, false), 1);
        assertEquals(field.numOfDiagonalPatterns(Resource.FUNGI, true), 1);
        assertEquals(field.numOfDiagonalPatterns(Resource.ANIMAL, true), 1);
        assertEquals(field.numOfDiagonalPatterns(Resource.PLANT, false), 1);

        //checking verticalPatterns of differentResources
        assertEquals(field.numOfVerticalPatterns(Resource.INSECT, Resource.ANIMAL, Position.TOPLEFT), 1);
        assertEquals(field.numOfVerticalPatterns(Resource.ANIMAL, Resource.FUNGI, Position.TOPRIGHT), 1);
        assertEquals(field.numOfVerticalPatterns(Resource.PLANT, Resource.INSECT, Position.BOTTOMLEFT), 1);
        assertEquals(field.numOfVerticalPatterns(Resource.FUNGI, Resource.PLANT, Position.BOTTOMRIGHT), 1);


        //creating with JsonParser a list containing all the valid coordinates expected from this instance of field
        List<Coordinates> allValidCoordsCheck = new ArrayList<>();

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader("src/test/resources/AllValidCoords.json"));
        JSONArray coordsArray = (JSONArray) jsonObject.get("coords");
        for (Object o : coordsArray) {
            JSONObject coord = (JSONObject) o;
            int x = ((Long) coord.get("x")).intValue();
            int y = ((Long) coord.get("y")).intValue();
            allValidCoordsCheck.add(new Coordinates(x, y));
        }
        //checking if list of coordinates expected and the one provided by the method match
        assertTrue(allValidCoordsCheck.containsAll(field.getAllValidCoords()));
    }

    /**
     *
     * @param pathname the path containing all the moves(in chronological order) of the game
     * @param field field where moves are applied
     * @throws IOException if the file fails to open
     */
    public void addMovesToField(String pathname, Field field) throws IOException, CoordinatesNotValidException, NotEnoughResourcesException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Move> moves = objectMapper.readValue(new File(pathname), new TypeReference<>() {});
        for(Move move : moves){
            Coordinates coords = new Coordinates(move.getX(), move.getY());
            if(move.getId() >= 41 && move.getId() <= 80){
                field.addCard((GoldCard) CardsConfig.getInstance().getCard(move.getId()),move.isFlipped(), coords);
            }
            else{
                field.addCard((PlayableCard) CardsConfig.getInstance().getCard(move.getId()), move.isFlipped(), coords);
            }
        }
    }
}
