package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameListener;
import it.polimi.ingsw.network.message.servertoclient.PlayCardAckMessage;
import it.polimi.ingsw.network.message.servertoclient.ServerToClientMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MainControllerTest {
    private MainController controller;
    private final GameListener listenerMock = mock(GameListener.class);

    private final String invalidUsername = "abc!";
    private final String playerConnected = "player1";
    private final String playerNotConnected = "playerNotConnected";
    private final int playersCount = 2;

    // Singleton initialized before each test
    @BeforeEach
    public void resetSingleton() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field instance = MainController.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
        controller = MainController.getInstance();
    }

    // connect request validates username and add it to a private map
    @Test
    public void connectTest() {

        // invalid username: bad char
        assertThrows(UsernameNotValidException.class, () -> controller.connect(invalidUsername, listenerMock));
        // valid username
        assertDoesNotThrow(() -> controller.connect(playerConnected, listenerMock));
        // username already taken
        assertThrows(UsernameAlreadyTakenException.class, () -> controller.connect(playerConnected, listenerMock));

        // username and listener added to map
        try {
            Class<? extends MainController> clazz = controller.getClass();
            Field field = clazz.getDeclaredField("usernameToListener");
            field.setAccessible(true);
            Map<String, GameListener> usernameToListener = (HashMap<String, GameListener>)field.get(controller);
            assertTrue(usernameToListener.containsKey(playerConnected) && usernameToListener.get(playerConnected).equals(listenerMock));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.out.println("Could not access usernameToListener Map");
        }
    }

    // player notified if connected
    @Test
    public void notifyListenerTest() {
        ServerToClientMessage message = new PlayCardAckMessage();

        // valid username
        assertDoesNotThrow(() -> controller.connect(playerConnected, listenerMock));

        // no player notified
        controller.notifyListener(playerNotConnected, message);
        verify(listenerMock, never()).updateFromModel(message);
        // player notified one time
        controller.notifyListener(playerConnected , message);
        verify(listenerMock, times(1)).updateFromModel(message);
    }

    // game created with username connected
    @Test
    public void createGameTest() throws PlayersCountNotValidException {


        // valid username
        assertDoesNotThrow(() -> controller.connect(playerConnected, listenerMock));
        try {
            // invalid username
            assertNull(controller.createGame(playerNotConnected, playersCount));
            // game created
            GameController game = controller.createGame(playerConnected, playersCount);

            // username and listener added to map
            Class<? extends MainController> clazz = controller.getClass();
            Field field = clazz.getDeclaredField("games");
            field.setAccessible(true);
            Map<String, GameListener> usernameToListener = (HashMap<String, GameListener>)field.get(controller);
            assertTrue(usernameToListener.containsValue(game));

        } catch (CannotCreateGameException e) {
            System.out.println("Cannot create the game");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.out.println("Could not access usernameToListener Map");
        }
    }

    @Test
    public void joinGameTest() throws PlayersCountNotValidException {
        String player2 = "player2";

        try {
            // valid usernames
            assertDoesNotThrow(() -> controller.connect(playerConnected, listenerMock));
            assertDoesNotThrow(() -> controller.connect(player2, listenerMock));

            GameController game = controller.createGame(playerConnected, playersCount);
            // invalid player
            assertNull(controller.joinGame(playerNotConnected, game.getId()));
            // invalid game id
            assertThrows(LobbyNotValidException.class, () -> controller.joinGame(player2, game.getId() + 1));

            // player added
            assertEquals(controller.joinGame(player2, game.getId()), game);
        } catch (CannotCreateGameException | LobbyFullException | LobbyNotValidException e) {
            System.out.println("Cannot create game");
        }
    }

    @Test
    public void leaveGameTest() throws PlayersCountNotValidException {

        try {
            assertDoesNotThrow(() -> controller.connect(playerConnected, listenerMock));

            // player not connected
            assertThrows(UsernameNotPlayingException.class, () -> controller.leaveGame(playerNotConnected));

            GameController game = controller.createGame(playerConnected, playersCount);
            // player connected
            assertEquals(controller.leaveGame(playerConnected), game);
        } catch (CannotCreateGameException | UsernameNotPlayingException e) {
            System.out.println("Cannot create game");
        }


    }

    @Test
    public void getGameOfPlayerTest() throws PlayersCountNotValidException {
        assertDoesNotThrow(() -> controller.connect(playerConnected, listenerMock));

        try {
            GameController game = controller.createGame(playerConnected, playersCount);
            assertEquals(controller.getGameOfPlayer(playerConnected), game);
            assertThrows(UsernameNotPlayingException.class, () -> controller.getGameOfPlayer(playerNotConnected));
        } catch (CannotCreateGameException | UsernameNotPlayingException e) {
            System.out.println("Could not create game");
        }
    }
}
