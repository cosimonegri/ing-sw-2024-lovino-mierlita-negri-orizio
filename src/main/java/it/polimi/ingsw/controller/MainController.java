package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.GameListener;
import it.polimi.ingsw.network.message.servertoclient.CreateGameAckMessage;
import it.polimi.ingsw.network.message.servertoclient.PingRequest;
import it.polimi.ingsw.network.message.servertoclient.ServerToClientMessage;
import it.polimi.ingsw.utilities.Config;

import java.io.IOException;
import java.util.*;

import static java.lang.Thread.sleep;

public class MainController {
    private static MainController instance;
    private final Map<Integer, GameController> games;
    private final Map<String, GameListener> usernameToListener;
    private final Map<String, Timer> usernameToTimer;
    //

    private MainController() {
        games = new HashMap<>();
        usernameToListener = new HashMap<>();
        usernameToTimer = new HashMap<>();

    }

    public static MainController getInstance() {
        if (instance == null) {
            instance = new MainController();
        }
        return instance;
    }

    synchronized public void pingResponse(String username) {
        try {
            // cancel timer
            this.usernameToTimer.get(username).cancel();
            // wait some time
            wait(5000);
            // send new request
            this.pingRequest(username);
        } catch (InterruptedException  | NullPointerException ignored) {
            System.err.println("Could not find the user timer");
        }
    }

    private void pingRequest(String username) {
        // send ping request
        notifyListener(username, new PingRequest(username));
        // create new timer
        Timer timer = new Timer();
        // if the player doesn't respond leave the game
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    leaveGame(username);
                } catch (UsernameNotPlayingException e) {
                    System.err.println("Username not playing");
                }
            }
        }, 5000);

        usernameToTimer.put(username, timer);
    }

    synchronized public void connect(String username, GameListener listener) throws UsernameNotValidException, UsernameAlreadyTakenException {
        if (!Config.isUsernameValid(username)) {
            throw new UsernameNotValidException();
        }
        if (this.isUsernameConnected(username)) {
            throw new UsernameAlreadyTakenException();
        }
        this.usernameToListener.put(username, listener);
        this.pingRequest(username);
    }

    synchronized public void notifyListener(String username, ServerToClientMessage message) {
        if (this.isUsernameConnected(username)) {
            usernameToListener.get(username).updateFromModel(message);
        }
    }

    synchronized public GameController createGame(String username, int playersCount) throws CannotCreateGameException {
        if (!this.isUsernameConnected(username)) {
            //todo maybe handle this case
            return null;
        }
        int gameId = this.generateGameId();
        try {
            GameController game = new GameController(gameId, playersCount);
            game.addPlayer(username, this.usernameToListener.get(username));
            this.games.put(gameId, game);
            return game;
        } catch (LobbyFullException ignored) {
            return null;
        }
    }

    synchronized public GameController joinGame(String username, int gameId) throws LobbyNotValidException, LobbyFullException {
        if (!this.isUsernameConnected(username)) {
            //todo maybe handle this case
            return null;
        }
        if (!this.games.containsKey(gameId)) {
            throw new LobbyNotValidException();
        }
        GameController game = games.get(gameId);
        game.addPlayer(username, this.usernameToListener.get(username));
        return game;
    }

    synchronized public void leaveGame(String username) throws UsernameNotPlayingException {
        if (!this.isUsernameConnected(username)) {
            return;
        }
        System.err.println(username + " leaving the game");
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
