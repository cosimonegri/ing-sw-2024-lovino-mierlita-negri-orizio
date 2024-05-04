package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.UsernameNotValidException;
import it.polimi.ingsw.model.GameListener;
import it.polimi.ingsw.exceptions.UsernameAlreadyTakenException;
import it.polimi.ingsw.model.GamePhase;
import it.polimi.ingsw.model.deck.card.objectivecard.ObjectiveCard;
import it.polimi.ingsw.model.exceptions.LobbyFullException;
import it.polimi.ingsw.model.GamePhase;
import it.polimi.ingsw.utilities.Config;

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
        if (this.usernameToListener.containsKey(username)) {
            throw new UsernameAlreadyTakenException();
        }
        this.usernameToListener.put(username, listener);
    }

    synchronized public void createGame(String username, int playersCount) {
        if (!this.usernameToListener.containsKey(username)) {
            return;
        }
        int gameId = this.generateGameId();
        GameController game = new GameController(playersCount);
        game.addPlayer(username, this.usernameToListener.get(username));
        this.games.put(gameId, game);
    }

    synchronized public void joinGame(String username, int gameId) {
        if (!this.usernameToListener.containsKey(username)) {
            return;
        }
        if (!this.games.containsKey(gameId)) {
            return;
        }
        GameController game = games.get(gameId);
        if (game.getPhase() == GamePhase.WAITING) {
            return;
        }
        game.addPlayer(username, this.usernameToListener.get(username));
    }

    synchronized public void leaveGame(String username) {
        if (!this.usernameToListener.containsKey(username)) {
            return;
        }
        this.usernameToListener.remove(username);
        getGameOfPlayer(username).removePlayer(username);
    }

    private GameController getGameOfPlayer(String username) {
        for (int gameId : this.games.keySet()) {
            GameController game = this.games.get(gameId);
            if (game.getPlayers().stream().anyMatch(p -> p.getUsername().equals(username))) {
                return game;
            }
        }
        return null; //TODO raise exception
    }

    private int generateGameId() {
        Random random = new Random();
        int generatedId;
        do {
            generatedId = random.nextInt(Config.MAX_GAME_ID);
        } while (games.containsKey(generatedId));
        return generatedId;
    }
}
