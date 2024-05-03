package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GamePhase;
import it.polimi.ingsw.model.player.Marker;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.client.ClientInterface;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameController {
    private final Game model;
    private final Map<String, GameListener> usernameToListener;

    public GameController(int gameId, int playersCount) {

        this.model = new Game(gameId,playersCount);

        this.usernameToListener = new HashMap<>();
        System.out.println("Created new game with id " + gameId);
    }

    //TODO fix
    synchronized public void addPlayer(String username) {
        try {
            model.addPlayer(username);
            System.out.println(username + " joined the game");
            usernameToListener.put(username, null);
        } catch (Exception e) {
            System.out.println("The lobby is already full");
        } finally {
            if (model.isLobbyFull()) {
                model.start();
                System.out.println("Game started");
            }
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

    synchronized public GamePhase getGamePhase() {
        return this.model.getGamePhase();
    }
}
