package it.polimi.ingsw.network.message.servertoclient;

/**
 * Message sent to the client when the action of choosing the username has failed.
 */
public class UsernameNotValidMessage implements ServerToClientMessage {
    /**
     * Error message
     */
    private final String message;

    public UsernameNotValidMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
