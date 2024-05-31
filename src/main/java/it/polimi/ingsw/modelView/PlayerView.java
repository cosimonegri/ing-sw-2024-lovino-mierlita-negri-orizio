package it.polimi.ingsw.modelView;

import it.polimi.ingsw.model.deck.card.objectivecard.ObjectiveCard;
import it.polimi.ingsw.model.deck.card.playablecard.PlayableCard;
import it.polimi.ingsw.model.player.Marker;
import it.polimi.ingsw.model.player.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerView implements Serializable, Comparable<PlayerView> {
    private final String username;
    private final Marker marker;
    private final int score;
    private final int objectiveScore;
    private final PlayableCard starterCard;
    private final ObjectiveCard objective;
    private final List<ObjectiveCard> objectiveOptions;
    private final List<PlayableCard> hand;
    private final FieldView field;
    private final boolean winner;

    public PlayerView (Player player){
        this.username = player.getUsername();
        this.marker = player.getMarker();
        this.score = player.getScore();
        this.starterCard = player.getStarterCard();
        this.objective = player.getObjCard();
        this.objectiveScore = player.getObjectiveScore();
        this.objectiveOptions = new ArrayList<>(player.getObjOptions());
        this.hand = new ArrayList<>(player.getHand());
        this.field = player.getField().getView();
        this.winner = player.getIsWinner();
    }


    public String getUsername() {
        return username;
    }

    public Marker getMarker() {
        return marker;
    }

    public int getTotalScore() {
        return score + objectiveScore;
    }

    public int getObjectiveScore() {
        return objectiveScore;
    }

    public PlayableCard getStarterCard() {
        return starterCard;
    }

    public ObjectiveCard getObjective() {
        return objective;
    }

    public List<PlayableCard> getHand() {
        return Collections.unmodifiableList(this.hand);
    }

    public List<ObjectiveCard> getObjectiveOptions() {
        return Collections.unmodifiableList(objectiveOptions);
    }

    public FieldView getField() {
        return field;
    }

    public boolean isWinner() {
        return winner;
    }

    @Override
    public int compareTo(PlayerView other) {
        if (other.getTotalScore() != this.getTotalScore()) {
            return Integer.compare(other.getTotalScore(), this.getTotalScore());
        };
        return Integer.compare(other.objectiveScore, this.objectiveScore);
    }
}
