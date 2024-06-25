package it.polimi.ingsw.network.message.servertoclient;

public class CreateGameErrorMessage implements ServerToClientMessage {
    private final String message;

    public CreateGameErrorMessage(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
