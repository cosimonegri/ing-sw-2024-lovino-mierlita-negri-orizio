package it.polimi.ingsw.network.message.clienttoserver.gamecontroller;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.deck.card.playablecard.PlayableCard;
import it.polimi.ingsw.model.player.Coordinates;

public class PlayCardMessage extends GameControllerMessage {
    private final PlayableCard card;

    private final boolean flipped;

    private final Coordinates coords;

    public PlayCardMessage (String username, PlayableCard card, boolean flipped, Coordinates coords) {
        super(username);
        this.card = card;
        this.flipped = flipped;
        this.coords = coords;
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

    @Override
    public void execute(GameController controller) {
        controller.playCard(this.getUsername(), this.card, this.flipped, this.coords);
    }
}
