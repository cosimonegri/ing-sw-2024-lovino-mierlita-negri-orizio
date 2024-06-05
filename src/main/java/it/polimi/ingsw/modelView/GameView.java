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
    private final boolean isLastRound;
    private final boolean isEnded;
    private final int currentTurn;

    public GameView(Game game) {
        this.playersCount = game.getPlayersCount();
        this.currentTurn = game.getCurrentTurn();
        this.currentPlayer = game.getCurrentPlayer() != null ? game.getCurrentPlayer().getView() : null;
        for(Player p : game.getPlayers()){
            this.players.add(p.getView());
        }
        this.board = game.getBoard().getView();
        this.objectives.addAll(game.getObjectives());
        this.isLastRound = game.isLastRound();
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
        //todo maybe raise exception
        return null;
    }

    public Optional<PlayerView> getCurrentPlayer() {
        return Optional.ofNullable(currentPlayer);
    }

    public int getCurrentTurn() { return currentTurn; }

    public boolean isCurrentPlayer(String username) {
        if (this.currentPlayer == null) {
            return false;
        }
        return this.currentPlayer.getUsername().equals(username);
    }

    public BoardView getBoard() {
        return board;
    }

    public List<ObjectiveCard> getObjectives() {
        return Collections.unmodifiableList(this.objectives);
    }

    public boolean isLastRound() {
        return this.isLastRound;
    }

    public boolean isEnded() {
        return this.isEnded;
    }
}
