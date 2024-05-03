package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.GamePhase;
import it.polimi.ingsw.model.exceptions.LobbyFullException;
import it.polimi.ingsw.utilities.Config;

import java.util.*;

public class MainController {
    private static MainController instance;
    private final Map<Integer, GameController> games;
    private final List<String> usernames;

    private MainController() {
        games = new HashMap<>();
        usernames = new ArrayList<>();
    }

    public static MainController getInstance() {
        if (instance == null) {
            instance = new MainController();
        }
        return instance;
    }

    synchronized public GameController createGame(String username, int playersCount) {
        if (this.usernames.contains(username)) {
            System.out.println("This username already exists");
            return null;
        }
        this.usernames.add(username);
        int gameId = this.generateGameId();
        GameController game = new GameController(gameId, playersCount);
        game.addPlayer(username);
        this.games.put(gameId, game);
        return game;
    }

    synchronized public GameController joinGame(String username, int gameId) {
        if (this.usernames.contains(username)) {
            System.out.println("This username already exists");
            return null;
        }
        if (!this.games.containsKey(gameId)) {
            System.out.println("This game does not exists");
            return null;
        }
        this.usernames.add(username);
        GameController game = games.get(gameId);
        if (game.getGamePhase() == GamePhase.WAITING) {
            System.out.println("Game already started");
            return null;
        }
        game.addPlayer(username);
        return game;
    }

    synchronized public GameController leaveGame(String username) {
        if (this.usernames.contains(username)) {
            return null;
        }
        this.usernames.remove(username);
        GameController game = getGameOfPlayer(username);
        game.removePlayer(username);
        return game;
    }

    synchronized private GameController getGameOfPlayer(String username) {
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
