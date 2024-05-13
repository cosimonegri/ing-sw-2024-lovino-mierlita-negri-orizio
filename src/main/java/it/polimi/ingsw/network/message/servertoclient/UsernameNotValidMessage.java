package it.polimi.ingsw.network.message.servertoclient;

public class UsernameNotValidMessage implements ServerToClientMessage {
    private final String message;

    public UsernameNotValidMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
