package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.player.Marker;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.client.ClientInterface;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameController {
    private Game model;
    private Map<String, GameListener> usernameToListener;

    public GameController(int gameId, int playersCount) {
        this.model = new Game(playersCount);
        this.usernameToListener = new HashMap<>();
    }

    //TODO fix
    synchronized public void addPlayer(String username) {
        try {
            model.addPlayer(username, Marker.RED);
            usernameToListener.put(username, null);
        } catch (Exception e) {

        }
    }

    synchronized public void removePlayer(String username) {
        try {
            model.removePlayer(username);
            usernameToListener.remove(username);
        } catch (Exception e) {

        }
    }

    synchronized public List<Player> getPlayers() {
        return model.getPlayers();
    }

    synchronized private void notifyAllClients() {
        for (GameListener listener : usernameToListener.values()) {

        }
    }
}
