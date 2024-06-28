package it.polimi.ingsw.network.message.servertoclient;

/**
 * Message sent to the client when the action of joining a game has failed.
 */
public class LobbyNotValidMessage implements ServerToClientMessage {
    /**
     * Error message
     */
    private final String message;

    public LobbyNotValidMessage(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
