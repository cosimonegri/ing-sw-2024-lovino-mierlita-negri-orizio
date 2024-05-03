package it.polimi.ingsw.network.message;

import it.polimi.ingsw.controller.MainController;
import it.polimi.ingsw.model.deck.card.playablecard.PlayableCard;
import it.polimi.ingsw.model.player.Coordinates;

public class PlayCardMessage implements Message{

    private final String username;

    private final PlayableCard card;

    private final boolean flipped;

    private final Coordinates coords;

    public PlayCardMessage(String username, PlayableCard card, boolean flipped, Coordinates coords){
        this.username = username;
        this.card = card;
        this.flipped = flipped;
        this.coords = coords;
    }

    @Override
    public void execute() {
        //MainController.getInstance().playCard(this.getUsername(),this.getCard(),this.isFlipped(),this.getCoords();
    }

    public String getUsername() {
        return username;
    }

    public PlayableCard getCard() {
        return card;
    }

    public boolean isFlipped() {
        return flipped;
    }

    public Coordinates getCoords() {
        return coords;
    }
}
