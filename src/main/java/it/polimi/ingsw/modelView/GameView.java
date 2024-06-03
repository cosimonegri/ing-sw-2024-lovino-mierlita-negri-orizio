package it.polimi.ingsw.modelView;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GamePhase;
import it.polimi.ingsw.model.deck.card.objectivecard.ObjectiveCard;
import it.polimi.ingsw.model.player.Player;

import java.io.Serializable;
import java.util.*;

public class GameView implements Serializable {
    private final int playersCount;
    private final List<PlayerView> players = new ArrayList<>();
    private final PlayerView currentPlayer;
    private final BoardView board;
    private final List<ObjectiveCard> objectives = new ArrayList<>();
    private final boolean isEnded;
    private final int currentTurn;

    public GameView(Game game) {
        this.playersCount = game.getPlayersCount();
        this.currentTurn = game.getCurrentTurn();
        this.currentPlayer = game.getCurrentPlayer().getView();
        for(Player p : game.getPlayers()){
            this.players.add(p.getView());
        }
        this.board = game.getBoard().getView();
        this.objectives.addAll(game.getObjectives());
        this.isEnded = game.getGamePhase() == GamePhase.ENDED;
    }

    public int getPlayersCount() {
        return playersCount;
    }

    public List<PlayerView> getPlayers() {
        return Collections.unmodifiableList(this.players);
    }

    /**
     * @return list of players sorted from highest to lowest score
     */
    public List<PlayerView> getSortedPlayers() {
        List<PlayerView> players = new ArrayList<>(this.players);
        players.sort(PlayerView::compareTo);
        return players;
    }

    public PlayerView getPlayer(String username) {
        for (PlayerView p : this.players) {
            if (p.getUsername().equals(username)) {
                return p;
            }
        }
        return null;
    }

    public PlayerView getCurrentPlayer() {
        return currentPlayer;
    }

    public int getCurrentTurn() { return currentTurn; }

    public boolean isCurrentPlayer(String username) {
        return this.getCurrentPlayer().getUsername().equals(username);
    }

    public BoardView getBoard() {
        return board;
    }

    public List<ObjectiveCard> getObjectives() {
        return Collections.unmodifiableList(this.objectives);
    }

    public boolean isEnded() {
        return this.isEnded;
    }
}
