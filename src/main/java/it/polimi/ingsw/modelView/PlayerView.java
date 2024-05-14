package it.polimi.ingsw.modelView;

import it.polimi.ingsw.model.deck.card.objectivecard.ObjectiveCard;
import it.polimi.ingsw.model.deck.card.playablecard.PlayableCard;
import it.polimi.ingsw.model.player.Marker;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.modelView.cardView.ObjectiveCardView.ObjectiveCardView;
import it.polimi.ingsw.modelView.cardView.PlayableCardView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PlayerView implements Serializable {
    private final String username;
    private final Marker marker;
    private final int score;
    private final PlayableCardView starterCard;
    private final ObjectiveCardView objective;
    private final List<ObjectiveCardView> objectiveOptions = new ArrayList<>();
    private final List<PlayableCardView> hand = new ArrayList<>();
    private final FieldView field;
    private final boolean winner;
    public PlayerView (Player player){
        this.username = player.getUsername();
        this.marker = player.getMarker();
        this.score = player.getScore();
        this.starterCard = player.getStarterCard().getView();
        this.objective = player.getObjCard().getView();
        for(ObjectiveCard card : player.getObjOptions()){
            this.objectiveOptions.add(card.getView());
        }
        for(PlayableCard card: player.getHand()){
            this.hand.add(card.getView());
        }
        this.field = new FieldView(player.getField()); //todo devo inizializzarlo prima?? riga 22, altrimenti togliere anche riga 20 21
        this.winner = player.getIsWinner();
    }


    public String getUsername() {
        return username;
    }

    public Marker getMarker() {
        return marker;
    }

    public int getScore() {
        return score;
    }

    public PlayableCardView getStarterCard() {
        return starterCard;
    }

    public ObjectiveCardView getObjective() {
        return objective;
    }

    public List<PlayableCardView> getHand() {
        return hand;
    }

    public List<ObjectiveCardView> getObjectiveOptions() {
        return objectiveOptions;
    }

    public FieldView getField() {
        return field;
    }

    public boolean isWinner() {
        return winner;
    }
}
