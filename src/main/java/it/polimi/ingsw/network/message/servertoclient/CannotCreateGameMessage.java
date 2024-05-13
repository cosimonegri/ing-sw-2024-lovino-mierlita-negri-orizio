package it.polimi.ingsw.network.message.servertoclient;

public class CannotCreateGameMessage implements ServerToClientMessage {
    public String getMessage() {
        return "Cannot create a game. Try again later.";
    }
}
