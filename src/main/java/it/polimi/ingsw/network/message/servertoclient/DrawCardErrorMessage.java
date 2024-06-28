package it.polimi.ingsw.network.message.servertoclient;

/**
 * Message sent to the client when the action of drawing a card has failed.
 */
public class DrawCardErrorMessage implements ServerToClientMessage {
    /**
     * Error message
     */
    private final String message;

    public DrawCardErrorMessage(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
