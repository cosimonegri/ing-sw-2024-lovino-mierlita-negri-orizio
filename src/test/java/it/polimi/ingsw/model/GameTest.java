package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.deck.card.objectivecard.ObjectiveCard;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.utilities.Config;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class GameTest {
    private Game game;
    private final GameListener listenerMock = mock(GameListener.class);

    private static List<String> names;

    @BeforeAll
    public static void setAttributes() {
        names = new ArrayList<>();
        names.add("player1");
        names.add("player2");
        names.add("player3");
        names.add("player4");
        names.add("player5");
    }

    @BeforeEach
    public void gameTest() throws CannotCreateGameException {
        try {
            game = new Game(4);
        } catch (PlayersCountNotValidException ignored) { }
    }

    @Test
    @DisplayName("Constructor Test")
    public void constructorTest() {
        for (int playersCount = -1; playersCount <= 5; playersCount++) {
            int finalCount = playersCount;
            if (playersCount < 2 || playersCount > 4) {
                assertThrows(PlayersCountNotValidException.class, () -> new Game(finalCount));
            } else {
                assertDoesNotThrow(() -> new Game(finalCount));
            }
        }
    }


    @Test
    @DisplayName("Add, get and remove player")
    public void lobbyTest() {
        // add first player
        assertEquals(0, game.getPlayers().size());
        assertDoesNotThrow(() -> game.addPlayer(names.getFirst(), listenerMock));
        assertEquals(1, game.getPlayers().size());
        assertEquals(names.getFirst(), game.getPlayers().getFirst().getUsername());
        assertTrue(game.getPlayer(names.getFirst()).isPresent());
        assertEquals(names.getFirst(), game.getPlayer(names.getFirst()).get().getUsername());

        // add other players
        assertDoesNotThrow(() -> game.addPlayer(names.get(1), listenerMock));
        assertDoesNotThrow(() -> game.addPlayer(names.get(2), listenerMock));
        assertDoesNotThrow(() -> game.addPlayer(names.get(3), listenerMock));

        // get players
        assertEquals(4, game.getPlayers().size());
        for (Player player : game.getPlayers()){
            assertTrue(names.contains(player.getUsername()));
        }
        for (String username : names.subList(0, 4)) {
            assertTrue(game.getPlayer(username).isPresent());
            assertEquals(username, game.getPlayer(username).get().getUsername());
        }
        assertTrue(game.getPlayer(names.get(4)).isEmpty());

        // exception when lobby full
        assertThrows(LobbyFullException.class, () -> game.addPlayer(names.get(4), listenerMock));
        assertEquals(4, game.getPlayers().size());
        for (Player player : game.getPlayers()) {
            assertNotEquals(names.get(4), player.getUsername());
        }
        assertTrue(game.getPlayer(names.get(4)).isEmpty());

        // remove player not in lobby
        game.removePlayer(names.get(4));
        assertEquals(4, game.getPlayers().size());

        // remove first player
        game.removePlayer(names.getFirst());
        assertEquals(3, game.getPlayers().size());
        for (Player player : game.getPlayers()) {
            assertNotEquals(names.getFirst(), player.getUsername());
        }
        assertTrue(game.getPlayer(names.getFirst()).isEmpty());

        // remove other players
        game.removePlayer(names.get(3));
        game.removePlayer(names.get(2));
        game.removePlayer(names.get(1));
        assertEquals(0, game.getPlayers().size());
    }

    @Test
    @DisplayName("Is lobby full test")
    public void isLobbyFullTest() {
        for (String name : names.subList(0, 4)) {
            assertFalse(game.isLobbyFull());
            assertDoesNotThrow(() -> game.addPlayer(name, listenerMock));
        }
        assertTrue(game.isLobbyFull());
        for (String name : names.subList(0, 4)) {
            game.removePlayer(name);
            assertFalse(game.isLobbyFull());
        }
    }

    @Test
    @DisplayName("Give starter cards test")
    public void giveStarterCardsTest() {
        for (String name : names.subList(0, 4)) {
            assertDoesNotThrow(() -> game.addPlayer(name, listenerMock));
        }
        for (Player player : game.getPlayers()) {
            assertNull(player.getStarterCard());
        }
        assertEquals(6, game.getBoard().getStarterDeck().size());
        game.giveStarterCards();
        for (Player player : game.getPlayers()) {
            assertNotNull(player.getStarterCard());
        }
        assertEquals(2, game.getBoard().getStarterDeck().size());
    }

    @Test
    @DisplayName("Fill players' hands and draw common objectives test")
    public void fillHandsAndDrawObjctivesTest() {
        for (String name : names.subList(0, 4)) {
            assertDoesNotThrow(() -> game.addPlayer(name, listenerMock));
        }

        // initial situation
        for (Player player : game.getPlayers()) {
            assertEquals(0, player.getHand().size());
            assertEquals(0, player.getObjOptions().size());
        }
        assertEquals(0, game.getObjectives().size());
        assertEquals(38, game.getBoard().getResourceDeck().size());
        assertEquals(38, game.getBoard().getGoldDeck().size());
        assertEquals(16, game.getBoard().getObjectiveDeck().size());

        // situation after filling players' hands
        game.fillPlayerHands();
        for (Player player : game.getPlayers()) {
            assertEquals(3, player.getHand().size());
            assertEquals(2, player.getObjOptions().size());
        }
        assertEquals(0, game.getObjectives().size());
        assertEquals(30, game.getBoard().getResourceDeck().size());
        assertEquals(34, game.getBoard().getGoldDeck().size());
        assertEquals(8, game.getBoard().getObjectiveDeck().size());

        // situation after drawing common objectives
        game.drawCommonObjectives();
        for (Player player : game.getPlayers()) {
            assertEquals(3, player.getHand().size());
            assertEquals(2, player.getObjOptions().size());
        }
        assertEquals(2, game.getObjectives().size());
        assertEquals(30, game.getBoard().getResourceDeck().size());
        assertEquals(34, game.getBoard().getGoldDeck().size());
        assertEquals(6, game.getBoard().getObjectiveDeck().size());
    }

    @Test
    @DisplayName("Start test")
    public void startTest() {
        for (String name : names.subList(0, 4)) {
            assertDoesNotThrow(() -> game.addPlayer(name, listenerMock));
        }
        assertNull(game.getCurrentPlayer());
        assertEquals(0, game.getCurrentTurn());
        game.start();
        assertNotNull(game.getCurrentPlayer());
        assertEquals(game.getPlayers().getFirst(), game.getCurrentPlayer());
        assertEquals(1, game.getCurrentTurn());
    }

    @Test
    @DisplayName("Calculate points from objectives test")
    public void calculateObjectivePointsTest() throws NoSuchFieldException, IllegalAccessException {
        for (String name : names.subList(0, 4)) {
            assertDoesNotThrow(() -> game.addPlayer(name, listenerMock));
        }

        // set common objectives
        ObjectiveCard objective7points = mock(ObjectiveCard.class);
        ObjectiveCard objective0points = mock(ObjectiveCard.class);
        List<ObjectiveCard> objectives = Arrays.asList(objective7points, objective0points);
        Field privateField = Game.class.getDeclaredField("objectives");
        privateField.setAccessible(true);
        privateField.set(game, objectives);
        for (Player player : game.getPlayers()) {
            when(objective7points.getTotalPoints(player.getField())).thenReturn(7);
            when(objective0points.getTotalPoints(player.getField())).thenReturn(0);
        }

        // set private objective of player 1 and player 2
        Player player1 = game.getPlayers().getFirst();
        ObjectiveCard objective2points = mock(ObjectiveCard.class);
        when(objective2points.getTotalPoints(player1.getField())).thenReturn(2);
        player1.setObjCard(objective2points);
        Player player2 = game.getPlayers().get(1);
        ObjectiveCard objective3points = mock(ObjectiveCard.class);
        when(objective3points.getTotalPoints(player2.getField())).thenReturn(3);
        player2.setObjCard(objective3points);

        // set private objective of player 3 and 4
        Player player3 = game.getPlayers().get(2);
        player3.setObjCard(objective0points);
        Player player4 = game.getPlayers().get(3);
        player4.setObjCard(objective0points);

        // increase base score of some players
        player1.increaseScore(2);
        player2.increaseScore(5);
        player4.increaseScore(10);

        game.calculateObjectivePoints();
        assertEquals(2, player1.getScore());
        assertEquals(9, player1.getObjectiveScore());
        assertEquals(5, player2.getScore());
        assertEquals(10, player2.getObjectiveScore());
        assertEquals(0, player3.getScore());
        assertEquals(7, player3.getObjectiveScore());
        assertEquals(10, player4.getScore());
        assertEquals(7, player4.getObjectiveScore());
    }

    @Test
    @DisplayName("Advance turn test")
    public void advanceTurnTest() {
        for (String name : names.subList(0, 4)) {
            assertDoesNotThrow(() -> game.addPlayer(name, listenerMock));
        }
        game.start();
        assertEquals(game.getPlayers().getFirst(), game.getCurrentPlayer());
        assertEquals(1, game.getCurrentTurn());

        game.advanceTurn(); // 1st to 2nd
        assertEquals(game.getPlayers().get(1), game.getCurrentPlayer());
        assertEquals(2, game.getCurrentTurn());

        game.advanceTurn(); // 2nd to 3rd
        assertEquals(game.getPlayers().get(2), game.getCurrentPlayer());
        assertEquals(3, game.getCurrentTurn());

        game.advanceTurn(); // 3rd to 4th
        assertEquals(game.getPlayers().get(3), game.getCurrentPlayer());
        assertEquals(4, game.getCurrentTurn());

        game.advanceTurn(); // 4th to 1st
        assertEquals(game.getPlayers().getFirst(), game.getCurrentPlayer());
        assertEquals(5, game.getCurrentTurn());
    }

    @Test
    @DisplayName("Test game end when a player reaches 20 points")
    public void gameEndMaxScore1Test() {
        for (String name : names.subList(0, 4)) {
            assertDoesNotThrow(() -> game.addPlayer(name, listenerMock));
        }
        game.start();
        assertFalse(game.isLastRound());
        game.advanceTurn();

        // 2nd player reaches 20 points
        game.getCurrentPlayer().increaseScore(Config.SCORE_FOR_FINAL_PHASE);
        assertFalse(game.isLastRound());
        assertTrue(game.getRemainingTurns().isEmpty());
        game.advanceTurn();

        // finish current round
        assertFalse(game.isLastRound());
        assertTrue(game.getRemainingTurns().isPresent());
        assertEquals(6, game.getRemainingTurns().get());
        game.advanceTurn();

        game.getCurrentPlayer().increaseScore(Config.SCORE_FOR_FINAL_PHASE);
        assertFalse(game.isLastRound());
        assertTrue(game.getRemainingTurns().isPresent());
        assertEquals(5, game.getRemainingTurns().get());
        game.advanceTurn();

        // finish last round
        assertTrue(game.isLastRound());
        assertTrue(game.getRemainingTurns().isPresent());
        assertEquals(4, game.getRemainingTurns().get());
        game.advanceTurn();

        game.getCurrentPlayer().increaseScore(Config.SCORE_FOR_FINAL_PHASE);
        assertTrue(game.isLastRound());
        assertTrue(game.getRemainingTurns().isPresent());
        assertEquals(3, game.getRemainingTurns().get());
        game.advanceTurn();

        assertTrue(game.isLastRound());
        assertTrue(game.getRemainingTurns().isPresent());
        assertEquals(2, game.getRemainingTurns().get());
        game.advanceTurn();

        game.getCurrentPlayer().increaseScore(Config.SCORE_FOR_FINAL_PHASE);
        assertTrue(game.isLastRound());
        assertTrue(game.getRemainingTurns().isPresent());
        assertEquals(1, game.getRemainingTurns().get());
        game.advanceTurn();

        assertTrue(game.isLastRound());
        assertTrue(game.getRemainingTurns().isPresent());
        assertEquals(0, game.getRemainingTurns().get());
    }

    @Test
    @DisplayName("Test game end when the last player reaches 20 points")
    public void gameEndMaxScore2Test() {
        for (String name : names.subList(0, 4)) {
            assertDoesNotThrow(() -> game.addPlayer(name, listenerMock));
        }
        game.start();
        assertFalse(game.isLastRound());
        game.advanceTurn();
        assertFalse(game.isLastRound());
        game.advanceTurn();
        assertFalse(game.isLastRound());
        game.advanceTurn();

        // 4th player reaches 20 points
        game.getCurrentPlayer().increaseScore(Config.SCORE_FOR_FINAL_PHASE);
        assertFalse(game.isLastRound());
        assertTrue(game.getRemainingTurns().isEmpty());
        game.advanceTurn();

        // 1st player last round
        assertTrue(game.isLastRound());
        assertTrue(game.getRemainingTurns().isPresent());
        assertEquals(4, game.getRemainingTurns().get());
        game.advanceTurn();

        // 2nd player last round
        assertTrue(game.isLastRound());
        assertTrue(game.getRemainingTurns().isPresent());
        assertEquals(3, game.getRemainingTurns().get());
        game.advanceTurn();

        // 3rd player last round + nothing happens if the decks finish or another player reached 20 points
        for (int i = 0; i < 38; i++) {
            game.getBoard().getResourceDeck().draw();
            game.getBoard().getGoldDeck().draw();
        }
        game.getCurrentPlayer().increaseScore(Config.SCORE_FOR_FINAL_PHASE);
        assertTrue(game.isLastRound());
        assertTrue(game.getRemainingTurns().isPresent());
        assertEquals(2, game.getRemainingTurns().get());
        game.advanceTurn();

        // 4th player last round
        assertTrue(game.isLastRound());
        assertTrue(game.getRemainingTurns().isPresent());
        assertEquals(1, game.getRemainingTurns().get());
        game.advanceTurn();

        // game ended
        assertTrue(game.isLastRound());
        assertTrue(game.getRemainingTurns().isPresent());
        assertEquals(0, game.getRemainingTurns().get());
    }

    @Test
    @DisplayName("Test game end when the decks are empty")
    public void gameEndEmptyDecksTest() {
        for (String name : names.subList(0, 4)) {
            assertDoesNotThrow(() -> game.addPlayer(name, listenerMock));
        }
        game.start();
        assertFalse(game.isLastRound());
        game.advanceTurn();
        assertFalse(game.isLastRound());
        game.advanceTurn();

        // decks finish during the turn of the 3rd player
        for (int i = 0; i < 38; i++) {
            game.getBoard().getResourceDeck().draw();
            game.getBoard().getGoldDeck().draw();
        }
        assertFalse(game.isLastRound());
        assertTrue(game.getRemainingTurns().isEmpty());
        game.advanceTurn();

        // finish current round
        assertFalse(game.isLastRound());
        assertTrue(game.getRemainingTurns().isPresent());
        assertEquals(5, game.getRemainingTurns().get());
        game.advanceTurn();

        // 1st player last round
        assertTrue(game.isLastRound());
        assertTrue(game.getRemainingTurns().isPresent());
        assertEquals(4, game.getRemainingTurns().get());
        game.advanceTurn();

        // 2nd player last round + nothing happens if a player reached 20 points
        game.getCurrentPlayer().increaseScore(Config.SCORE_FOR_FINAL_PHASE);
        assertTrue(game.isLastRound());
        assertTrue(game.getRemainingTurns().isPresent());
        assertEquals(3, game.getRemainingTurns().get());
        game.advanceTurn();

        // 3rd player last round
        assertTrue(game.isLastRound());
        assertTrue(game.getRemainingTurns().isPresent());
        assertEquals(2, game.getRemainingTurns().get());
        game.advanceTurn();

        // 4th player last round
        assertTrue(game.isLastRound());
        assertTrue(game.getRemainingTurns().isPresent());
        assertEquals(1, game.getRemainingTurns().get());
        game.advanceTurn();

        // game ended
        assertTrue(game.isLastRound());
        assertTrue(game.getRemainingTurns().isPresent());
        assertEquals(0, game.getRemainingTurns().get());
    }
}
