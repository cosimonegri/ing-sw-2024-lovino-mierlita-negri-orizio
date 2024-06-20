package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.deck.Deck;
import it.polimi.ingsw.model.deck.card.objectivecard.ObjectiveCard;
import it.polimi.ingsw.model.deck.card.playablecard.GoldCard;
import it.polimi.ingsw.model.deck.card.playablecard.PlayableCard;
import it.polimi.ingsw.model.player.Coordinates;
import it.polimi.ingsw.model.player.Marker;
import it.polimi.ingsw.model.player.Player;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

public class GameControllerTest {
    private GameController game;
    private final Game gameMock = Mockito.mock(Game.class);
    private static List<String> names;

    private final GameListener listenerMock = mock(GameListener.class);

    @BeforeAll
    public static void setAttributes() {
        names = new ArrayList<>();
        names.add("player1");
        names.add("player2");
        names.add("player3");
    }

    @BeforeEach
    public void resetGame() {
        try {
            game = new GameController(1, 2);
            // substitute model with mockito
            Field privateField = GameController.class.getDeclaredField("model");
            privateField.setAccessible(true);
            privateField.set(game, gameMock);
        } catch (CannotCreateGameException e) {
            System.out.println("Could not create game");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void addPlayerTest() {
        // add first player
        when(gameMock.isLobbyFull()).thenReturn(false);
        Assertions.assertDoesNotThrow(() -> game.addPlayer(names.getFirst(), listenerMock));

        // after second player the lobby is full
        when(gameMock.isLobbyFull()).thenReturn(false).thenReturn(true); // First call returns false, second call returns true
        Assertions.assertDoesNotThrow(() -> game.addPlayer(names.get(1), listenerMock));
        verify(gameMock, times(1)).setGamePhase(GamePhase.STARTER);
        verify(gameMock, times(1)).giveStarterCards();

        // lobby already full
        when(gameMock.isLobbyFull()).thenReturn(true);
        Assertions.assertThrows(LobbyFullException.class,
                () -> game.addPlayer(names.get(2), listenerMock));
    }

    @Test
    public void removePlayerTest() {
        // remove player in waiting phase
        when(gameMock.getGamePhase()).thenReturn(GamePhase.WAITING);
        game.removePlayer(names.getFirst());
        verify(gameMock, times(1)).removePlayer(names.getFirst());
        // end game
        when(gameMock.getGamePhase()).thenReturn(GamePhase.MAIN);
        game.removePlayer(names.getFirst());
        verify(gameMock, times(1)).setGamePhase(GamePhase.ENDED);
    }

    @Test
    public void chooseMarkerTest() {
        Player playerWithMarker = mock(Player.class),
            playerWithoutMarker = mock(Player.class);
        when(playerWithMarker.getMarker()).thenReturn(Marker.RED);

        // gamePhase not STARTER
        when(gameMock.getGamePhase()).thenReturn(GamePhase.MAIN);
        Assertions.assertThrows(ActionNotValidException.class,
                () -> game.chooseMarker(names.getFirst(), Marker.RED));

        // player with marker
        when(gameMock.getGamePhase()).thenReturn(GamePhase.STARTER);
        when(gameMock.getPlayer(names.getFirst())).thenReturn(playerWithMarker);
        Assertions.assertThrows(ActionNotValidException.class,
                () -> game.chooseMarker(names.getFirst(), Marker.RED));

        // invalid player
        when(gameMock.getPlayer(names.getFirst())).thenReturn(null);
        Assertions.assertThrows(ActionNotValidException.class,
                () -> game.chooseMarker(names.getFirst(), Marker.RED));

        // marker already taken
        List<Player> lp = new ArrayList<>();
        lp.add(playerWithMarker);
        when(gameMock.getPlayer(names.getFirst())).thenReturn(playerWithoutMarker);
        when(gameMock.getPlayers()).thenReturn(lp);
        Assertions.assertThrows(MarkerNotValidException.class,
                () -> game.chooseMarker(names.getFirst(), Marker.RED));

        // valid marker
        when(lp.getFirst().getMarker()).thenReturn(null);
        Assertions.assertDoesNotThrow(() -> game.chooseMarker(names.getFirst(), Marker.RED));
    }

    @Test
    public void playStarterTest() {
        Player player = mock(Player.class);

        // wrong game phase
        when(gameMock.getGamePhase()).thenReturn(GamePhase.WAITING);
        Assertions.assertThrows(ActionNotValidException.class,
                () -> game.playStarter(names.getFirst(), false));

        // players has cards
        when(gameMock.getGamePhase()).thenReturn(GamePhase.STARTER);
        when(gameMock.getPlayer(names.getFirst())).thenReturn(player);
        when(player.getField()).thenReturn(mock(it.polimi.ingsw.model.player.Field.class));
        when(player.getField().getCardsCount()).thenReturn(1);
        Assertions.assertThrows(ActionNotValidException.class,
                () -> game.playStarter(names.getFirst(), false));

        when(player.getField().getCardsCount()).thenReturn(0);
        Assertions.assertDoesNotThrow(() -> game.playStarter(names.getFirst(), false));
    }

    @Test
    public void chooseObjectiveTest() {
        Player player = mock(Player.class);
        ObjectiveCard oc = mock(ObjectiveCard.class);

        // wrong game phase
        when(gameMock.getGamePhase()).thenReturn(GamePhase.WAITING);
        Assertions.assertThrows(ActionNotValidException.class,
                () -> game.chooseObjective(names.getFirst(), oc));

        // players has objective
        when(gameMock.getGamePhase()).thenReturn(GamePhase.OBJECTIVE);
        when(player.getObjCard()).thenReturn(oc);
        Assertions.assertThrows(ActionNotValidException.class,
                () -> game.chooseObjective(names.getFirst(), oc));

        // option not valid
        when(player.getObjCard()).thenReturn(null);
        when(player.getObjOptions()).thenReturn(null);
        Assertions.assertThrows(CardNotInHandException.class,
                () -> game.chooseObjective(names.getFirst(), oc));

        // objective card added
        List<ObjectiveCard> loc = new ArrayList<>();
        loc.add(oc);
        when(player.getObjOptions()).thenReturn(loc);
        Assertions.assertDoesNotThrow(() -> game.chooseObjective(names.getFirst(), oc));
    }

    @Test
    public void playCardTest() {
        Player player = mock(Player.class);
        when(player.getUsername()).thenReturn(names.getFirst());
        when(player.getField()).thenReturn(mock(it.polimi.ingsw.model.player.Field.class));


        PlayableCard pc = mock(PlayableCard.class);
        Coordinates coord = new Coordinates(0, 0);
        List<PlayableCard> hand = new ArrayList<>();
        hand.add(pc);

        // invalid game phase
        when(gameMock.getGamePhase()).thenReturn(GamePhase.OBJECTIVE);
        Assertions.assertThrows(ActionNotValidException.class,
                () -> game.playCard(names.getFirst(), pc, false, coord));

        // invalid turn phase
        when(gameMock.getGamePhase()).thenReturn(GamePhase.MAIN);
        when(gameMock.getTurnPhase()).thenReturn(TurnPhase.DRAW);
        Assertions.assertThrows(ActionNotValidException.class,
                () -> game.playCard(names.getFirst(), pc, false, coord));

        // not players' turn
        when(gameMock.getTurnPhase()).thenReturn(TurnPhase.PLAY);
        Assertions.assertThrows(ActionNotValidException.class,
                () -> game.playCard(names.getFirst(), pc, false, coord));

        // card not in hand
        when(gameMock.getCurrentPlayer()).thenReturn(player);
        Assertions.assertThrows(CardNotInHandException.class,
                () -> game.playCard(names.getFirst(), pc, false, coord));


        // card in hand
        when(player.getHand()).thenReturn(hand);
        // last round
        Assertions.assertDoesNotThrow(() -> game.playCard(names.getFirst(), pc, false, coord));
        // the card is removed
        verify(player, times(1)).removeFromHand(pc);
        // add points
        verify(player, times(1)).increaseScore(anyInt());
        // new phase
        verify(gameMock, times(1)).setTurnPhase(TurnPhase.DRAW);
    }

    @Test
    public void drawCardTest() {
        Player player = mock(Player.class);
        when(player.getUsername()).thenReturn(names.getFirst());
        when(player.getField()).thenReturn(mock(it.polimi.ingsw.model.player.Field.class));


        PlayableCard pc = mock(PlayableCard.class);
        List<PlayableCard> hand = new ArrayList<>();
        hand.add(pc);

        // invalid game phase
        when(gameMock.getGamePhase()).thenReturn(GamePhase.OBJECTIVE);
        Assertions.assertThrows(ActionNotValidException.class,
                () -> game.drawCard(names.getFirst(), DrawType.VISIBLE, pc));

        // invalid turn phase
        when(gameMock.getGamePhase()).thenReturn(GamePhase.MAIN);
        when(gameMock.getTurnPhase()).thenReturn(TurnPhase.PLAY);
        Assertions.assertThrows(ActionNotValidException.class,
                () -> game.drawCard(names.getFirst(), DrawType.VISIBLE, pc));

        // not current player
        when(gameMock.getTurnPhase()).thenReturn(TurnPhase.DRAW);
        Assertions.assertThrows(ActionNotValidException.class,
                () -> game.drawCard(names.getFirst(), DrawType.VISIBLE, pc));


        when(gameMock.getCurrentPlayer()).thenReturn(player);
        Board board = mock(Board.class);
        Deck<PlayableCard> deck = mock(Deck.class);
        when(deck.draw()).thenReturn(pc);

        // Resource deck empty
        when(gameMock.getBoard()).thenReturn(board);
        when(board.getResourceDeck()).thenReturn(deck);
        when(deck.isEmpty()).thenReturn(true);
        Assertions.assertThrows(EmptyDeckException.class,
                () -> game.drawCard(names.getFirst(), DrawType.RESOURCE, pc));
        // resource deck not empty
        when(deck.isEmpty()).thenReturn(false);
        Assertions.assertDoesNotThrow(() -> game.drawCard(names.getFirst(), DrawType.RESOURCE, pc));
        verify(player, times(1)).addToHand(pc);

        // Gold deck empty
        Deck<GoldCard> goldDeck = mock(Deck.class);
        when(board.getGoldDeck()).thenReturn(goldDeck);
        when(goldDeck.isEmpty()).thenReturn(true);
        Assertions.assertThrows(EmptyDeckException.class,
                () -> game.drawCard(names.getFirst(), DrawType.GOLD, pc));
        // resource deck not empty
        when(goldDeck.isEmpty()).thenReturn(false);
        Assertions.assertDoesNotThrow(() -> game.drawCard(names.getFirst(), DrawType.GOLD, pc));
        verify(player, times(1)).addToHand(pc);

        // Visible deck empty
        PlayableCard[] visible = new PlayableCard[1];
        when(board.getVisibleCards()).thenReturn(visible);
        Assertions.assertThrows(CardNotOnBoardException.class,
                () -> game.drawCard(names.getFirst(), DrawType.VISIBLE, pc));
        // resource deck not empty
        visible[0] = pc;
        Assertions.assertDoesNotThrow(() -> game.drawCard(names.getFirst(), DrawType.VISIBLE, pc));
        verify(board, times(1)).replaceVisibleCard(pc);
    }
}
