package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.CannotCreateGameException;
import it.polimi.ingsw.exceptions.MarkerNotValidException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameListener;
import it.polimi.ingsw.model.GamePhase;
import it.polimi.ingsw.exceptions.LobbyFullException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Marker;
import it.polimi.ingsw.network.message.servertoclient.ServerToClientMessage;

import java.util.List;

public class GameController {
    private final int gameId;
    private final Game model;

    public GameController(int gameId, int playersCount) throws CannotCreateGameException {
        this.gameId = gameId;
        this.model = new Game(playersCount);
    }

    synchronized public int getId() {
        return this.gameId;
    }

    synchronized public void notifyListener(String username, ServerToClientMessage message) {
        model.notifyListener(username, message);
    }

    synchronized public void notifyAllListenersExcept(String username, ServerToClientMessage message) {
        model.notifyAllListenersExcept(username, message);
    }

    synchronized public void notifyAllListeners(ServerToClientMessage message) {
        model.notifyAllListeners(message);
    }

    synchronized public void addPlayer(String username, GameListener listener) throws LobbyFullException {
        if (model.isLobbyFull()) {
            throw new LobbyFullException();
        }
        model.addPlayer(username, listener);
        if (model.isLobbyFull()) {
            model.setGamePhase(GamePhase.MARKER);
        }
    }

    synchronized public void chooseMarker(String username, Marker marker) throws MarkerNotValidException {
        if (model.getGamePhase() != GamePhase.MARKER) {
            return;
        }
        model.assignMarker(username, marker);
        if (model.isLastPlayer(username)) {
            model.setGamePhase(GamePhase.STARTER);
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
