package it.polimi.ingsw.network.message.servertoclient;

/**
 * Message sent to the client when the action of creating a game has failed.
 */
public class CreateGameErrorMessage implements ServerToClientMessage {
    /**
     * Error message
     */
    private final String message;

    public CreateGameErrorMessage(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
