package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.deck.Deck;
import it.polimi.ingsw.model.deck.card.objectivecard.ObjectiveCard;
import it.polimi.ingsw.model.deck.card.playablecard.GoldCard;
import it.polimi.ingsw.model.deck.card.playablecard.PlayableCard;
import it.polimi.ingsw.model.deck.card.playablecard.corner.Corner;
import it.polimi.ingsw.model.deck.card.playablecard.corner.CornerType;
import it.polimi.ingsw.model.deck.card.playablecard.corner.Position;
import it.polimi.ingsw.model.player.Coordinates;
import it.polimi.ingsw.model.player.Field;
import it.polimi.ingsw.model.player.Marker;
import it.polimi.ingsw.model.player.Player;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GameControllerTest {
    private GameController controller;
    private Game gameMock;
    private final GameListener listenerMock = mock(GameListener.class);

    private static List<String> names;

    @BeforeAll
    public static void setAttributes() {
        names = new ArrayList<>();
        names.add("player1");
        names.add("player2");
        names.add("player3");
        names.add("player4");
    }

    @BeforeEach
    public void resetController() throws CannotCreateGameException {
        try {
            controller = new GameController(1, 3);
        } catch (PlayersCountNotValidException ignored) {}
    }

    private void mockModel() throws NoSuchFieldException, IllegalAccessException {
        gameMock = mock(Game.class);
        java.lang.reflect.Field privateField = GameController.class.getDeclaredField("model");
        privateField.setAccessible(true);
        privateField.set(controller, gameMock);
    }

    @Test
    public void addPlayerTest() {
        //TODO error when username already in use
        assertEquals(controller.getPhase(), GamePhase.WAITING);

        // add first player
        assertDoesNotThrow(() -> controller.addPlayer(names.getFirst(), listenerMock));
        assertEquals(1, controller.getPlayers().size());
        assertEquals(controller.getPhase(), GamePhase.WAITING);

        // add second player
        assertDoesNotThrow(() -> controller.addPlayer(names.get(1), listenerMock));
        assertEquals(2, controller.getPlayers().size());
        assertEquals(controller.getPhase(), GamePhase.WAITING);

        // add last player
        assertDoesNotThrow(() -> controller.addPlayer(names.get(2), listenerMock));
        assertEquals(3, controller.getPlayers().size());
        assertEquals(controller.getPhase(), GamePhase.STARTER);

        // lobby already full
        assertThrows(LobbyFullException.class,
                () -> controller.addPlayer(names.get(3), listenerMock)
        );
        assertEquals(3, controller.getPlayers().size());
        assertEquals(controller.getPhase(), GamePhase.STARTER);
    }

    @Test
    public void removePlayerTest() throws NoSuchFieldException, IllegalAccessException {
        assertDoesNotThrow(() -> controller.addPlayer(names.getFirst(), listenerMock));
        assertDoesNotThrow(() -> controller.addPlayer(names.get(1), listenerMock));

        // remove player in waiting phase
        controller.removePlayer(names.getFirst());
        assertEquals(1, controller.getPlayers().size());
        assertEquals(names.get(1), controller.getPlayers().getFirst().getUsername());
        assertEquals(controller.getPhase(), GamePhase.WAITING);

        // remove player during the game
        this.mockModel();
        int times = 0;
        for (GamePhase phase : GamePhase.values()) {
            if (phase != GamePhase.WAITING) {
                times++;
                when(gameMock.getGamePhase()).thenReturn(phase);
                controller.removePlayer(names.get(1));
                verify(gameMock, times(times)).setGamePhase(GamePhase.ENDED);
                verify(gameMock, times(0)).removePlayer(names.get(1));
                assertDoesNotThrow(() -> controller.addPlayer(names.get(1), listenerMock));
            }
        }
    }

    @Test
    public void chooseMarkerTest() throws NoSuchFieldException, IllegalAccessException {
        this.mockModel();
        Player player = new Player(names.getFirst());
        Player otherPlayer = mock(Player.class);
        when(otherPlayer.getMarker()).thenReturn(Marker.RED);

        // wrong game phase
        for (GamePhase phase : GamePhase.values()) {
            if (phase != GamePhase.STARTER) {
                when(gameMock.getGamePhase()).thenReturn(phase);
                assertThrows(ActionNotValidException.class,
                        () -> controller.chooseMarker(player.getUsername(), Marker.RED)
                );
            }
        }
        when(gameMock.getGamePhase()).thenReturn(GamePhase.STARTER);

        // player not valid
        assertThrows(ActionNotValidException.class,
                () -> controller.chooseMarker(player.getUsername(), Marker.RED)
        );
        when(gameMock.getPlayer(player.getUsername())).thenReturn(Optional.of(player));

        // marker already taken
        when(gameMock.getPlayers()).thenReturn(Arrays.asList(player, otherPlayer));
        assertThrows(MarkerNotValidException.class,
                () -> controller.chooseMarker(player.getUsername(), Marker.RED)
        );
        when(gameMock.getPlayers()).thenReturn(List.of(player));

        // valid marker
        assertNull(player.getMarker());
        assertDoesNotThrow(() -> controller.chooseMarker(player.getUsername(), Marker.RED));
        assertEquals(Marker.RED, player.getMarker());

        // player already has marker
        assertThrows(ActionNotValidException.class,
                () -> controller.chooseMarker(player.getUsername(), Marker.GREEN)
        );
        assertEquals(Marker.RED, player.getMarker());
    }

    @Test
    public void playStarterTest() throws NoSuchFieldException, IllegalAccessException {
        this.mockModel();
        Player player = new Player(names.getFirst());
        PlayableCard starter = mock(PlayableCard.class);
        Coordinates coords = new Coordinates(player.getField().size() / 2, player.getField().size() / 2);

        // wrong game phase
        for (GamePhase phase : GamePhase.values()) {
            if (phase != GamePhase.STARTER) {
                when(gameMock.getGamePhase()).thenReturn(phase);
                assertThrows(ActionNotValidException.class,
                        () -> controller.playStarter(player.getUsername(), false)
                );
            }
        }
        when(gameMock.getGamePhase()).thenReturn(GamePhase.STARTER);

        // player not valid
        assertThrows(ActionNotValidException.class,
                () -> controller.playStarter(player.getUsername(), false)
        );
        when(gameMock.getPlayer(player.getUsername())).thenReturn(Optional.of(player));

        // play starter
        player.setStarterCard(starter);
        assertEquals(0, player.getField().getCardsCount());
        assertDoesNotThrow(() -> controller.playStarter(player.getUsername(), false));
        assertEquals(1, player.getField().getCardsCount());
        assertEquals(starter, player.getField().getPlacedCard(coords).card());
        assertFalse(player.getField().getPlacedCard(coords).flipped());

        // player has already played the starter card
        assertThrows(ActionNotValidException.class,
                () -> controller.playStarter(player.getUsername(), true)
        );
        assertEquals(1, player.getField().getCardsCount());
        assertEquals(starter, player.getField().getPlacedCard(coords).card());
        assertFalse(player.getField().getPlacedCard(coords).flipped());
    }

    @Test
    public void chooseObjectiveTest() throws NoSuchFieldException, IllegalAccessException {
        this.mockModel();
        Player player = new Player(names.getFirst());
        ObjectiveCard objective = mock(ObjectiveCard.class);

        // wrong game phase
        for (GamePhase phase : GamePhase.values()) {
            if (phase != GamePhase.OBJECTIVE) {
                when(gameMock.getGamePhase()).thenReturn(phase);
                assertThrows(ActionNotValidException.class,
                        () -> controller.chooseObjective(player.getUsername(), objective)
                );
            }
        }
        when(gameMock.getGamePhase()).thenReturn(GamePhase.OBJECTIVE);

        // player not valid
        assertThrows(ActionNotValidException.class,
                () -> controller.chooseObjective(player.getUsername(), objective)
        );
        when(gameMock.getPlayer(player.getUsername())).thenReturn(Optional.of(player));

        // ojective option not valid
        player.addObjOption(mock(ObjectiveCard.class));
        assertThrows(CardNotInHandException.class,
                () -> controller.chooseObjective(player.getUsername(), objective)
        );

        // objective card added
        player.addObjOption(objective);
        assertNull(player.getObjCard());
        assertDoesNotThrow(() -> controller.chooseObjective(player.getUsername(), objective));
        assertEquals(objective, player.getObjCard());

        // player already has an objective
        assertThrows(ActionNotValidException.class,
                () -> controller.chooseObjective(player.getUsername(), objective)
        );
        assertEquals(objective, player.getObjCard());
    }

    @Test
    public void playCardTest() throws NoSuchFieldException, IllegalAccessException {
        this.mockModel();
        Player player = new Player(names.getFirst());
        PlayableCard card = mock(PlayableCard.class);
        GoldCard goldCard = mock(GoldCard.class);
        when(card.getPoints()).thenReturn(1);
        when(goldCard.getTotalPoints(player.getField())).thenReturn(3);

        Corner corner = new Corner(CornerType.VISIBLE, null);
        for (Position pos : Position.values()) {
            when(card.getCorner(pos, true)).thenReturn(corner);
            when(card.getCorner(pos, false)).thenReturn(corner);
            when(goldCard.getCorner(pos, true)).thenReturn(corner);
            when(goldCard.getCorner(pos, false)).thenReturn(corner);
        }

        Coordinates invalidCoords = new Coordinates(0, 0);
        int centralCoord = player.getField().size() / 2;
        Coordinates coords1 = new Coordinates(centralCoord, centralCoord);
        Coordinates coords2 = new Coordinates(centralCoord + 1, centralCoord + 1);
        Coordinates coords3 = new Coordinates(centralCoord + 2, centralCoord + 2);
        Coordinates coords4 = new Coordinates(centralCoord + 3, centralCoord + 3);

        // invalid game phase
        for (GamePhase phase : GamePhase.values()) {
            if (phase != GamePhase.MAIN) {
                when(gameMock.getGamePhase()).thenReturn(phase);
                assertThrows(ActionNotValidException.class,
                        () -> controller.playCard(player.getUsername(), card, false, coords1)
                );
            }
        }

        // invalid turn phase
        when(gameMock.getGamePhase()).thenReturn(GamePhase.MAIN);
        when(gameMock.getTurnPhase()).thenReturn(TurnPhase.DRAW);
        assertThrows(ActionNotValidException.class,
                () -> controller.playCard(player.getUsername(), card, false, coords1)
        );
        when(gameMock.getTurnPhase()).thenReturn(TurnPhase.PLAY);

        // not current player
        assertThrows(ActionNotValidException.class,
                () -> controller.playCard(player.getUsername(), card, false, coords1)
        );
        when(gameMock.getCurrentPlayer()).thenReturn(new Player(names.get(1)));
        assertThrows(ActionNotValidException.class,
                () -> controller.playCard(player.getUsername(), card, false, coords1)
        );
        when(gameMock.getCurrentPlayer()).thenReturn(player);

        // card not in hand
        assertThrows(CardNotInHandException.class,
                () -> controller.playCard(player.getUsername(), card, false, coords1)
        );
        player.addToHand(card);
        player.addToHand(goldCard);

        // coordinates not valid
        assertThrows(CoordinatesNotValidException.class,
                () -> controller.playCard(player.getUsername(), card, false, invalidCoords)
        );

        // not enough resources
        when(goldCard.hasResourcesNeeded(player.getField())).thenReturn(false);
        assertThrows(NotEnoughResourcesException.class,
                () -> controller.playCard(player.getUsername(), goldCard, false, coords1)
        );

        // play resource card + advance when not last round
        when(gameMock.isLastRound()).thenReturn(false);
        assertDoesNotThrow(() -> controller.playCard(player.getUsername(), card, false, coords1));
        assertEquals(1, player.getField().getCardsCount());
        assertEquals(card, player.getField().getPlacedCard(coords1).card());
        assertFalse(player.getField().getPlacedCard(coords1).flipped());
        assertEquals(1, player.getHand().size());
        for (PlayableCard c : player.getHand()) {
            assertNotEquals(card, c);
        }
        assertEquals(1, player.getScore());
        verify(gameMock, times(1)).setTurnPhase(TurnPhase.DRAW);

        // play resource card flipped + advance when last round
        when(gameMock.isLastRound()).thenReturn(true);
        player.addToHand(card);
        assertDoesNotThrow(() -> controller.playCard(player.getUsername(), card, true, coords2));
        assertEquals(2, player.getField().getCardsCount());
        assertEquals(card, player.getField().getPlacedCard(coords2).card());
        assertTrue(player.getField().getPlacedCard(coords2).flipped());
        assertEquals(1, player.getHand().size());
        for (PlayableCard c : player.getHand()) {
            assertNotEquals(card, c);
        }
        assertEquals(1, player.getScore());
        verify(gameMock, times(1)).advanceTurn();

        // play gold card flipped (when not having enought resource)
        assertDoesNotThrow(() -> controller.playCard(player.getUsername(), goldCard, true, coords3));
        assertEquals(3, player.getField().getCardsCount());
        assertEquals(goldCard, player.getField().getPlacedCard(coords3).card());
        assertTrue(player.getField().getPlacedCard(coords3).flipped());
        assertEquals(0, player.getHand().size());
        assertEquals(1, player.getScore());

        // play gold card (when having enough resource)
        when(goldCard.hasResourcesNeeded(player.getField())).thenReturn(true);
        player.addToHand(goldCard);
        assertDoesNotThrow(() -> controller.playCard(player.getUsername(), goldCard, false, coords4));
        assertEquals(4, player.getField().getCardsCount());
        assertEquals(goldCard, player.getField().getPlacedCard(coords4).card());
        assertFalse(player.getField().getPlacedCard(coords4).flipped());
        assertEquals(0, player.getHand().size());
        assertEquals(4, player.getScore());
    }

    @Test
    public void drawCardTest() throws IOException, NoSuchFieldException, IllegalAccessException {
        this.mockModel();
        Player player = new Player(names.getFirst());
        Board board = new Board();

        Board emptyBoard = mock(Board.class);
        Deck<PlayableCard> resourceDeck = mock(Deck.class);
        Deck<GoldCard> goldDeck = mock(Deck.class);
        when(emptyBoard.getResourceDeck()).thenReturn(resourceDeck);
        when(emptyBoard.getGoldDeck()).thenReturn(goldDeck);
        when(resourceDeck.isEmpty()).thenReturn(true);
        when(goldDeck.isEmpty()).thenReturn(true);

        // invalid game phase
        for (GamePhase phase : GamePhase.values()) {
            if (phase != GamePhase.MAIN) {
                when(gameMock.getGamePhase()).thenReturn(phase);
                assertThrows(ActionNotValidException.class,
                        () -> controller.drawCard(player.getUsername(), DrawType.RESOURCE, null)
                );
            }
        }

        // invalid turn phase
        when(gameMock.getGamePhase()).thenReturn(GamePhase.MAIN);
        when(gameMock.getTurnPhase()).thenReturn(TurnPhase.PLAY);
        assertThrows(ActionNotValidException.class,
                () -> controller.drawCard(player.getUsername(), DrawType.RESOURCE, null)
        );
        when(gameMock.getTurnPhase()).thenReturn(TurnPhase.DRAW);

        // not current player
        assertThrows(ActionNotValidException.class,
                () -> controller.drawCard(player.getUsername(), DrawType.RESOURCE, null)
        );
        when(gameMock.getCurrentPlayer()).thenReturn(new Player(names.get(1)));
        assertThrows(ActionNotValidException.class,
                () -> controller.drawCard(player.getUsername(), DrawType.RESOURCE, null)
        );
        when(gameMock.getCurrentPlayer()).thenReturn(player);

        // draw resource + advance turn
        when(gameMock.getBoard()).thenReturn(board);
        PlayableCard card = board.getResourceDeck().getTop();
        assertDoesNotThrow(() -> controller.drawCard(player.getUsername(), DrawType.RESOURCE, null));
        assertNotEquals(card, board.getResourceDeck().getTop());
        assertEquals(1, player.getHand().size());
        assertEquals(card, player.getHand().getFirst());
        verify(gameMock, times(1)).advanceTurn();
        verify(gameMock, times(1)).setTurnPhase(TurnPhase.PLAY);

        // draw gold + advance turn
        GoldCard goldCard = board.getGoldDeck().getTop();
        assertDoesNotThrow(() -> controller.drawCard(player.getUsername(), DrawType.GOLD, null));
        assertNotEquals(goldCard, board.getGoldDeck().getTop());
        assertEquals(2, player.getHand().size());
        assertEquals(goldCard, player.getHand().get(1));
        verify(gameMock, times(2)).advanceTurn();
        verify(gameMock, times(2)).setTurnPhase(TurnPhase.PLAY);

        // draw a visible card + advance turn
        PlayableCard visibleCard = board.getVisibleCards()[0];
        assertDoesNotThrow(() -> controller.drawCard(player.getUsername(), DrawType.VISIBLE, visibleCard));
        for (PlayableCard c : board.getVisibleCards()) {
            assertNotNull(c);
            assertNotEquals(visibleCard, c);
        }
        assertEquals(3, player.getHand().size());
        assertEquals(visibleCard, player.getHand().get(2));
        verify(gameMock, times(3)).advanceTurn();
        verify(gameMock, times(3)).setTurnPhase(TurnPhase.PLAY);

        // visible card not valid
        assertThrows(CardNotOnBoardException.class,
                () -> controller.drawCard(player.getUsername(), DrawType.VISIBLE, null)
        );
        assertThrows(CardNotOnBoardException.class,
                () -> controller.drawCard(player.getUsername(), DrawType.VISIBLE, mock(PlayableCard.class))
        );
        assertEquals(3, player.getHand().size());

        // resource deck empty
        when(gameMock.getBoard()).thenReturn(emptyBoard);
        assertThrows(EmptyDeckException.class,
                () -> controller.drawCard(player.getUsername(), DrawType.RESOURCE, null)
        );
        assertEquals(3, player.getHand().size());

        // gold deck empty + not advance turn
        assertThrows(EmptyDeckException.class,
                () -> controller.drawCard(player.getUsername(), DrawType.GOLD, null)
        );
        assertEquals(3, player.getHand().size());
        verify(gameMock, times(3)).advanceTurn();
        verify(gameMock, times(3)).setTurnPhase(TurnPhase.PLAY);
    }

    @Test
    public void gameFlowTest() {
        int times;
        int turn = 1;

        // simulate some cards
        PlayableCard card10points = mock(PlayableCard.class);
        PlayableCard card5points = mock(PlayableCard.class);
        when(card10points.getPoints()).thenReturn(10);
        when(card5points.getPoints()).thenReturn(5);
        Corner corner = new Corner(CornerType.VISIBLE, null);
        for (Position pos : Position.values()) {
            when(card10points.getCorner(pos, true)).thenReturn(corner);
            when(card10points.getCorner(pos, false)).thenReturn(corner);
            when(card5points.getCorner(pos, true)).thenReturn(corner);
            when(card5points.getCorner(pos, false)).thenReturn(corner);
        }
        int centralCoord = Field.SIZE / 2;
        Coordinates coords1 = new Coordinates(centralCoord + 1, centralCoord + 1);
        Coordinates coords2 = new Coordinates(centralCoord + 2, centralCoord + 2);
        Coordinates coords3 = new Coordinates(centralCoord + 3, centralCoord + 3);

        assertEquals(3, controller.getPlayersCount());
        assertEquals(Optional.empty(), controller.getRemainingTurns());

        // WAITING PHASE
        assertDoesNotThrow(() -> controller.addPlayer(names.getFirst(), listenerMock));
        assertDoesNotThrow(() -> controller.addPlayer(names.get(1), listenerMock));
        for (Player player : controller.getPlayers()) {
            assertNull(player.getStarterCard());
        }
        assertDoesNotThrow(() -> controller.addPlayer(names.get(2), listenerMock));
        for (Player player : controller.getPlayers()) {
            assertNotNull(player.getStarterCard());
        }
        assertEquals(4, controller.getModelView().getBoard().getVisibleCards().length);
        times = 0;
        for (Player player : controller.getPlayers()) {
            times++;
            assertFalse(controller.isCurrentPlayer(player.getUsername()));
        }
        assertEquals(3, times);

        // STARTER PHASE
        assertDoesNotThrow(() -> controller.playStarter(names.get(2), true));
        assertDoesNotThrow(() -> controller.chooseMarker(names.get(1), Marker.GREEN));
        assertDoesNotThrow(() -> controller.playStarter(names.get(1), false));
        assertDoesNotThrow(() -> controller.playStarter(names.getFirst(), true));
        assertDoesNotThrow(() -> controller.chooseMarker(names.getFirst(), Marker.RED));
        for (Player player : controller.getPlayers()) {
            assertEquals(0, player.getHand().size());
            assertEquals(0, player.getObjOptions().size());
        }
        assertEquals(0, controller.getModelView().getObjectives().size());
        assertDoesNotThrow(() -> controller.chooseMarker(names.get(2), Marker.BLUE));
        for (Player player : controller.getPlayers()) {
            assertEquals(3, player.getHand().size());
            assertEquals(2, player.getObjOptions().size());
        }
        assertEquals(2, controller.getModelView().getObjectives().size());

        // OBJECTIVE PHASE
        times = 0;
        for (Player player : controller.getPlayers()) {
            times++;
            assertDoesNotThrow(() -> controller.chooseObjective(
                    player.getUsername(), player.getObjOptions().getFirst())
            );
        }
        assertEquals(3, times);
        assertEquals(turn, controller.getModelView().getCurrentTurn());
        assertFalse(controller.getRemainingTurns().isPresent());
        assertEquals(3, controller.getPlayers().size());
        assertTrue(controller.getModelView().getCurrentPlayer().isPresent());
        assertTrue(controller.isCurrentPlayer(controller.getModelView().getCurrentPlayer().get().getUsername()));

        // simulate the objective cards
        Player player2 = controller.getPlayers().get(1);
        ObjectiveCard objective5points = mock(ObjectiveCard.class);
        when(objective5points.getTotalPoints(player2.getField())).thenReturn(5);
        player2.setObjCard(objective5points);
        Player player3 = controller.getPlayers().get(2);
        ObjectiveCard objective3points = mock(ObjectiveCard.class);
        when(objective3points.getTotalPoints(player3.getField())).thenReturn(3);
        player3.setObjCard(objective3points);

        // MAIN PHASE
        for (Player player : controller.getPlayers()) {
            turn++;
            assertDoesNotThrow(() -> player.removeFromHand(player.getHand().getFirst()));
            player.addToHand(card10points);
            assertDoesNotThrow(() -> controller.playCard(player.getUsername(), card10points, false, coords1));
            assertThrows(ActionNotValidException.class,
                    () -> controller.playCard(player.getUsername(), card10points, false, coords1)
            );
            assertDoesNotThrow(() -> controller.drawCard(player.getUsername(), DrawType.RESOURCE, null));
            assertThrows(ActionNotValidException.class,
                    () -> controller.drawCard(player.getUsername(), DrawType.GOLD, null)
            );
            assertEquals(turn, controller.getModelView().getCurrentTurn());
            assertFalse(controller.getRemainingTurns().isPresent());
        }
        times = 0;
        for (Player player : controller.getPlayers()) {
            turn++;
            times++;
            assertDoesNotThrow(() -> player.removeFromHand(player.getHand().getFirst()));
            if (times == 1) {
                player.addToHand(card5points);
                assertDoesNotThrow(() -> controller.playCard(player.getUsername(), card5points, false, coords2));
            } else {
                player.addToHand(card10points);
                assertDoesNotThrow(() -> controller.playCard(player.getUsername(), card10points, false, coords2));
            }
            assertDoesNotThrow(() -> controller.drawCard(player.getUsername(), DrawType.GOLD, null));
            assertEquals(turn, controller.getModelView().getCurrentTurn());
            if (times == 1) {
                assertFalse(controller.getRemainingTurns().isPresent());
            } else {
                assertTrue(controller.getRemainingTurns().isPresent());
                assertEquals(2 * controller.getPlayers().size() - times, controller.getRemainingTurns().get());
            }
        }
        for (Player player : controller.getPlayers()) {
            assertDoesNotThrow(() -> player.removeFromHand(player.getHand().getFirst()));
            player.addToHand(card10points);
            assertDoesNotThrow(() -> controller.playCard(player.getUsername(), card10points, false, coords3));
            turn++;
            assertEquals(turn, controller.getModelView().getCurrentTurn());
            times++;
            assertTrue(controller.getRemainingTurns().isPresent());
            assertEquals(2 * controller.getPlayers().size() - times, controller.getRemainingTurns().get());
        }

        // GAME ENDED - calculate objective points
        assertEquals(GamePhase.ENDED, controller.getPhase());
        assertEquals(25, controller.getPlayers().getFirst().getView().getTotalScore());
        assertEquals(35, controller.getPlayers().get(1).getView().getTotalScore());
        assertEquals(33, controller.getPlayers().get(2).getView().getTotalScore());
        // TODO maybe move this gameview test to gameview
        assertEquals(
                controller.getPlayers().get(1).getUsername(),
                controller.getModelView().getSortedPlayers().getFirst().getUsername()
        );
        assertEquals(
                controller.getPlayers().get(2).getUsername(),
                controller.getModelView().getSortedPlayers().get(1).getUsername()
        );
        assertEquals(
                controller.getPlayers().getFirst().getUsername(),
                controller.getModelView().getSortedPlayers().get(2).getUsername()
        );
    }
}
