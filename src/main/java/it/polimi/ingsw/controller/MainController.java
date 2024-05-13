package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.GameListener;
import it.polimi.ingsw.network.message.servertoclient.CreateGameAckMessage;
import it.polimi.ingsw.network.message.servertoclient.ServerToClientMessage;
import it.polimi.ingsw.utilities.Config;

import java.io.IOException;
import java.util.*;

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

    synchronized public void connect(String username, GameListener listener) throws UsernameNotValidException, UsernameAlreadyTakenException {
        if (!Config.isUsernameValid(username)) {
            throw new UsernameNotValidException();
        }
        if (this.isUsernameConnected(username)) {
            throw new UsernameAlreadyTakenException();
        }
        this.usernameToListener.put(username, listener);
    }

    synchronized public void notifyListener(String username, ServerToClientMessage message) {
        if (this.isUsernameConnected(username)) {
            usernameToListener.get(username).updateFromModel(message);
        }
    }

    synchronized public void createGame(String username, int playersCount) throws CannotCreateGameException {
        if (!this.isUsernameConnected(username)) {
            return;
        }
        int gameId = this.generateGameId();
        try {
            GameController game = new GameController(gameId, playersCount);
            game.addPlayer(username, this.usernameToListener.get(username));
            this.games.put(gameId, game);
        } catch (LobbyFullException ignored) {}
    }

    synchronized public void joinGame(String username, int gameId) throws LobbyNotValidException, LobbyFullException {
        if (!this.isUsernameConnected(username)) {
            return;
        }
        if (!this.games.containsKey(gameId)) {
            throw new LobbyNotValidException();
        }
        GameController game = games.get(gameId);
        game.addPlayer(username, this.usernameToListener.get(username));
    }

    synchronized public void leaveGame(String username) throws UsernameNotPlayingException {
        if (!this.isUsernameConnected(username)) {
            return;
        }
        this.usernameToListener.remove(username);
        GameController game = getGameOfPlayer(username);
        game.removePlayer(username);
    }

    public GameController getGameOfPlayer(String username) throws UsernameNotPlayingException {
        for (int gameId : this.games.keySet()) {
            GameController game = this.games.get(gameId);
            if (game.getPlayers().stream().anyMatch(p -> p.getUsername().equals(username))) {
                return game;
            }
        }
        throw new UsernameNotPlayingException();
    }

    private boolean isUsernameConnected(String username) {
        return this.usernameToListener.containsKey(username);
    }

    private int generateGameId() {
        Random random = new Random();
        int generatedId;
        do {
            generatedId = random.nextInt(Config.MIN_GAME_ID, Config.MAX_GAME_ID + 1);
        } while (games.containsKey(generatedId));
        return generatedId;
    }
}
