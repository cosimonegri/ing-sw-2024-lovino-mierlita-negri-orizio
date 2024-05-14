package it.polimi.ingsw.modelView;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.deck.card.objectivecard.ObjectiveCard;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.modelView.cardView.ObjectiveCardView.ObjectiveCardView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameView implements Serializable {
    private int id;
    private final int playersCount;
    private final List<PlayerView> players = new ArrayList<>();
    private final PlayerView currentPlayer;
    private final BoardView board;
    private final List<ObjectiveCardView> objectives = new ArrayList<>();
    public GameView(Game game){
        //this.id = game.getId();
        this.playersCount = game.getPlayersCount();
        this.currentPlayer = game.getCurrentPlayer().getView();
        for(Player p : game.getPlayers()){
            this.players.add(p.getView());
        }
        this.board = game.getBoard().getView();
        for(ObjectiveCard obj: game.getObjectives()){
            this.objectives.add(obj.getView());
        }
    }

    public int getId() {
        return id;
    }

    public int getPlayersCount() {
        return playersCount;
    }

    public List<PlayerView> getPlayers() {
        return players;
    }


    public PlayerView getCurrentPlayer() {
        return currentPlayer;
    }

    public BoardView getBoard() {
        return board;
    }

    public List<ObjectiveCardView> getObjectives() {
        return objectives;
    }
}
