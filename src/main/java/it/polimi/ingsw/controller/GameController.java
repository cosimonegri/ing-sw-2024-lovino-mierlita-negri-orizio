package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameListener;
import it.polimi.ingsw.model.GamePhase;
import it.polimi.ingsw.model.player.Marker;
import it.polimi.ingsw.model.player.Player;

import java.util.List;

public class GameController {
    private final Game model;

    public GameController(int playersCount) {
        this.model = new Game(playersCount);
        System.out.println("Created a new game");
    }

    //TODO fix
    synchronized public void addPlayer(String username, GameListener listener) {
        try {
            model.addPlayer(username, listener);
            System.out.println(username + " joined the game");
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
        } catch (Exception e) {

        }
    }

    synchronized public List<Player> getPlayers() {
        return model.getPlayers();
    }

    synchronized public GamePhase getGamePhase() {
        return this.model.getGamePhase();
    }
}
