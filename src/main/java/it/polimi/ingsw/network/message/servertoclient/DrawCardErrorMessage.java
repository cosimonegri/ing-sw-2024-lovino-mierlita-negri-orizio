package it.polimi.ingsw.network.message.servertoclient;

public class DrawCardErrorMessage implements ServerToClientMessage {
    private final String message;

    public DrawCardErrorMessage(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
