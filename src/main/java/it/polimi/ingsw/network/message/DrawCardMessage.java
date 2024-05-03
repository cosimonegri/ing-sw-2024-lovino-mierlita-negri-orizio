package it.polimi.ingsw.network.message;

import it.polimi.ingsw.controller.DrawType;
import it.polimi.ingsw.controller.MainController;
import it.polimi.ingsw.model.deck.card.playablecard.PlayableCard;

public class DrawCardMessage {
    private final String username;
    private final DrawType type;
    private final PlayableCard card;
    public DrawCardMessage(String username, DrawType type, PlayableCard card){
        this.username = username;
        this.type = type;
        this.card = card;
    }

    public String getUsername() {
        return username;
    }

    public DrawType getType() {
        return type;
    }

    public PlayableCard getCard() {
        return card;
    }
    public void execute(){
        //MainController.getInstance().drawCard(this.getCard(),this.getType().this.getCard());

    }
}
