package it.polimi.ingsw.network.message.servertoclient;

/**
 * Message sent to the client when the action of playing a card has failed.
 */
public class PlayCardErrorMessage implements ServerToClientMessage {
    /**
     * Error message
     */
    private final String message;

    public PlayCardErrorMessage(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
