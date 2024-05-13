package it.polimi.ingsw.network.message.servertoclient;

public class LobbyNotValidMessage implements ServerToClientMessage {
    private final String message;

    public LobbyNotValidMessage(String message){
        this.message = message;
    }

    public String toString() {
        return message;
    }
}
