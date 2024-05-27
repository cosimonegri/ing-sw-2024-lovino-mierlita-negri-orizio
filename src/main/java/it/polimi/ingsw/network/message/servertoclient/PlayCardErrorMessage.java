package it.polimi.ingsw.network.message.servertoclient;

public class PlayCardErrorMessage implements ServerToClientMessage {
    private final String message;

    public PlayCardErrorMessage(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
