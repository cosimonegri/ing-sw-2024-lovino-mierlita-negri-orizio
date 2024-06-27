package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.GameListener;
import it.polimi.ingsw.model.GamePhase;
import it.polimi.ingsw.network.message.servertoclient.ServerToClientMessage;
import it.polimi.ingsw.utilities.Config;

import java.util.*;

/**
 * Control the creation and deletion of new games
 */
public class MainController {
    private static MainController instance;
    private final Map<Integer, GameController> games;
    private final Map<String, GameListener> usernameToListener;


    private MainController() {
        games = new HashMap<>();
        usernameToListener = new HashMap<>();
    }

    public static MainController getInstance() {
        if (instance == null) {
            instance = new MainController();
        }
        return instance;
    }

    /**
     * Add the new player to the map of listeners
     * @param username the new player's username
     * @param listener the new player's game listener
     * @throws UsernameNotValidException when the username is not valid
     * @throws UsernameAlreadyTakenException when a player with this username already exists
     */
    synchronized public void connect(String username, GameListener listener) throws UsernameNotValidException, UsernameAlreadyTakenException {
        if (!Config.isUsernameValid(username)) {
            throw new UsernameNotValidException();
        }
        if (this.isUsernameConnected(username)) {
            throw new UsernameAlreadyTakenException();
        }
        this.usernameToListener.put(username, listener);
    }

    /**
     * Send a message to the specified client
     * @param username the username of the player to be notified
     * @param message the message to send
     */
    synchronized public void notifyListener(String username, ServerToClientMessage message) {
        if (this.isUsernameConnected(username)) {
            usernameToListener.get(username).updateFromModel(message);
        }
    }

    /**
     * Create a new game
     * @param username the player requesting the new game
     * @param playersCount the number of players in the game
     * @return a controller for the new game
     * @throws PlayersCountNotValidException when the players count is not valid
     * @throws CannotCreateGameException when the game controller can't be created
     */
    synchronized public GameController createGame(String username, int playersCount)
            throws PlayersCountNotValidException, CannotCreateGameException
    {
        if (!this.isUsernameConnected(username)) {
            //todo maybe handle this case
            return null;
        }
        int gameId = this.generateGameId();
        try {
            GameController game = new GameController(gameId, playersCount);
            game.addPlayer(username, this.usernameToListener.get(username));
            this.games.put(gameId, game);
            System.out.println("[GAME] Created game " + game.getId());
            return game;
        } catch (LobbyFullException ignored) {
            return null;
        }
    }

    /**
     * Add a player to an existing game
     * @param username the player to be joined
     * @param gameId the game to join
     * @return the game controller for the game joined
     * @throws LobbyNotValidException if the id is not valid
     * @throws LobbyFullException if the game lobby is full
     */
    synchronized public GameController joinGame(String username, int gameId) throws LobbyNotValidException, LobbyFullException {
        if (!this.isUsernameConnected(username)) {
            //todo maybe handle this case
            // check where the pings start
            return null;
        }
        if (!this.games.containsKey(gameId)) {
            throw new LobbyNotValidException();
        }
        GameController game = games.get(gameId);
        game.addPlayer(username, this.usernameToListener.get(username));
        return game;
    }

    /**
     * Remove a player from a lobby/game
     * @param username the player to be removed
     * @return the game controller from which the player is removed
     * @throws UsernameNotPlayingException when the player is not in a game/lobby
     */
    synchronized public GameController leaveGame(String username) throws UsernameNotPlayingException {
        if (!this.isUsernameConnected(username)) {
            throw new UsernameNotPlayingException(username);
        }
        this.usernameToListener.remove(username);
        GameController game = getGameOfPlayer(username);
        game.removePlayer(username);
        // remove game if the game has ended or if the lobby is empty during the waiting phase
        if (game.getPhase() == GamePhase.ENDED || game.getPlayers().isEmpty()) {
            deleteGame(game);
        }
        return game;
    }
    synchronized public GameController getGameOfPlayer(String username) throws UsernameNotPlayingException {
        for (int gameId : this.games.keySet()) {
            GameController game = this.games.get(gameId);
            if (game.getPlayers().stream().anyMatch(p -> p.getUsername().equals(username))) {
                return game;
            }
        }
        throw new UsernameNotPlayingException(username);
    }

    private void deleteGame(GameController game) {
        System.out.println("[GAME] Deleted game " + game.getId());
        this.games.remove(game.getId());
    }

    private boolean isUsernameConnected(String username) {
        return this.usernameToListener.containsKey(username);
    }

    /**
     * Create a new random id for a game
     * @return the new id
     */
    private int generateGameId() {
        Random random = new Random();
        int generatedId;
        do {
            generatedId = random.nextInt(Config.MIN_GAME_ID, Config.MAX_GAME_ID + 1);
        } while (games.containsKey(generatedId));
        return generatedId;
    }
}
