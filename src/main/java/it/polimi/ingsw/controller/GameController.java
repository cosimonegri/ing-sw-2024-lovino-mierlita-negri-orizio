package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.UsernameAlreadyTakenException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameListener;
import it.polimi.ingsw.model.GamePhase;
import it.polimi.ingsw.model.player.Marker;
import it.polimi.ingsw.exceptions.LobbyFullException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.message.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameController {
    private final Game model;

    public GameController(int playersCount) {
        this.model = new Game(playersCount);
        System.out.println("Created a new game");
    }

    public void notifyListener(String username, Message message) {
        model.notifyListener(username, message);
    }

    public void notifyAllListenersExcept(String username, Message message) {
        model.notifyAllListenersExcept(username, message);
    }

    public void notifyAllListeners(Message message) {
        model.notifyAllListeners(message);
    }

    //TODO maybe we can remove checks on username validity in game
    synchronized public void addPlayer(String username, GameListener listener) throws LobbyFullException {
        try {
            model.addPlayer(username, listener);
        } catch (UsernameAlreadyTakenException ignored) {
        } finally {
            if (model.isLobbyFull()) {
                model.start();
                System.out.println("Game started");
            }
        }
    }

    synchronized public void removePlayer(String username) {
        model.removePlayer(username);
    }

    synchronized public List<Player> getPlayers() {
        return model.getPlayers();
    }

    synchronized public GamePhase getPhase() {
        return this.model.getGamePhase();
    }
}
