package it.polimi.ingsw.modelView.cardView;

import it.polimi.ingsw.model.deck.card.playablecard.PlayableCard;
import it.polimi.ingsw.model.deck.card.playablecard.corner.Corner;
import it.polimi.ingsw.model.deck.card.playablecard.corner.Resource;
import it.polimi.ingsw.modelView.cardView.CardView;

import java.io.Serializable;
import java.util.List;

public class PlayableCardView extends CardView implements Serializable
{
    private final int id;
    private final int points;
    private final List<Corner> corners;
    private final List<Resource> backResources;
    public PlayableCardView(PlayableCard card){
        this.id = card.getId();
        this.points = card.getPoints();
        this.corners = card.getCorners();
        this.backResources = card.getBackResources();
    }
    public int getId(){
        return this.id;
    }
    public int getPoints(){
        return this.points;
    }

    public List<Corner> getCorners() {
        return corners;
    }
    public List<Resource> getBackResources() {
        return backResources;
    }
    public Resource getColor() {
        if (this.backResources.size() == 1) {
            return this.backResources.getFirst();
        }
        return null;}
}
